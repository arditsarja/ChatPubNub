package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.ImmutableMap;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.CameraPhoto;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.ImageSaver;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    }

    private void getPremissions() {
        if( ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        0);
            }
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");



        try {

            sendMessage(saveToInternalStorage(bitmap));

        } catch (Exception e) {
            Log.v("Message", e.getMessage());
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        // Create imageDir
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        new ImageSaver(this).
                setFileName(imageFileName).
                setDirectoryName("images").
                save(bitmapImage);
        return "isphoto"+imageFileName;
        // Create imageDir
//        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

//        File mypath=new File(directory,imageFileName);
//         mypath=new File(directory,imageFileName);
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return "isphoto"+directory.getAbsolutePath();

    }
    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, 0);


        } catch (Exception e) {
            Log.v("Message", e.getMessage());
        }
//        try {
//            startActivityForResult(cameraPhoto.takePhotoIntent(), 0);
//            cameraPhoto.addToGallery();
//        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(),
//                    "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
//        }
    }

    public void publish(View view) {
        sendMessage(null);
    }

    private void sendMessage(String bitmap) {
        final EditText mMessage = (EditText) this.findViewById(R.id.new_message);
        Map<String, Object> message = null;

        if (bitmap == null)
            message = ImmutableMap.<String, Object>of("sender", this.mUsername, "message", mMessage.getText().toString(), "timestamp", DateTimeUtil.getTimeStampUtc());
        else
            message = ImmutableMap.<String, Object>of("sender", this.mUsername, "message", bitmap, "timestamp", DateTimeUtil.getTimeStampUtc());

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
