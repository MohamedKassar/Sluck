package fr.upmc.sluck.model;

import java.util.Enumeration;

/**
 * Created by labib on 28/03/2018.
 */

public abstract class Update {
    public enum Type{MSG_RECEIVED(1),MSG_SENT(2),USER_ENTERED(3),USER_LEFT(4);
        int value;
        Type(int v) {
            value=v;
        }

        public int getValue() {
            return value;
        }

    }
    public abstract Type getType();
}
