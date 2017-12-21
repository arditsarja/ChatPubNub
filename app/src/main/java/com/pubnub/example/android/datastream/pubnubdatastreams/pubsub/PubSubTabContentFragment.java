package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.collect.ImmutableMap;
import com.pubnub.api.PubNub;
import com.pubnub.api.Pubnub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.example.android.datastream.pubnubdatastreams.MainActivity;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;

import java.io.Serializable;
import java.util.Map;

public class PubSubTabContentFragment extends AppCompatActivity {
    private final String TAG = PubSubTabContentFragment.class.getName();
    private PubSubListAdapter psAdapter;
    private PubNub mPubnub_DataStream;
    private String channel;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pubsub);


//        return view;
        psAdapter = PostVariables.mPubSub;
        mUsername = PostVariables.mUsername;
        mPubnub_DataStream = PostVariables.mPubnub_DataStream;
        channel = PostVariables.channel;
        ListView listView = (ListView) findViewById(R.id.message_list);
        listView.setAdapter(psAdapter);
    }

    public void publish(View view) {
        final EditText mMessage = (EditText) this.findViewById(R.id.new_message);

        final Map<String, String> message = ImmutableMap.<String, String>of("sender", this.mUsername, "message", mMessage.getText().toString(), "timestamp", DateTimeUtil.getTimeStampUtc());

//        MainActivity.this.mPubnub_DataStream.publish().channel(Constants.CHANNEL_NAME).message(message).async(
        mPubnub_DataStream.publish().channel(channel).message(message).async(

                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (!status.isError()) {
                                mMessage.setText("");
                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                            } else {
                                Log.v(TAG, "publishErr(" + JsonUtil.asJson(status) + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_pubsub, container, false);
//        ListView listView = (ListView) view.findViewById(R.id.message_list);
//        listView.setAdapter(psAdapter);
//
//        return view;
//    }

    public void setAdapter(PubSubListAdapter psAdapter) {
        this.psAdapter = psAdapter;
    }
}
