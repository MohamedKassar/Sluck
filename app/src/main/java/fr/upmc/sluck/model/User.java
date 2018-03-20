package fr.upmc.sluck.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by ktare on 20/03/2018.
 */

public class User {
    private final String name;
    private final String ip;
    private final int port;

    public User(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public User(String json) throws JSONException {
        JSONObject o = new JSONObject(json);
        name = o.getString("name");
        ip = o.getString("ip");
        port = o.getInt("port");
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String toJSON() {
        try {
            return new JSONObject().put("name", name).put("ip", ip)
                    .put("port", port).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

}
