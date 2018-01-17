package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosalgeek.android.photoutil.ImageLoader;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.LoadImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        message.setChannel();
        if ((message.getChannel().equals(PostVariables.person.channel)) || (message.getSender().equals(PostVariables.person.name))) {
//        if ((message.getChannel().equals(PostVariables.person.channel)))
            if (values.indexOf(message) == -1)
                this.values.add(0, message);
//            else
//                this.values.set(this.values.indexOf(message), message);
            Log.v("LastSeen", "addi");
            Log.v("LastSeen", "addi");
            ((Activity) this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();

                    Log.v("LastSeen", "runni");
                    Log.v("LastSeen", "runni");

                }
            });
        }
    }

    @Override
    public void add(PubSubPojo message) {
//        this.values.add(0, message);
        if (message.getChannel().contains("-receipts")) {
            String hi = message.getChannel();
        }
        message.setChannel();
        if ((message.getChannel().equals(PostVariables.person.channel)) || (message.getSender().equals(PostVariables.person.name))) {
//        if ((message.getChannel().equals(PostVariables.person.channel)))
            if (values.indexOf(message) == -1)
                this.values.add(message);
            else
                this.values.set(this.values.indexOf(message), message);

            Log.v("LastSeen", "addi");
            Log.v("LastSeen", "addi");
            ((Activity) this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                    Log.v("LastSeen", "runni");
                    Log.v("LastSeen", "runni");
                }
            });
        }
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
            newLine = "";
//            newLine = "\n";
        }

//        if ((!dsMsg.getSender().equals(PostVariables.person.name)) && (!dsMsg.getSender().equals(PostVariables.mUsername)))
//            convertView.setVisibility(View.GONE);

        msgView = new PubSubListRowUi();
        msgView.sender = (TextView) convertView.findViewById(R.id.sender);
        msgView.image = (ImageView) convertView.findViewById(R.id.imageView);
        msgView.message = (TextView) convertView.findViewById(R.id.message);
        msgView.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        convertView.setTag(msgView);


        msgView.sender.setText(dsMsg.getSender());

        if (dsMsg.getMessageType().equals("photo") && PostVariables.mUsername.equals(dsMsg.getSender())) {
            try {
                LoadImage.loadImageInChat(dsMsg.getMessage(), msgView.image, context);
//                Bitmap myBitmap = ImageLoader.init().from(dsMsg.getMessage()).requestSize(512, 512).getBitmap();
//                msgView.image.setImageBitmap(myBitmap);
            } catch (Exception e) {
            }
            msgView.image.setVisibility(View.VISIBLE);
            msgView.message.setText("");
        } else {
            msgView.message.setText(newLine + dsMsg.getMessage());
            msgView.message.setVisibility(View.VISIBLE);
        }
        msgView.timestamp.setText(dsMsg.getTimestamp());
        if ((dsMsg.getLastSeen() != null && dsMsg.getLastSeen().equals(dsMsg.getUniqueId())) && dsMsg.getSender().equals(PostVariables.mUsername)) {
            msgView.seen = (ImageView) convertView.findViewById(R.id.seen);
            msgView.seen.setVisibility(View.VISIBLE);
        }
        if ((dsMsg.getLastSeen() == null || !dsMsg.getLastSeen().equals(dsMsg.getUniqueId())) && !dsMsg.getSender().equals(PostVariables.mUsername)) {

            dsMsg.setLastSeen();
            Map<String, String> message = new HashMap<String, String>();
            message.put("id", dsMsg.getId());
            message.put("uniqueId", dsMsg.getUniqueId());
            message.put("message", dsMsg.getMessage());
            message.put("messageType", dsMsg.getMessageType());
            message.put("sender", dsMsg.getSender());
            message.put("channel", dsMsg.getChannel() + "-receipts");
            message.put("sendDate", dsMsg.getSendDate());
            message.put("receiveDate", dsMsg.getReceiveDate());
            message.put("lastSeen", dsMsg.getUniqueId());
            PostVariables.mPubnub_DataStream.publish().channel(dsMsg.getChannel()).message(message).async(
                    new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            try {
                                if (!status.isError()) {

                                    Log.v("seen", "publish(" + JsonUtil.asJson(result) + ")");
//                                    Toast.makeText(context,"message is sendet for seen",Toast.LENGTH_LONG).show();
                                } else {
                                    Log.v("seen", "publishErr(" + JsonUtil.asJson(status) + ")");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }

        Log.v("LastSeen", "strampimi");
        Log.v("LastSeen", "strampimi");
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
