package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;

public class PubSubPojo {
    public static final int TYPE_STRING = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_FILE = 2;


    private final String id;
    private final String uniqueId;
    private final String message;
    private final String messageType;
    private final String sender;
    private String channel;
    private final String sendDate;
    private final String receiveDate;
    private final String timestamp;
    private String lastSeen;

    private DateTimeUtil dateTimeUtil;


    public PubSubPojo(@JsonProperty("id ") String id,
                      @JsonProperty("uniqueId ") String uniqueId,
                      @JsonProperty("message") String message,
                      @JsonProperty("messageType ") String messageType,
                      @JsonProperty("sender") String sender,
                      @JsonProperty("channel") String channel,
                      @JsonProperty("sendDate") String sendDate,
                      @JsonProperty("receiveDate") String receiveDate,
                      @JsonProperty("lastSeen") String lastSeen) {
        this.id = id != null ? id : "";
        this.uniqueId = uniqueId != null ? uniqueId : "";
        this.message = message != null ? message : "";
        this.messageType = messageType != null ? messageType : "";
        this.sender = sender != null ? sender : "";
        this.channel = channel != null ? channel : "";
        this.sendDate = sendDate != null ? sendDate : "";
        this.receiveDate = receiveDate != null ? receiveDate : "";
        this.timestamp = sendDate != null ? sendDate : "";
        this.lastSeen = lastSeen != null ? lastSeen : "false";

    }


    public String getId() {
        return id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSender() {
        return sender;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel() {
        if (this.channel.contains("-receipts")) {
            String channel = this.channel.replace("-receipts", "");
            this.channel = channel;
        }
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public DateTimeUtil getDateTimeUtil() {
        return dateTimeUtil;
    }


    public int getTypeOfmessage() {

        if ((messageType).equals("photo"))
            return TYPE_IMAGE;
        if ((messageType).equals("file"))
            return TYPE_FILE;

        return TYPE_STRING;
    }


    public String getTimestamp() {
        return timestamp.substring(11, 16);
    }
    public String getDatestamp() {
        return timestamp.substring(0,10);
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
                .add("message", message)
                .add("timestamp", timestamp)
                .toString();
    }

    public void setLastSeen() {
        this.lastSeen = this.uniqueId;
    }
}
