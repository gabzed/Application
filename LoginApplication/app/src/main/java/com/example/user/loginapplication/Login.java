package com.example.user.loginapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Login extends Base {
    // Server URL
    private String serverUrl = "http://10.0.0.202:8080/api/login";

    // Token
    private String jsonToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Turn on all permissions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Save the state
        super.onCreate(savedInstanceState);

        // Load up the login page settings
        // TODO: Find out about R
        setContentView(R.layout.activity_login);

        // Number of Attempts
        TextView tx1;
        tx1 = (TextView)findViewById(R.id.textView3);
        // tx1.setVisibility(View.GONE);
        tx1.setText("3");

        // Login button with Listener
        Button yourButton = (Button) findViewById(R.id.button_login);
        yourButton.setOnClickListener(new View.OnClickListener() {
            int counter = 3;
            public void onClick(View v) {
                // Username and Password inputs
                EditText username = (EditText) findViewById(R.id.editText_username);
                EditText password = (EditText) findViewById(R.id.editText_password);
                TextView localTxt = (TextView)findViewById(R.id.textView3);
                // Password authentication
                if (serverAuthentication(username.getText().toString(), password.getText().toString())) {
                    Log.d("login", jsonToken);
                    // Go to Home activity when success
                    Intent intent = new Intent(getBaseContext(), Home.class);
                    intent.putExtra("token", jsonToken);
                    intent.putExtra("username", username.getText().toString());
                    startActivity(intent);

                } else {
                    // Decrease number of attempts
                    counter--;
                    localTxt.setText(Integer.toString(counter));
                }
                if (counter == 0) {
                    // Disable the login button on the third failed attempt
                    v.setEnabled(false);
                }
            }
        });
    }

    public boolean serverAuthentication(String username, String password) {
        try {
            //Prints to console
            Log.d("login", "Starting serverAuthentication");
            URL server = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) server.openConnection();

            //Authentication array
            byte[] basicAuth = (username+":"+password).getBytes();
            String basicAuthToken = Base64.encodeToString(basicAuth, Base64.DEFAULT);
            conn.setRequestProperty("Authorization", "basic " + basicAuthToken);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            int responseStatus = conn.getResponseCode();
            Boolean result = (responseStatus == HttpURLConnection.HTTP_OK);
            Log.d("login", "Response Code:" + responseStatus);
            if (result) {
                Log.d("login", "Getting input");
                InputStream serverResponse = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serverResponse, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d("login", line);
                    response += line;
                }
                JSONObject json = new JSONObject(response);
                result = json.has("token") && result;
                if(result){
                    jsonToken = json.getString("token");
                }
                Log.d("login", response);
                bufferedReader.close();
                serverResponse.close();
            }
            // Disconnect
            conn.disconnect();
            return result;
        } catch (MalformedURLException e) {
            Log.d("login", "MalformedURLException", e);
        } catch (IOException e) {
            Log.d("login", "IOException", e);
        } catch (JSONException e) {
            Log.d("login", "JSONException", e);
        }
        return false;
    }
}