package com.example.user.loginapplication;

import android.os.Bundle;

public class Tasks extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        setNavDrawSettings();

        checkAuth();
        displayUsername();
        makeGetRequest("http://10.0.0.202:8080/api/log/recent");

    }
}