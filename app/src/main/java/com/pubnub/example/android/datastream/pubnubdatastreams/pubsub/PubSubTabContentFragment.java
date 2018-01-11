package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.example.android.datastream.pubnubdatastreams.MainActivity;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.CameraPhoto;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PubSubTabContentFragment extends AppCompatActivity {
    private final String TAG = PubSubTabContentFragment.class.getName();
    private PubSubListAdapter psAdapter;
    private PubNub mPubnub_DataStream;
    private String mUsername;
    CameraPhoto cameraPhoto;
    private Person person;
    private long start;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private History history;
    private final int GET_HISTORY_DATA = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pubsub);
        psAdapter = PostVariables.mPubSub;
        mUsername = PostVariables.mUsername;
        mPubnub_DataStream = PostVariables.mPubnub_DataStream;
        person = PostVariables.person;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ListView listView = (ListView) findViewById(R.id.message_list);
        listView.setAdapter(psAdapter);
        getHistory();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });

        cameraPhoto = new CameraPhoto(getApplicationContext());
        ImageView imageView = findViewById(R.id.circleImage);
        TextView textView = findViewById(R.id.contactName);
        textView.setText(person.name);
        Picasso.with(getApplicationContext())
                .load(person.image)
                .resize(100, 100)
                .transform(new CircleTransform())
                .into(imageView);
//        getHistory();

    }

    private void getHistory() {
        History history = mPubnub_DataStream.history();
        if (start != 0)
            history.start(start);
        history.channel(person.channel)
                .count(GET_HISTORY_DATA)
                .includeTimetoken(true)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (!status.isError()) {

                            for (int i = result.getMessages().size() - 1; i >= 0; i--) {
                                PNHistoryItemResult itemResult = result.getMessages().get(i);
                                try {
                                    PubSubPojo msg = JsonUtil.convert(itemResult.getEntry(), PubSubPojo.class);
                                    psAdapter.addFormServer(msg);
                                    if (i == 0)
                                        start = result.getMessages().get(i).getTimetoken();
                                    Log.v("History of Channel", itemResult.getEntry().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                });
        mSwipeRefreshLayout.setRefreshing(false);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendMessage(cameraPhoto.getData());
    }

    public void openCamera(View view) {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), 0);
        } catch (Exception e) {
            Log.v("Message error", e.getMessage());
        }
        cameraPhoto.addToGallery();
    }

    public void publish(View view) {
        sendMessage(null);
    }

    private void sendMessage(ArrayList<String> data) {
        final EditText mMessage = (EditText) this.findViewById(R.id.new_message);
        Map<String, String> message =new HashMap<String,String>();
        message.put("id","id");
        message.put("uniqueId","uniqueId");
        message.put("message",data!=null?data.get(1):mMessage.getText().toString());
        message.put("messageType",data!=null?data.get(0):"string");
        message.put("sender",mUsername);
        message.put("channel",person.channel);
        message.put("sendDate",DateTimeUtil.getTimeStampUtc());
        message.put("receiveDate","receiveDate");
//        MainActivity.this.mPubnub_DataStream.publish().channel(Constants.CHANNEL_NAME).message(message).async(
        mPubnub_DataStream.publish().channel(person.channel).message(message).async(
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
                            Log.v("the errrrror", e.getMessage());
                        }
                    }
                }
        );
    }

    public void chatList(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PostVariables.person=null;
        startActivity(intent);
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
