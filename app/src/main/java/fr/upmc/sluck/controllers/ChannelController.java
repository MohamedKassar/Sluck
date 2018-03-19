package fr.upmc.sluck.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        //fetch this user channels TODO
        //channels.add
        //
    }


    public void addNewChannel(String name, List<String> users, String owner) throws UtilException {
        Util.createChannelFolder(name, users, owner);
        Channel channel = new Channel(name, users, owner);
        myChannels.add(channel);
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
            return new Channel(name, users, owner);
        }).filter(channel -> channel != null).collect(Collectors.toList());
    }
}
