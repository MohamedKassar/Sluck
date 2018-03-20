package fr.upmc.sluck.network.servers;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ktare on 20/03/2018.
 */

public class ConnexionServer extends Thread {
    private final int BASE_PORT;
    private int freePort;

    public ConnexionServer(int serverPort) {
        setDaemon(true);
        this.BASE_PORT = serverPort;
        this.freePort = serverPort + 1;

    }

    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(BASE_PORT);
            Socket client;
            while (true) {
                client = server.accept();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                final String userName = bufferedReader.readLine();
                final String clientIP = client.getInetAddress().getHostAddress();
                final OutputStream outputStream = client.getOutputStream();
                outputStream.write(((freePort++) +"").getBytes());
                outputStream.flush();
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
