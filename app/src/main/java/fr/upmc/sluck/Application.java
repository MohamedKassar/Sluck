package fr.upmc.sluck;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.network.client.Sender;
import fr.upmc.sluck.network.servers.ConnexionServer;
import fr.upmc.sluck.network.servers.LocalServer;
import fr.upmc.sluck.utils.exceptions.UtilException;

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

    public void connect(String userName, String connexionServerIp, int port) throws IOException, JSONException {
        if (!connected) {
            Application.userName = userName;
            this.sender = new Sender(userName, connexionServerIp, port);
            this.controller = new GlobalController(sender);
            this.localServer = new LocalServer(controller, sender);
            try {
                sender.connect(this.controller);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.connected = true;
            //todo
        }
    }

    public GlobalController getController() {
        return controller;
    }

    private boolean connexionServerStarted = false;

    public ConnexionServer startConnexionServer() throws IOException, UtilException {
        if (!connexionServerStarted) {
            final ConnexionServer connexionServer = new ConnexionServer(0);
            connexionServer.start();
            connexionServerStarted = true;
            return connexionServer;
        } else {
            throw new UtilException(UtilException.ExceptionType.SERVER_ALREADY_CREATED, null);
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
