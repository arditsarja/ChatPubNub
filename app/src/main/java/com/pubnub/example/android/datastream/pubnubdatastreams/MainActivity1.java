package com.pubnub.example.android.datastream.pubnubdatastreams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pubnub.api.PubNub;
import com.pubnub.example.android.datastream.pubnubdatastreams.multi.MultiListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.multi.MultiPnCallback;
import com.pubnub.example.android.datastream.pubnubdatastreams.presence.PresenceListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.presence.PresencePnCallback;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubPnCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity1 extends AppCompatActivity {
    private static SharedPreferences mSharedPrefs;
    public static String mUsername;
    public static final List<String> MULTI_CHANNELS = Arrays.asList(Constants.MULTI_CHANNEL_NAMES.split(","));
    //    public static final List<String> PUBSUB_CHANNEL = Arrays.asList(Constants.CHANNEL_NAME);
    public static final List<String> PUBSUB_CHANNEL = Arrays.asList(Constants.CHANNEL_NAME.split(","));

    public static ScheduledExecutorService mScheduleTaskExecutor;
    public static List<String> theChannel = new ArrayList<>();
    public static PubNub mPubnub_DataStream;
    public static PubSubListAdapter mPubSub;
    public static PubSubPnCallback mPubSubPnCallback;

    public static PresenceListAdapter mPresence;
    public static PresencePnCallback mPresencePnCallback;

    public static PubNub mPubnub_Multi;
    public static MultiListAdapter mMulti;
    public static MultiPnCallback mMultiPnCallback;

    public static Random random = new Random();
    public static String channel;

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
        mUsername = mSharedPrefs.getString(Constants.DATASTREAM_UUID, "");
    }

    private void disconnectAndCleanup() {
        getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE).edit().clear().commit();

        if (this.mPubnub_DataStream != null) {
            this.mPubnub_DataStream.unsubscribe().channels(PUBSUB_CHANNEL).execute();
            this.mPubnub_DataStream.removeListener(this.mPubSubPnCallback);
            this.mPubnub_DataStream.removeListener(this.mPresencePnCallback);
            this.mPubnub_DataStream.stop();
            this.mPubnub_DataStream = null;
        }

        if (this.mPubnub_Multi != null) {
            this.mPubnub_Multi.unsubscribe().channels(MULTI_CHANNELS).execute();
            this.mPubnub_Multi.removeListener(this.mMultiPnCallback);
            this.mPubnub_Multi.stop();
            this.mPubnub_Multi = null;
        }

        if (this.mScheduleTaskExecutor != null) {
            this.mScheduleTaskExecutor.shutdownNow();
            this.mScheduleTaskExecutor = null;
        }
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectAndCleanup();
    }

    public void conversaton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("username", username);
        startActivity(intent);
    }

}
