package fr.upmc.sluck;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.network.client.Sender;
import fr.upmc.sluck.network.servers.LocalServer;

/**
 * Created by ktare on 18/03/2018.
 */

public class Application extends android.app.Application {
    private static Context context;
    private static String userName;
    private boolean connected;

    public static void setUserName(String userName) {
        Application.userName = userName;
    }

    public void onCreate() {
        super.onCreate();
        userName = "user";//TODO
        Application.context = getApplicationContext(); // TODO put

    }

    private Sender sender;
    private LocalServer localServer;
    private GlobalController controller;

    public void connect(String userName, String connexionServerIp, int port) {
        if (!connected) {
            try {
                this.sender = new Sender(userName, connexionServerIp, port);
                this.controller = new GlobalController(sender);
                this.localServer = new LocalServer(controller, sender);
                //todo
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public static void setContext(Context context) { //TODO remove
        Application.context = context;
    }

    public static String getUserName() {
        return userName;
    }

    public static Context getContext() {
        return Application.context;
    }
}
