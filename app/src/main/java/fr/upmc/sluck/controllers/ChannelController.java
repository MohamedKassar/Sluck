package fr.upmc.sluck.controllers;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fr.upmc.sluck.Application;
import fr.upmc.sluck.model.Message;
import fr.upmc.sluck.utils.exceptions.UtilException;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.utils.Util;

/**
 * Created by ktare on 18/03/2018.
 */

public class ChannelController {
    public List<Channel> myChannels, otherChannels;

    public ChannelController() {
        myChannels = new LinkedList<>();
        otherChannels = new LinkedList<>();
        myChannels.addAll(readMyChannels());
    }

    public void addNewLocalChannel(String name, List<String> users) throws UtilException {
        Util.createChannelFolder(name, users, Application.getUserName());
        Channel channel = new Channel(name, users, Application.getUserName(), true);
        myChannels.add(channel);
    }

    public Channel getChannel(String channelName) {
        for (Channel c : myChannels) {
            if(c.getName().equals(channelName))return c;
        }
        for (Channel c : otherChannels) {
            if(c.getName().equals(channelName))return c;
        }
        return null;
    }

    public void addNewChannel(String name, List<String> users) {
        Channel channel = new Channel(name, users, Application.getUserName(), false);
        otherChannels.add(channel);
    }

    public void postMessageOnChannel(Message message, Channel channel) {
        if (myChannels.contains(channel) && channel.isFullCache()) {
            List<Message> temp = channel.flushCache();
            Date date = new Date();
            try {
                JSONArray ja = new JSONArray();
                temp.stream().map(Message::toJSON).forEach(ja::put);
                JSONObject jo = new JSONObject();
                jo.put("messages", ja);
                Util.eraseAndWriteInFile(channel.getName(), date.getTime() + "", jo.toString());
            } catch (JSONException | UtilException e) {
                e.printStackTrace();
                Toast.makeText(Application.getContext(),
                        "Last 100 messages for " + channel.getName() + " was lost", Toast.LENGTH_LONG);
            }
        }
        channel.postMessage(message);
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
        if (files == null || files.length == 0) return Collections.EMPTY_LIST;
        return Arrays.asList(files).stream().filter(File::isDirectory).map(folder -> {
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
        }).filter(channel -> channel != null).collect(Collectors.toList());
    }
}
