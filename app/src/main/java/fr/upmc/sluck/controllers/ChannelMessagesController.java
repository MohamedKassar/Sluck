package fr.upmc.sluck.controllers;

import fr.upmc.sluck.utils.Util;

/**
 * Created by ktare on 18/03/2018.
 */

public class ChannelMessagesController {
    private String channel;
    private String userId;

    public ChannelMessagesController(String channel, String userId) {
        this.channel = channel;
        this.userId = userId;

        //channel folder creation

        Util.createFolder(channel);
    }



}
