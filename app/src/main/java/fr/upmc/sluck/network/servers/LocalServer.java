package fr.upmc.sluck.network.servers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.model.Message;
import fr.upmc.sluck.network.client.Sender;

/**
 * Created by ktare on 20/03/2018.
 */

public class LocalServer extends Thread {
    private static int port;
    private final GlobalController globalController;
    private final Sender sender;

    public LocalServer(GlobalController globalController, Sender sender) {
        setDaemon(true);
        this.globalController = globalController;
        this.sender = sender;
    }


    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            Socket client;
            while (true) {
                client = server.accept();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                final String[] request = bufferedReader.readLine().split(" ");
                switch (request[0]) {
                    case Sender.DISCONNEXION_TOKEN:
                        Sender.removeUser(request[1]);
                        break;
                    case Sender.CONNEXION_TOKEN:
                        Sender.addUser(request[1], client.getInetAddress().getHostAddress(), Integer.parseInt(request[2]));
                        this.sender.signalMyChannels(globalController);
                        break;
                    case Sender.JOIN_CHANNEL_TOKEN:
                        Channel temp = globalController.addNewUser(request[1], request[2]);
                        if (temp != null) {
                            sender.notifyNewUser(temp, request[2]);
                        }
                        break;
                    case Sender.MESSAGE_TOKEN:
                        globalController.receiveMessageOnChannel(new Message(request[1], request[3], request[2]), request[1]);
                        break;
                    case Sender.NEW_CHANNEL_TOKEN:
                        globalController.addAvailableChannel(request[1], request[2]);
                        break;

                    case Sender.NOTIFY_NEW_MEMBER_TOKEN:
                        globalController.addNewUser(request[1], request[2]);
                        break;
                    case Sender.SIGNAL_CHANNEL_TOKEN:
                        if (request[3].contains("false")) {
                            globalController.addAvailableChannel(request[2], request[1]);
                        } else {
                            List<String> users = new ArrayList<>();
                            for (int i = 4; i < request.length; i++) {
                                users.add(request[i]);
                            }
                            globalController.addNewChannel(request[2], users);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("01", "Problem while creating the connexion server socket.", e);
        } finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("01", "Problem while closing the connexion server socket.", e);
                }
        }
    }

    public static void setPort(int port) {
        LocalServer.port = port;
    }
}
