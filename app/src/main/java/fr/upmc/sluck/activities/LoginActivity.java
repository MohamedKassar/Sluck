package fr.upmc.sluck.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;

import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.BindView;
import fr.upmc.sluck.Application;
import fr.upmc.sluck.R;
import fr.upmc.sluck.network.servers.ConnexionServer;
import fr.upmc.sluck.utils.exceptions.UtilException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CREATECHANNEL = 0;

    @BindView(R.id.input_ip_address)
    EditText _IpAddressText;
    @BindView(R.id.input_port)
    EditText _portText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.input_username)
    EditText _username;
    @BindView(R.id.link_create_channel)
    TextView _createChannelLink;
    @BindView(R.id.address)
    TextView _address;
    private static String ip;
    private int port;
    private ConnexionServer cs;
    Application app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new Thread(() -> ip = getApIpAddr(getApplicationContext())).start();
        _loginButton.setOnClickListener(v -> login());
        app = (Application) getApplicationContext();
        _createChannelLink.setOnClickListener(v -> {
            // Start the Signup activity
            ButterKnife.bind(this);



            try {
                cs = app.startConnexionServer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UtilException e) {
                e.printStackTrace();
            }

            _address.setText("address : " + ip + ":" + cs.getPort());
            // Intent intent = new Intent(getApplicationContext(), CreateChannelActivity.class);
            //startActivityForResult(intent, REQUEST_CREATECHANNEL);
        });
    }

    public static String getApIpAddr(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);

        /*WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        byte[] ipAddress = convert2Bytes(dhcpInfo.serverAddress);
        try {
            String apIpAddr = InetAddress.getByAddress(ipAddress).getHostAddress();
            return apIpAddr;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
        return ipAddress;
    }

    private static byte[] convert2Bytes(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};
        return addressBytes;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        int i = 0;
        try {
            i = new ConnexionTask().execute(app).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (i < 0)
            return;
        Intent it = new Intent();
        it.setClass(this, ConversationActivity.class);
        startActivity(it);
    }

    private class ConnexionTask extends AsyncTask<Application, Void, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Application... applications) {
            try {
                applications[0].connect(_username.getText().toString(), _IpAddressText
                        .getText().toString(), Integer.parseInt(_portText.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == -1)
                runOnUiThread(() -> showMessage("Server error"));
            onLoginSuccess();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATECHANNEL) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        showMessage("Login failed");
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String ip = _IpAddressText.getText().toString();
        String port = _portText.getText().toString();
        String username = _username.getText().toString();

        if (ip.isEmpty()) {
            _IpAddressText.setError("username should not be empty");
            valid = false;
        } else {
            _IpAddressText.setError(null);
        }

        if (ip.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip).matches()) {
            _IpAddressText.setError("enter a valid ip address");
            valid = false;
        } else {
            _IpAddressText.setError(null);
        }

        if (port.isEmpty() || port.length() < 4 || port.length() > 10) {
            _portText.setError("port should not be empty");
            valid = false;
        } else {
            _portText.setError(null);
        }

        return valid;
    }

    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}