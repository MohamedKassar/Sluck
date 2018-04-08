package fr.upmc.sluck.model;

import fr.upmc.sluck.controllers.Update;

/**
 * Created by labib on 28/03/2018.
 */

public class UserStatusUpdate extends Update {
    private User user;
    private Type type;
    public UserStatusUpdate(User user, Type type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Type getType() {
        return type;
    }
}
