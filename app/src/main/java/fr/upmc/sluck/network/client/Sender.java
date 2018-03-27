package fr.upmc.sluck.network.client;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fr.upmc.sluck.Application;
import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.model.User;
import fr.upmc.sluck.network.servers.LocalServer;

/**
 * Created by ktare on 20/03/2018.
 */

public class Sender {

    public final static String NEW_CHANNEL_TOKEN = "NCHANNEL";
    public final static String DISCONNEXION_TOKEN = "DISCONNECT";
    public final static String CONNEXION_TOKEN = "CONNECT";
    public final static String MESSAGE_TOKEN = "MESSAGE";
    public final static String JOIN_CHANNEL_TOKEN = "JOIN";
    public final static String NOTIFY_NEW_MEMBER_TOKEN = "NOTIMEM";
    public final static String SIGNAL_CHANNEL_TOKEN = "CHANNELS";

    private final static List<User> USERS_ADDRESSES = new LinkedList<>();

    private final String connexionServerIp;
    private final int connexionServerPort;

    public Sender(String userName, String connexionServerIp, int connexionServerPort) {
        this.connexionServerIp = connexionServerIp;
        this.connexionServerPort = connexionServerPort;
        //USERS_ADDRESSES.forEach(user -> send(user,));
    }


    public void connect(GlobalController gc) throws IOException, JSONException {
        final Socket socket = send(connexionServerIp, connexionServerPort, Application.getUserName());
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final String[] response = bufferedReader.readLine().split(";");
        LocalServer.setPort(Integer.parseInt(response[0]));
        for (int i = 1; i < response.length; i++) {
            if (!response[i].isEmpty())
                USERS_ADDRESSES.add(new User(response[i]));
        }
        USERS_ADDRESSES.forEach(user -> send(user, CONNEXION_TOKEN + " " + Application.getUserName() + " " + connexionServerPort));
        signalMyChannels(gc);
    }

    public void signalMyChannels(GlobalController gc) {
        USERS_ADDRESSES.forEach(user -> gc.getMyChannels().stream()
                .map(ch -> Application.getUserName() + " " + ch.getName() + " "
                        + (ch.getUsers().contains(user.getName()) ? "true" + " "
                        + ch.getUsers().stream().reduce("", (a, b) -> a + " " + b) : "false"))
                .forEach(ch -> send(user, SIGNAL_CHANNEL_TOKEN + " " + ch)));
    }

    public void sendMessage(Channel channel, String message) {
        List<User> target = USERS_ADDRESSES.stream().filter(user -> channel.getUsers()
                .contains(user.getName())).collect(Collectors.toList());
        target.forEach(user -> send(user, MESSAGE_TOKEN + " " + channel.getName() + " " + Application.getUserName() + " " + message));
    }

    public void notifyNewUser(Channel channel, String userName) {
        List<User> target = USERS_ADDRESSES.stream().filter(user -> channel.getUsers()
                .contains(user.getName())).collect(Collectors.toList());
        target.forEach(user -> send(user, NOTIFY_NEW_MEMBER_TOKEN + " " + channel.getName() + " " + userName));
    }

    public void notifyChannelCreation(String channelName) {
        USERS_ADDRESSES.forEach(user -> send(user, NEW_CHANNEL_TOKEN + " " + channelName + " " + Application.getUserName()));
    }

    public void joinChannel(String channelName, String owner) {
        User user = USERS_ADDRESSES.stream().filter(user1 -> user1.getName().equals(owner)).findAny().orElse(null);
        if (user != null)
            send(user, JOIN_CHANNEL_TOKEN + " " + channelName + " " + Application.getUserName());
    }

    public void notifyDisconnexion() {
        final String message = DISCONNEXION_TOKEN + " " + Application.getUserName();
        USERS_ADDRESSES.forEach(user -> send(user, message));
        try {
            send(connexionServerIp, connexionServerPort, message);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("01", "Cannot send message", e);
        }
    }

    public static void removeUser(String userName) {
        USERS_ADDRESSES.removeIf(user -> user.getName().equals(userName));
    }

    public static void addUser(String userName, String userIp, int userPort) {
        USERS_ADDRESSES.add(new User(userName, userIp, userPort));
    }

    private Socket send(String ip, int port, String message) throws IOException {
        Socket socket = new Socket(ip, port);
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes());
        outputStream.flush();
        return socket;
    }

    private void send(User user, String message) {
        try {
            send(user.getIp(), user.getPort(), message);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("01", "Cannot send message", e);
        }
    }


}
