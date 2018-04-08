package fr.upmc.sluck.model;

import fr.upmc.sluck.controllers.Update;

/**
 * Created by labib on 28/03/2018.
 */

public class MessageRecievedUpdate extends Update {
    private Message msg;

    public MessageRecievedUpdate(Message msg) {
        this.msg = msg;
    }

    public Message getMsg() {
        return msg;
    }

    @Override
    public Type getType() {
        return Type.MSG_RECEIVED;
    }
}
