package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 01/04/2018.
 */

public class Samples {
    public Map<String, ArrayList<Person>> data = new HashMap<>();
    public String[] Ardit = {"Ardit", "https://blog.bufferapp.com/wp-content/uploads/2014/05/dreamstimefree_169606.jpg"};
    public String[] Beni = {"Beni", "https://blog.bufferapp.com/wp-content/uploads/2014/05/1443725_58313555.jpg"};
    public String[] Mira = {"Mira", "https://blog.bufferapp.com/wp-content/uploads/2014/05/6110974997_8b0dfa13a0_b.jpg"};
    public String[] Tani = {"Tani", "https://blog.bufferapp.com/wp-content/uploads/2014/05/file0001897600074.jpg"};
    public ArrayList<Person> myListItems = new ArrayList<Person>();

    public Samples() {
        myListItems.add(new Person(Beni[0], Beni[1], "0xAAAAAAAA"));
        myListItems.add(new Person(Mira[0], Mira[1], "0xAAAAAAAB"));
        myListItems.add(new Person(Tani[0], Tani[1], "0xAAAAAAAC"));
        data.put("Ardit", new ArrayList<>(myListItems));
        myListItems.clear();
        myListItems.add(new Person(Ardit[0], Ardit[1], "0xAAAAAAAA"));
        myListItems.add(new Person(Mira[0], Mira[1], "0xAAAAAAAD"));
        myListItems.add(new Person(Tani[0], Tani[1], "0xAAAAAAAE"));
        data.put("Beni", new ArrayList<>(myListItems));
        myListItems.clear();
        myListItems.add(new Person(Ardit[0], Ardit[1], "0xAAAAAAAB"));
        myListItems.add(new Person(Beni[0], Beni[1], "0xAAAAAAAD"));
        myListItems.add(new Person(Tani[0], Tani[1], "0xAAAAAAAF"));
        data.put("Mira", new ArrayList<>(myListItems));
        myListItems.clear();
        myListItems.add(new Person(Ardit[0], Ardit[1], "0xAAAAAAAC"));
        myListItems.add(new Person(Beni[0], Beni[1], "0xAAAAAAAE"));
        myListItems.add(new Person(Mira[0], Mira[1], "0xAAAAAAAF"));
        data.put("Tani", new ArrayList<>(myListItems));
        myListItems.clear();
    }

    public ArrayList<Person> getData(String key) {

        return data.get(key);
    }

}
