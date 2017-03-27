package com.example.user.loginapplication;

import android.os.Bundle;
import android.util.Log;

public class History extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setNavDrawSettings();

        checkAuth();
        displayUsername();
        makeGetRequest("http://10.0.0.202:8080/api/profile/history");

        Log.d("History", getIntent().getStringExtra("responseCode"));

        getJSONarray("log", getIntent().getStringExtra("responseCode"));

        displayInXML("textView_log1", "log");
        displayInXML("textView_timestamp1", "created_at");

    }
}
