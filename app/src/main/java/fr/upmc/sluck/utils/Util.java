package fr.upmc.sluck.utils;

import android.util.Log;

import java.io.File;

import fr.upmc.sluck.Application;

/**
 * Created by ktare on 18/03/2018.
 */

public class Util {

    private static final String APP_FOLDER = "Sluck";
    static {
        createFolder_bis(APP_FOLDER);
    }

    public static void createFolder(String name) {
        createFolder_bis(APP_FOLDER + File.separator + name);
    }

    private static void createFolder_bis(String name) {
        File folder = new File(Application.getContext().getFilesDir() + File.separator + name);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            Log.v("01", name + " exists");
        }
        if (success) {
            Log.v("01", name + " folder created");
        } else {
            //TODO do somthing
            Log.v("01", name + " folder creation error");
        }
    }
}
