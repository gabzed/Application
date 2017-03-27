package com.example.user.loginapplication;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

public class Logs extends Base implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        setNavDrawSettings();

        checkAuth();
        displayUsername();

        makeGetRequest("http://10.0.0.202:8080/api/log/recent/1");

//        String newObj = new String(getJSONarray("sites", getIntent().getStringExtra("responseCode")));
//        getJSONarray("name", newObj);
    }
}
