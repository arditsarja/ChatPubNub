package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosalgeek.android.photoutil.ImageLoader;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;

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
    public void addFormServer(PubSubPojo message) {
;
        if ((message.getSender().equals(PostVariables.person.name))||(message.getSender().equals(PostVariables.mUsername)))
            this.values.add(0,message);

        ((Activity) this.context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void add(PubSubPojo message) {
//        this.values.add(0, message);
        if ((message.getSender().equals(PostVariables.person.name))||(message.getSender().equals(PostVariables.mUsername)))
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


        if (dsMsg.getSender().equals(PostVariables.mUsername))
            convertView = inflater.inflate(R.layout.list_row_pubsub_sender, parent, false);
        else {
            convertView = inflater.inflate(R.layout.list_row_pubsub_recive, parent, false);
            newLine = "\n";
        }

        if ((!dsMsg.getSender().equals(PostVariables.person.name))&&(!dsMsg.getSender().equals(PostVariables.mUsername)))
            convertView.setVisibility(View.GONE);

        msgView = new PubSubListRowUi();
        msgView.sender = (TextView) convertView.findViewById(R.id.sender);
        msgView.image = (ImageView) convertView.findViewById(R.id.imageView);
        msgView.message = (TextView) convertView.findViewById(R.id.message);
        msgView.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        convertView.setTag(msgView);


        msgView.sender.setText(dsMsg.getSender());

        if (dsMsg.getMessage() instanceof ArrayList && ((ArrayList) dsMsg.getMessage()).get(0).equals("isphoto") && dsMsg.getSender().equals(PostVariables.mUsername)) {

            try {
                Bitmap myBitmap = ImageLoader.init().from((String) ((ArrayList) dsMsg.getMessage()).get(1)).requestSize(512, 512).getBitmap();
                msgView.image.setImageBitmap(myBitmap);
            } catch (Exception e) {
            }
            msgView.image.setVisibility(View.VISIBLE);
            msgView.message.setText("");
        } else {
            msgView.message.setText(newLine + dsMsg.getMessage() + "\n");
            msgView.message.setVisibility(View.VISIBLE);
        }
        msgView.timestamp.setText(dsMsg.getTimestamp());


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
