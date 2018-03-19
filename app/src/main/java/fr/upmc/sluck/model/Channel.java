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
    private List<Message> cache;
    private List<Message> allMessages;

    public Channel(String name, List<String> users, String owner, boolean local) {
        this.name = name;
        this.users = users;
        this.owner = owner;
        this.allMessages = new LinkedList<>();
        if (local)
            this.cache = new ArrayList<>();
    }

    public Channel initMessages(List<Message> messages){
        this.allMessages.addAll(messages);
        return this;
    }

    public void postMessage(Message message) {
        allMessages.add(message);
        if (cache != null)
            cache.add(message);
    }

    public void putUser(String user) {
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
        return allMessages;
    }

    public boolean isFullCache() {
        return cache != null && cache.size() == 150;
    }

    public List<Message> flushCache() {
        if (cache == null) throw new RuntimeException("Should not appear");
        List<Message> temp = cache;
        cache = new LinkedList<>();
        return temp;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Channel ? ((Channel) obj).name.equals(name) : false;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", users=" + users +
                ", owner='" + owner + '\'' +
                ", messages=" + allMessages +
                '}';
    }
}
