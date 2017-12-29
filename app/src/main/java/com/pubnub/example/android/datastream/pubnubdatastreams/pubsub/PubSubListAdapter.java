package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosalgeek.android.photoutil.ImageLoader;
import com.pubnub.example.android.datastream.pubnubdatastreams.MainActivity1;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.ImageSaver;

import java.util.ArrayList;
import java.util.List;

public class PubSubListAdapter extends ArrayAdapter<PubSubPojo> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<PubSubPojo> values = new ArrayList<PubSubPojo>();

    public PubSubListAdapter(Context context) {
        super(context, R.layout.list_row_pubsub_recive);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void add(PubSubPojo message) {
//        this.values.add(0, message);
        this.values.add(message);

        ((Activity) this.context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PubSubPojo dsMsg = this.values.get(position);
        PubSubListRowUi msgView;
        String newLine = "";
        if (dsMsg.getSender().equals(MainActivity1.mUsername))
            convertView = inflater.inflate(R.layout.list_row_pubsub_sender, parent, false);
        else {
            convertView = inflater.inflate(R.layout.list_row_pubsub_recive, parent, false);
            newLine = "\n";
        }

//        if (convertView == null) {
        msgView = new PubSubListRowUi();
        msgView.sender = (TextView) convertView.findViewById(R.id.sender);
//        if (dsMsg.getMessage() instanceof Bitmap && dsMsg.getSender().equals(MainActivity1.mUsername)) {
            msgView.image = (ImageView) convertView.findViewById(R.id.imageView);

            msgView.message = (TextView) convertView.findViewById(R.id.message);
//        } else
//            msgView.message = (TextView) convertView.findViewById(R.id.message);
        msgView.timestamp = (TextView) convertView.findViewById(R.id.timestamp);

        convertView.setTag(msgView);
//        } else {
//            msgView = (PubSubListRowUi) convertView.getTag();
//        }
String theMessage = (String) dsMsg.getMessage();
        msgView.sender.setText(dsMsg.getSender());
//        if (dsMsg.getMessage() instanceof Bitmap  && dsMsg.getSender().equals(MainActivity1.mUsername)) {
//            msgView.image.setImageBitmap((Bitmap) dsMsg.getMessage());
//            msgView.image.setVisibility(View.VISIBLE);
//            msgView.message.setText("");
        if (theMessage.contains("isphoto") && dsMsg.getSender().equals(MainActivity1.mUsername)) {
//            msgView.image.setImageBitmap((Bitmap) dsMsg.getMessage());
            theMessage=theMessage.replaceFirst("isphoto","");
            try {
//                Bitmap myBitmap = BitmapFactory.decodeFile(theMessage);
                Bitmap myBitmap = new ImageSaver(context).
                        setFileName(theMessage).
                        setDirectoryName("images").
                        load();
//                Bitmap myBitmap = ImageLoader.init().from(theMessage).requestSize(512, 512).getBitmap();
                msgView.image.setImageBitmap(myBitmap);

            }catch (Exception e){

            }
            msgView.image.setVisibility(View.VISIBLE);
            msgView.message.setText("");
        } else {
            msgView.message.setText(newLine + dsMsg.getMessage() + "\n");
            msgView.message.setVisibility(View.VISIBLE);
        }
        msgView.timestamp.setText(dsMsg.getTimestamp());
        if (dsMsg.getSender().equals(MainActivity1.mUsername)) {

//            msgView.sender.setTextAppearance(R.style.boldText);
//            msgView.message.setTextAppearance(R.style.boldText);
//            msgView.message.setTextAppearance(R.style.boldText);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    public void clear() {
        this.values.clear();
        notifyDataSetChanged();
    }
}
