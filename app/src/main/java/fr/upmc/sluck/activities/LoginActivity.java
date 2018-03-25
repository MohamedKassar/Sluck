package fr.upmc.sluck.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import fr.upmc.sluck.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CREATECHANNEL = 0;

    @BindView(R.id.input_ip_address) EditText _IpAddressText;
    @BindView(R.id.input_port) EditText _portText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_create_channel) TextView _createChannelLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(v -> login());

        _createChannelLink.setOnClickListener(v -> {
            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), CreateChannelActivity.class);
            startActivityForResult(intent, REQUEST_CREATECHANNEL);
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO : connect to channel

        new android.os.Handler().postDelayed(
                () -> {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                }, 3000);
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
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String ip = _IpAddressText.getText().toString();
        String port = _portText.getText().toString();

        if (ip.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip).matches()) {
            _IpAddressText.setError("enter a valid ip address");
            valid = false;
        } else {
            _IpAddressText.setError(null);
        }

        if (port.isEmpty() || port.length() < 4 || port.length() > 10) {
            _portText.setError("Port should not be empty");
            valid = false;
        } else {
            _portText.setError(null);
        }

        return valid;
    }
}