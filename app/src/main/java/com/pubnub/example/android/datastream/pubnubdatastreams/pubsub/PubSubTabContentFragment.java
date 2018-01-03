package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.example.android.datastream.pubnubdatastreams.MainActivity;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.CameraPhoto;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class PubSubTabContentFragment extends AppCompatActivity {
    private final String TAG = PubSubTabContentFragment.class.getName();
    private PubSubListAdapter psAdapter;
    private PubNub mPubnub_DataStream;
    private String channel;
    private String mUsername;
    private ImageButton btnCamera;
    CameraPhoto cameraPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pubsub);

        psAdapter = PostVariables.mPubSub;
        mUsername = PostVariables.mUsername;
        mPubnub_DataStream = PostVariables.mPubnub_DataStream;
        channel = PostVariables.channel;
        ListView listView = (ListView) findViewById(R.id.message_list);
        listView.setAdapter(psAdapter);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        getPremissions();
        ImageView imageView = findViewById(R.id.circleImage);
        TextView textView = findViewById(R.id.contactName);
        textView.setText(channel);
        Picasso.with(getApplicationContext())
                .load("http://i.imgur.com/DvpvklR.png")
                .resize(100,100)
                .transform(new CircleTransform())
                .into(imageView);
    }

    private void getPremissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        0);
            }
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        sendMessage(cameraPhoto.getData());

    }

    public void openCamera(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(intent, 0);
//
//
//        } catch (Exception e) {
//            Log.v("Message", e.getMessage());
//        }
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

    private void sendMessage(ArrayList data) {
        final EditText mMessage = (EditText) this.findViewById(R.id.new_message);
        Map<String, Object> message = null;

        if (data == null)
            message = ImmutableMap.<String, Object>of("sender", this.mUsername, "message", mMessage.getText().toString(), "timestamp", DateTimeUtil.getTimeStampUtc());
        else
            message = ImmutableMap.<String, Object>of("sender", this.mUsername, "message", data, "timestamp", DateTimeUtil.getTimeStampUtc());

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
                            Log.v("the errrrror", e.getMessage());
                        }
                    }
                }
        );
    }

    public void chatList(View view) {
        Intent intent = new Intent(this, MainActivity.class);
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
