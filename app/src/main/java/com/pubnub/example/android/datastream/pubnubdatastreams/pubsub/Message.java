package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

/**
 * Created by User on 01/03/2018.
 */

public class Message {
    public String type;
    public String content;

    public Message() {
    }

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
