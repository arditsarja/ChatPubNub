package com.pubnub.example.android.datastream.pubnubdatastreams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mSharedPrefs;
    private String mUsername;
    private List<String> theChannel = new ArrayList<>();
    private List<String> subscribeChannels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mSharedPrefs == null) {
            mSharedPrefs = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
            if (!mSharedPrefs.contains(Constants.DATASTREAM_UUID)) {
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                return;
            }
        }
        this.mUsername = mSharedPrefs.getString(Constants.DATASTREAM_UUID, "");
    }

    public void conversaton(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
//        Intent intent = new Intent(this,Main2Activity.class);


        intent.putExtra("username", mUsername);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        disconnectAndCleanup();

        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }
}
