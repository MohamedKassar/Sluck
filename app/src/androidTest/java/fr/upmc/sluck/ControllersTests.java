package fr.upmc.sluck;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.model.Channel;
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
        GlobalController cc = new GlobalController(null);
        cc.addNewLocalChannel("testChannel01");
    }

    @Test
    public void retrieveChannelsTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Application.setContext(appContext);

//        Util.purgeData();
        Log.v("01", "retrieveChannelsTest");

        Log.v("01",Util.CHANNELS_FOLDER_PATH);
        GlobalController cc = new GlobalController(null);
        cc.addNewLocalChannel("testChannel01");
        cc.addNewLocalChannel("testChannel02");
        cc.addNewLocalChannel("testChannel03");
        Channel c1 = cc.getChannel("testChannel01");
//        for(int i = 0; i < 200 ; i++){
//            cc.postMessageOnChannel(new Message("testChannel01","m "+ i,"zoro"), c1);
//        }
        Log.v("01", Arrays.toString(new File(Util.CHANNELS_FOLDER_PATH).listFiles()));
        Log.v("01", "LOILOI " + c1.getMessages().size());
        cc.readMyChannels().forEach(c -> Log.v("01", c.toString()));

    }
    @Test
    public void ipTest() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        System.out.println(getApIpAddr(appContext));
        Log.v("02", getApIpAddr(appContext));
    }

    public static String getApIpAddr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        byte[] ipAddress = convert2Bytes(dhcpInfo.serverAddress);
        try {
            String apIpAddr = InetAddress.getByAddress(ipAddress).getHostAddress();
            return apIpAddr;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] convert2Bytes(int hostAddress) {
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress >> 16)),
                (byte)(0xff & (hostAddress >> 24)) };
        return addressBytes;
    }
}
