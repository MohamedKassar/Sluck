package fr.upmc.sluck.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import fr.upmc.sluck.Application;
import fr.upmc.sluck.R;
import fr.upmc.sluck.controllers.ChannelController;
import fr.upmc.sluck.utils.exceptions.UtilException;
import fr.upmc.sluck.utils.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Application.setContext(getApplicationContext());
        try {
            System.out.println("\n\n\n" + Util.readFile("testChannel01", Util.CHANNEL_INFO_FILE_NAME) + "\n\n\n");
            System.out.println("\n\n\n" + Util.readFile("testChannel01", Util.CHANNEL_INFO_FILE_NAME) + "\n\n\n");
        } catch (UtilException e) {
            e.printStackTrace();
        }
        Util.purgeData();
        Log.v("01", "messageControllerTest");

        Log.v("01", Util.CHANNELS_FOLDER_PATH);
        Log.v("01", " louloulou " + new File(Util.CHANNELS_FOLDER_PATH).getAbsolutePath());
        ChannelController cc = new ChannelController();
        try {
            cc.addNewLocalChannel("testChannel01", new LinkedList<String>(Arrays.asList("user01", "user02", "user03")));
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }
}
