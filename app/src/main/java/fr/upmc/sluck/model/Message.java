package fr.upmc.sluck.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by ktare on 18/03/2018.
 */

public class Message {

    private String channel;
    private String text;
    private String sender;
    private Date date;

    public Message(String json) throws JSONException {
        JSONObject o = new JSONObject(json);
        channel = o.getString("channel");
        text = o.getString("text");
        sender = o.getString("sender");
        date = new Date(o.getString("date"));
    }

    public Message(String channel, String text, String sender, Date date) {
        this.channel = channel;
        this.text = text;
        this.sender = sender;
        this.date = date;
    }

    public Message(String channel, String text, String sender) {
        this(channel, text, sender, new Date());
    }

    public String toJSON() {
        try {
            return new JSONObject().put("channel", channel).put("text", text)
                    .put("sender", sender).put("date", date.toString()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    public String getChannel() {
        return channel;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return toJSON();
    }
}
