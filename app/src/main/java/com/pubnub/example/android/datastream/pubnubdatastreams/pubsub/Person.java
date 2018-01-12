package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import java.util.Map;

/**
 * Created by User on 01/03/2018.
 */

public class Person {

    public static Map<String, Map<String, Person>> alldata;

    public String name;
    public String image;
    public String channel;
    public String lastMessage;
    public boolean newMessage;
    public int numberOfnewMessage;

    public Person() {
    }


    public Person(String name, String image, String channel) {
        this.name = name;
        this.image = image;
        this.channel = channel;
    }

    public void setlastMessage(String messageFromType, boolean newMessage) {
        this.newMessage = newMessage;
        if (newMessage) {
            numberOfnewMessage++;
        }
        setlastMessage(messageFromType);
    }

    public void setReadMessage() {
        newMessage = false;
        numberOfnewMessage = 0;
    }

    public void setlastMessage(String messageFromType) {
        this.lastMessage = messageFromType;
    }
}
