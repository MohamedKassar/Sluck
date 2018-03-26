package fr.upmc.sluck.network.servers;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import fr.upmc.sluck.model.User;
import fr.upmc.sluck.network.client.Sender;

/**
 * Created by ktare on 20/03/2018.
 */

public class ConnexionServer extends Thread {
    private final int port;
    private String ipAdress;
    private final static List<User> USERS_ADDRESSES = new LinkedList<>();
    private final ServerSocket server;
    public ConnexionServer(int serverPort) throws IOException {
        setDaemon(true);
        this.port = serverPort;
        server = new ServerSocket(port);
    }



    public String getHostIp() throws UnknownHostException {
        return server.getLocalSocketAddress().toString();
    }
    public int getPort(){
        return  server.getLocalPort();
    }


    @Override
    public void run() {

        try {
            Socket client;


            while (true) {
                client = server.accept();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                final String message = bufferedReader.readLine();
               if(message.startsWith(Sender.DISCONNEXION_TOKEN)){
                    final String userName = message.split(" ")[1];
                    USERS_ADDRESSES.removeIf(user -> user.getName().equals(userName));
               }else{
                   final String userName = message;
                   final String clientIP = client.getInetAddress().getHostAddress();
                   final OutputStream outputStream = client.getOutputStream();
                   String response = port + ";";
                   response += USERS_ADDRESSES.stream().map(User::toJSON).reduce("",(user, user2) -> user +";"+user2);
                   outputStream.write(response.getBytes());
                   outputStream.flush();
                   USERS_ADDRESSES.add(new User(userName,clientIP,port));
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
}
