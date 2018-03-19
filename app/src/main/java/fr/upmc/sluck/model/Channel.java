package fr.upmc.sluck.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ktare on 18/03/2018.
 */

public class Channel {
    private String name;
    private List<String> users;
    private String owner;
    private List<Message> messages;

    public Channel(String name, List<String> users, String owner) {
        this.name = name;
        this.users = users;
        this.owner = owner;
        this.messages = new LinkedList<>();
    }

    public void putMessage(Message message) {
        messages.add(message);
    }

    public void putUser(String user){
        users.add(user);
    }
    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return users;
    }

    public String getOwner() {
        return owner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", users=" + users +
                ", owner='" + owner + '\'' +
                ", messages=" + messages +
                '}';
    }
}
