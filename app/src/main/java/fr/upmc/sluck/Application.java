package fr.upmc.sluck;

import android.content.Context;

/**
 * Created by ktare on 18/03/2018.
 */

public class Application extends android.app.Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        //Application.context = getApplicationContext(); // TODO put
    }

    public static void setContext(Context context) { //TODO remove
        Application.context = context;
    }

    public static Context getContext() {
        return Application.context;
    }
}
