package com.example.user.loginapplication;

import android.os.Bundle;
import android.util.Log;

public class UserProfile extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setNavDrawSettings();

        checkAuth();
        displayUsername();
        makeGetRequest("http://10.0.0.202:8080/api/profile/me");

        Log.d("User Profile", getIntent().getStringExtra("responseCode"));

        displayInXML("textView_user1", "username");
        displayInXML("textView_userID1", "id");

        buttonClick("button_history", "History");

    }
}
