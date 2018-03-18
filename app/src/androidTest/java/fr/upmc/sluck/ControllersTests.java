package fr.upmc.sluck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import fr.upmc.sluck.controllers.ChannelController;
import fr.upmc.sluck.utils.Util;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ControllersTests {

    @Test
    public void messageControllerTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Application.setContext(appContext);
        Log.v("01", "messageControllerTest");

        Log.v("01",Util.APP_FOLDER_PATH);
        ChannelController cc = new ChannelController();
        cc.addNewChannel("testChannel01", new LinkedList<String>(Arrays.asList("user01", "user02", "user03")), "Zoro");
    }
}
