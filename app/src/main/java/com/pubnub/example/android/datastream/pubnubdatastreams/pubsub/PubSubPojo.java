package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class PubSubPojo {
    public static final int TYPE_STRING = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_FILE = 2;

    private final String sender;
    private final Object message;
    private final String timestamp;
    private final String timestampOriginal;
    private DateTimeUtil dateTimeUtil;


    public PubSubPojo(@JsonProperty("sender") String sender, @JsonProperty("message") Object message, @JsonProperty("timestamp") String timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.timestampOriginal = timestamp;
    }


    public String getSender() {
        return sender;
    }

    public Object getMessage() {
        return message;
    }

    public String getTimestampOriginal() {
        return timestampOriginal;
    }

    public int getTypeOfmessage() {
        if (this.message instanceof ArrayList) {
            if (((ArrayList) message).get(0).equals("isphoto"))
                return TYPE_STRING;
            if (((ArrayList) message).get(0).equals("isfile"))
                return TYPE_FILE;
        }
        return TYPE_STRING;
    }

    public String sendMessage() {
        String message = (String) this.message;
        int sdf = android.os.Build.VERSION.SDK_INT;
        if ((!message.contains("isphoto")))
            return message;
        message = message.replace("isphoto", "");
        File fi = new File(message);
        byte[] bytes = {};

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Files.readAllBytes(fi.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "isphoto" + new String(bytes);
    }

    public String getTimestamp() {
        return timestamp.substring(11, 16);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final PubSubPojo other = (PubSubPojo) obj;

        return Objects.equal(this.sender, other.sender)
                && Objects.equal(this.message, other.message)
                && Objects.equal(this.timestamp, other.timestamp);
    }

    public String getMessageFromType() {
        if (getTypeOfmessage() == PubSubPojo.TYPE_IMAGE)
            return "Image";
        else if (getTypeOfmessage() == PubSubPojo.TYPE_FILE)
            return "File";
        else return getMessage().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sender, message, timestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(PubSubPojo.class)
                .add("sender", sender)
//                .add("message", message.toString())
                .add("message", getMessage().toString())
                .add("timestamp", timestamp)
                .toString();
    }
}
