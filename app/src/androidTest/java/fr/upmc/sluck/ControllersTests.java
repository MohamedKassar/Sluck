package fr.upmc.sluck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.upmc.sluck.controllers.ChannelMessagesController;
import fr.upmc.sluck.model.Message;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ControllersTests {

    @Test
    public void messageControllerTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Application.setContext(appContext);
        Log.v("01", "messageControllerTest");
        ChannelMessagesController c = new ChannelMessagesController("channel01", "tiko");
    }
}
