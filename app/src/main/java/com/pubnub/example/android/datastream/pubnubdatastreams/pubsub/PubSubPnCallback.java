package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.example.android.datastream.pubnubdatastreams.MainActivity;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;

public class PubSubPnCallback extends SubscribeCallback {
    private static final String TAG = PubSubPnCallback.class.getName();
    private final PubSubListAdapter pubSubListAdapter;

    public PubSubPnCallback(PubSubListAdapter pubSubListAdapter) {
        this.pubSubListAdapter = pubSubListAdapter;

    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
        /*
        switch (status.getCategory()) {
             // for common cases to handle, see: https://www.pubnub.com/docs/java/pubnub-java-sdk-v4
             case PNStatusCategory.PNConnectedCategory:
             case PNStatusCategory.PNUnexpectedDisconnectCategory:
             case PNStatusCategory.PNReconnectedCategory:
             case PNStatusCategory.PNDecryptionErrorCategory:
         }
        */

        // no status handling for simplicity
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        try {
            Log.v(TAG, "messagenumber1(" + JsonUtil.asJson(message) + ")");
            JsonNode jsonMsg = message.getMessage();
            PubSubPojo dsMsg = JsonUtil.convert(jsonMsg, PubSubPojo.class);
            if (!PostVariables.lastMessage.equals("" + JsonUtil.asJson(message))) {
                PostVariables.lastMessage = "" + JsonUtil.asJson(message);
                boolean newMessage = false;
                if (!MainActivity.mUsername.equals(dsMsg.getSender()) && (PostVariables.person == null || !PostVariables.person.channel.equals(dsMsg.getChannel())))
                    newMessage = true;

                Person.alldata.get(MainActivity.mUsername).get(dsMsg.getChannel()).setlastMessage(dsMsg.getMessageFromType(), newMessage);
                Person person = Person.alldata.get(MainActivity.mUsername).get(dsMsg.getChannel());
                MainActivity.adbPerson.add(person);
            }
            if (PostVariables.person != null)
                this.pubSubListAdapter.add(dsMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        // no presence handling for simplicity
    }

    public void setListChatAdapter(AdapterPerson adbPerson) {
        adbPerson = adbPerson;
    }
}
