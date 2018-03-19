package fr.upmc.sluck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import fr.upmc.sluck.controllers.ChannelController;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.model.Message;
import fr.upmc.sluck.utils.Util;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ControllersTests {

    @Test
    public void addLocalChannelTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Application.setContext(appContext);
        Log.v("01", "addLocalChannelTest");

        Log.v("01",Util.CHANNELS_FOLDER_PATH);
        ChannelController cc = new ChannelController();
        cc.addNewLocalChannel("testChannel01", new LinkedList<String>(Arrays.asList("user01", "user02", "user03")));
    }

    @Test
    public void retrieveChannelsTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Application.setContext(appContext);

//        Util.purgeData();
        Log.v("01", "retrieveChannelsTest");

        Log.v("01",Util.CHANNELS_FOLDER_PATH);
        ChannelController cc = new ChannelController();
        cc.addNewLocalChannel("testChannel01", new LinkedList<String>(Arrays.asList("user01", "user02", "user03")));
        cc.addNewLocalChannel("testChannel02", new LinkedList<String>(Arrays.asList("user04", "user05", "user06")));
        cc.addNewLocalChannel("testChannel03", new LinkedList<String>(Arrays.asList("user07", "user08", "user09")));
        Channel c1 = cc.getChannel("testChannel01");
//        for(int i = 0; i < 200 ; i++){
//            cc.postMessageOnChannel(new Message("testChannel01","m "+ i,"zoro"), c1);
//        }
        Log.v("01", Arrays.toString(new File(Util.CHANNELS_FOLDER_PATH).listFiles()));
        Log.v("01", "LOILOI " + c1.getMessages().size());
        cc.readMyChannels().forEach(c -> Log.v("01", c.toString()));

    }
}
