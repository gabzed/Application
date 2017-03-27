package com.example.user.loginapplication;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Base extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Displays the users username
    public void displayUsername(){
        //Gets the username from the login intent page after user has logged in
        String output = getIntent().getStringExtra("username");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView userProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_user);
        userProfileName.setText(output);
    }

    public void buttonClick(String buttonID, final String page){
        //Gets the ID of the button that was clicked
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        View addButton = findViewById(resID);
        //When button is clicked it will take user to the page
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Base", "Went to " + page);
                goToPage(page);
            }
        });
    }

    //Checks if there is a user in the session
    boolean checkAuth() {
        //If token exists, continue
        if (getIntent().hasExtra("token")) {
            Log.d("Base", "Successful login");
            Log.d("Base", "Welcome " + getIntent().getStringExtra("username"));
            return true;
        //If token doesn't exist, return to Login page
        } else {
            Log.d("Base", "UnSuccessful login");
            goToPage("Login");
            return false;
        }
    }

    //Go to the specified page
    void goToPage(String className) {
        Intent pageIntent = null;
        switch (className) {
            case "Home":
                pageIntent = new Intent(getBaseContext(), Home.class);
                break;
            case "Tasks":
                pageIntent = new Intent(getBaseContext(), Tasks.class);
                break;
            case "UserProfile":
                pageIntent = new Intent(getBaseContext(), UserProfile.class);
                break;
            case "Logs":
                pageIntent = new Intent(getBaseContext(), Logs.class);
                break;
            case "History":
                pageIntent = new Intent(getBaseContext(), History.class);
                break;
        }
        pageIntent.putExtra("token", getIntent().getStringExtra("token"));
        pageIntent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(pageIntent);
    }

    void makeGetRequest(String serverURL) {
        try {
            Log.d("Base", "Starting GET Request");
            URL obj = new URL(serverURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "bearer " + getIntent().getStringExtra("token"));
            int responseCode = con.getResponseCode();
            Log.d("Base ", "GET Response Code: " + responseCode);
            getIntent().putExtra("response", responseCode);
            // If response code is 200 successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // Print result
                Log.d("Base", "Response code: " + response.toString());
                getIntent().putExtra("responseCode", response.toString());
            } else {
                Log.d("Base", "GET request did not work");
            }
        } catch (IOException e) {
            Log.d("login", "IOException", e);
        }
    }

    void setNavDrawSettings() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onBackPressed () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_nav, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            goToPage("Home");
        } else if (id == R.id.nav_profile) {
            goToPage("UserProfile");
        } else if (id == R.id.nav_tasks) {
            goToPage("Tasks");
        } else if (id == R.id.nav_logs) {
            goToPage("Logs");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getJSONarray(String infoID, String data){
//        String data = getIntent().getStringExtra("responseCode");
        String information = null;
        try {
            Object json = new JSONTokener(data).nextValue();
            if(json instanceof JSONObject){
                JSONObject reader = new JSONObject(data);
                information = reader.getString(infoID);
            } else if(json instanceof JSONArray) {
                for(int n = 0; n < ((JSONArray) json).length(); n++) {
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject object = jsonArray.getJSONObject(n);

                    JSONObject reader = new JSONObject(String.valueOf(object));
                    information = reader.getString(infoID);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Base", information);
        return information;
    }

    public void displayInXML(String textID, String infoName){
        int resID = getResources().getIdentifier(textID, "id", getPackageName());
        String IDoutput = getJSONarray(infoName, getIntent().getStringExtra("responseCode"));
        TextView textViewID = (TextView) findViewById(resID);
        textViewID.setText(IDoutput);
    }

}