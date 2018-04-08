package fr.upmc.sluck.controllers;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.acl.Owner;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.upmc.sluck.Application;
import fr.upmc.sluck.activities.ConversationActivity;
import fr.upmc.sluck.model.Message;
import fr.upmc.sluck.model.MessageSentUpdate;
import fr.upmc.sluck.network.client.Sender;
import fr.upmc.sluck.utils.exceptions.UtilException;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.utils.Util;

/**
 * Created by ktare on 18/03/2018.
 */

public class GlobalController {
    private List<Channel> myChannels, otherChannels, availableChannels;
    private final Sender sender;

    public GlobalController(Sender sender) {
        myChannels = new LinkedList<>();
        otherChannels = new LinkedList<>();
        availableChannels = new LinkedList<>();
        myChannels.addAll(readMyChannels());
        this.sender = sender;
    }


    public void addAvailableChannel(String channelName, String owner) {
        Channel c = new Channel(channelName, null, owner, false);
        availableChannels.add(c);
        ConversationActivity.adapterac.notifyDataSetChanged();
    }

    public Channel addNewLocalChannel(String name) throws UtilException {
        List<String> users = new LinkedList<>();
        users.add(Application.getUserName());
        Util.createChannelFolder(name, users, Application.getUserName());
        Channel channel = new Channel(name, users, Application.getUserName(), true);
        myChannels.add(channel);
        sender.notifyChannelCreation(name);
        return channel;
    }

    public Channel addNewUser(String channelName, String userName) {
        Channel channel = getChannel(channelName);
        if (channel != null) {
            channel.getUsers().add(userName);
            if (myChannels.contains(channel)) {
                JSONArray jUsers = new JSONArray();
                channel.getUsers().forEach(jUsers::put);
                try {
                    Util.eraseAndWriteInFile(channelName, Util.CHANNEL_INFO_FILE_NAME,
                            new JSONObject().put("users", jUsers).put("owner", Application.getUserName()).toString());
                } catch (JSONException | UtilException e) {
                    e.printStackTrace();
                    Log.v("01", "Cannot add user to channel.", e);
                }
            }
        }
        return channel;
    }

    public Channel getChannel(String channelName) {
        for (Channel c : myChannels) {
            if (c.getName().equals(channelName)) return c;
        }
        for (Channel c : otherChannels) {
            if (c.getName().equals(channelName)) return c;
        }
        return null;
    }

    public List<Channel> getMyChannels() {
        return myChannels;
    }

    public List<Channel> getAvailableChannels() {
        return availableChannels;
    }

    public List<Channel> getOtherChannels() {
        return otherChannels;
    }

    public void addNewChannel(String name, List<String> users) {
        Channel channel = new Channel(name, users, Application.getUserName(), false);
        otherChannels.add(channel);
    }

    public Channel receiveMessageOnChannel(Message message, String channelName) {
        Channel channel = getChannel(channelName);
        if (channel != null) {
            if (myChannels.contains(channel) && channel.isFullCache()) {
                List<Message> temp = channel.flushCache();
                Date date = new Date();
                try {
                    JSONArray ja = new JSONArray();
                    temp.stream().map(Message::toJSON).forEach(ja::put);
                    JSONObject jo = new JSONObject();
                    jo.put("messages", ja);
                    Util.eraseAndWriteInFile(channelName, date.getTime() + "", jo.toString());
                } catch (JSONException | UtilException e) {
                    e.printStackTrace();
                    Toast.makeText(Application.getContext(),
                            "Last 100 messages for " + channelName + " was lost", Toast.LENGTH_LONG).show();
                }
            }
            channel.postMessage(message);
            if(channel.getName().equalsIgnoreCase(ConversationActivity.currentChannel)){
                if(message.getSender().equalsIgnoreCase(Application.getUserName()))
                    ConversationActivity.messages.add(new MessageSentUpdate(message));
                else
                    ConversationActivity.messages.add(new MessageSentUpdate(message));
                ConversationActivity.mAdapter.notifyDataSetChanged();
            }
        }
        return channel;
    }

    public Message sendMessageOnChannel(String message, String channelName) {
        Message  msg = new Message(channelName, message, Application.getUserName());
        Channel channel = receiveMessageOnChannel(msg, channelName);
        if (channel != null) {
            sender.sendMessage(channel, message);
            return msg;
        }
        return null;

    }

    private List<Message> readMessagesFromChannel(String channelName, int packetsNumber) {
        List<Message> temp = new LinkedList<>();
        File channelsFolder = new File(Util.CHANNELS_FOLDER_PATH + File.separator + channelName);
        File[] files = channelsFolder.listFiles();
        if (files.length != 0) {
            List<File> filesList = Arrays.asList(files);
            filesList = filesList.stream().sorted((f0, f1) -> -f0.getName().compareTo(f1.getName()))
                    .limit(packetsNumber).collect(Collectors.toList());//TODO to be checked
            Collections.reverse(filesList);
            filesList.forEach(file -> {
                try {
                    String json = Util.readFile(channelName, file.getName());
                    JSONArray ja = new JSONObject(json).getJSONArray("messages");
                    for (int j = 0; j < ja.length(); j++) {
                        temp.add(new Message(ja.get(j).toString()));
                    }
                } catch (UtilException | JSONException e) {
                    e.printStackTrace();
                }

            });
        }
        return temp;
    }

    public List<Channel> readMyChannels() {
        File channelsFolder = new File(Util.CHANNELS_FOLDER_PATH);
        File[] files = channelsFolder.listFiles();
        if (files == null || files.length == 0) return new LinkedList<>();
        return Arrays.stream(files).filter(File::isDirectory).map(folder -> {
            String name = folder.getName();
            List<String> users = new LinkedList<>();
            String owner;
            try {
                JSONObject jInfos = new JSONObject(Util.readFile(name, Util.CHANNEL_INFO_FILE_NAME));
                JSONArray jUsers = jInfos.getJSONArray("users");
                for (int i = 0; i < jUsers.length(); i++)
                    users.add(jUsers.getString(i));
                owner = jInfos.getString("owner");
            } catch (UtilException | JSONException e) {
                e.printStackTrace();
                return null;
            }
            return new Channel(name, users, owner, true)
                    .initMessages(readMessagesFromChannel(name, 2));
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
