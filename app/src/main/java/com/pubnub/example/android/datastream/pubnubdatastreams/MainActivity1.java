package com.pubnub.example.android.datastream.pubnubdatastreams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity1 extends AppCompatActivity {
    private static SharedPreferences mSharedPrefs;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main123);
        if (mSharedPrefs == null) {
            mSharedPrefs = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
            if (!mSharedPrefs.contains(Constants.DATASTREAM_UUID)) {
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                return;
            }
        }
        username = mSharedPrefs.getString(Constants.DATASTREAM_UUID, "");

    }

    public void conversaton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

}
