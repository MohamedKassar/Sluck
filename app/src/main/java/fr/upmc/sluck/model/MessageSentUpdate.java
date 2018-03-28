package fr.upmc.sluck.model;

/**
 * Created by labib on 28/03/2018.
 */

public class MessageSentUpdate extends Update {
    private Message msg;

    public MessageSentUpdate(Message msg) {
        this.msg = msg;
    }

    public Message getMsg() {
        return msg;
    }

    @Override
    public Type getType() {
        return Type.MSG_SENT;
    }
}
