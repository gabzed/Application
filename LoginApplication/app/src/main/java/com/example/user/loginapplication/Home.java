package com.example.user.loginapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Home extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkAuth();
        makeGetRequest("http://10.0.0.202:8080/home");

        Log.d("Home", "Welcome to Home Page: " + getIntent().getStringExtra("token"));

        Toast.makeText(getApplicationContext(), "Welcome " + getIntent().getStringExtra("username"), Toast.LENGTH_LONG).show();

        buttonClick("button_tasks", "Tasks");
        buttonClick("button_profile", "UserProfile");
        buttonClick("button_logs", "Logs");
    }
}