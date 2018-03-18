package fr.upmc.sluck.controllers;

import java.util.LinkedList;
import java.util.List;

import fr.upmc.sluck.utils.exceptions.UtilException;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.utils.Util;

/**
 * Created by ktare on 18/03/2018.
 */

public class ChannelController {
    public List<Channel> channels;

    public ChannelController() {
        channels = new LinkedList<>();
        //fetch this user channels TODO
        //channels.add
        //
    }


    public void addNewChannel(String name, List<String> users, String owner) throws UtilException {
        Util.createChannelFolder(name, users, owner);
        Channel channel = new Channel(name, users, owner);
        channels.add(channel);
    }
}
