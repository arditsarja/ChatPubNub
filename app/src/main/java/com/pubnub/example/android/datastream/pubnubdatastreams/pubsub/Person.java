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
    public String dateStamp;
    public boolean newMessage;
    public int numberOfnewMessage;
    public boolean seen;


    public Person() {
    }


    public Person(String name, String image, String channel) {
        this.name = name;
        this.image = image;
        this.channel = channel;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public void fillFieldWithData(String messageFromType, boolean newMessage,boolean seen,String dateStamp) {
        setlastMessage(messageFromType,newMessage);
        setSeen(seen);
        setDateStamp(dateStamp);
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

    public void setSeen(boolean seen){
        this.seen=seen;
    }

    public void setlastMessage(String messageFromType) {
        this.lastMessage = messageFromType;
    }
}
