package fr.upmc.sluck.activities;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import butterknife.ButterKnife;
import butterknife.BindView;
import fr.upmc.sluck.Application;
import fr.upmc.sluck.R;
import fr.upmc.sluck.network.servers.ConnexionServer;
import fr.upmc.sluck.utils.exceptions.UtilException;

public class CreateChannelActivity extends AppCompatActivity {
    public static final int MAX_PORT_NUMBER = 49151;
    public static final int MIN_PORT_NUMBER = 1100;
    private static final String TAG = "CreateChannel";
    private String ipAdress;

    private ConnexionServer cs;


    @BindView(R.id.btn_createChannel) Button _createChannel;
    @BindView(R.id.link_login) TextView _loginLink;
    @BindView(R.id.ip_address) TextView _ipAdress;
    @BindView(R.id.port) TextView _port;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createchannel);
        ButterKnife.bind(this);
        Application app = (Application) getApplicationContext();

        new Thread(()->{
            String ip =getApIpAddr(getApplicationContext());
            runOnUiThread(()-> _ipAdress.setText(ip));
        }).start();
        try {
          cs = app.startConnexionServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UtilException e) {
            e.printStackTrace();
        }

        _port.setText(cs.getPort()+"");
        _createChannel.setOnClickListener(v -> createChannel());

        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }

    public void createChannel() {
        Log.d(TAG, "Create hannel");


        _createChannel.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateChannelActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Channel...");
        progressDialog.show();




        //TODO : generate port and find ip adress

        new android.os.Handler().postDelayed(
                () -> {
                    // On complete call either onCreateChannelSuccess or onCreateChannelFailed
                    // depending on success
                    onCreateChannelSuccess();
                    // onCreateChannelFailed();
                    progressDialog.dismiss();
                }, 3000);
    }


    public void onCreateChannelSuccess() {
        _createChannel.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
    }

    public void onCreateChannelFailed() {
        Toast.makeText(getBaseContext(), "creation failed", Toast.LENGTH_LONG).show();
        _createChannel.setEnabled(true);
    }


    public static boolean isPortAvailable(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }

    public int generatePort(){
        int port=MIN_PORT_NUMBER;
        while (port <= MAX_PORT_NUMBER  && !isPortAvailable(port) )
            ++port;
        if(port > MAX_PORT_NUMBER)
            return -1;
        return port;
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