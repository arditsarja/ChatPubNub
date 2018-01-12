package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 01/04/2018.
 */

public class Samples {
    public static Map<String, Map<String, Person>> data = new HashMap<>();
    public static Map<String, Map<String, Person>> elements = fill();
    public String[] Ardit = {"Ardit", "https://blog.bufferapp.com/wp-content/uploads/2014/05/dreamstimefree_169606.jpg"};
    public String[] Beni = {"Beni", "https://blog.bufferapp.com/wp-content/uploads/2014/05/1443725_58313555.jpg"};
    public String[] Goni = {"Goni", "https://blog.bufferapp.com/wp-content/uploads/2014/05/1443725_58313555.jpg"};
    public String[] Mira = {"Mira", "https://blog.bufferapp.com/wp-content/uploads/2014/05/6110974997_8b0dfa13a0_b.jpg"};
    public String[] Tani = {"Tani", "https://blog.bufferapp.com/wp-content/uploads/2014/05/file0001897600074.jpg"};
    public String[] Laert = {"Laert", "https://blog.bufferapp.com/wp-content/uploads/2014/05/6110974997_8b0dfa13a0_b.jpg"};

    public Map<String, Person> myListItems = new HashMap<>();

    public Samples() {

    }

    public void init() {
        myListItems.put("0xAAAAAAAA", new Person(Beni[0], Beni[1], "0xAAAAAAAA"));
        myListItems.put("0xAAAAAAAB", new Person(Mira[0], Mira[1], "0xAAAAAAAB"));
        myListItems.put("0xAAAAAAAC", new Person(Tani[0], Tani[1], "0xAAAAAAAC"));
        myListItems.put("channel_demo1", new Person(Laert[0], Laert[1], "channel_demo1"));
        myListItems.put("ardit_goni", new Person(Goni[0], Goni[1], "ardit_goni"));
        data.put("Ardit", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAA", new Person(Ardit[0], Ardit[1], "0xAAAAAAAA"));
        myListItems.put("0xAAAAAAAD", new Person(Mira[0], Mira[1], "0xAAAAAAAD"));
        myListItems.put("0xAAAAAAAE", new Person(Tani[0], Tani[1], "0xAAAAAAAE"));
        data.put("Beni", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAB", new Person(Ardit[0], Ardit[1], "0xAAAAAAAB"));
        myListItems.put("0xAAAAAAAD", new Person(Beni[0], Beni[1], "0xAAAAAAAD"));
        myListItems.put("0xAAAAAAAF", new Person(Tani[0], Tani[1], "0xAAAAAAAF"));
        data.put("Mira", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAC", new Person(Ardit[0], Ardit[1], "0xAAAAAAAC"));
        myListItems.put("0xAAAAAAAE", new Person(Beni[0], Beni[1], "0xAAAAAAAE"));
        myListItems.put("0xAAAAAAAF", new Person(Mira[0], Mira[1], "0xAAAAAAAF"));
        data.put("Tani", new HashMap<String, Person>(myListItems));
        myListItems.clear();
    }

    public static Map<String, Map<String, Person>> fill() {
        Map<String, Map<String, Person>> elements = new HashMap<>();
        Map<String, Person> myListItems = new HashMap<>();
        String[] Ardit = {"Ardit", "https://blog.bufferapp.com/wp-content/uploads/2014/05/dreamstimefree_169606.jpg"};
        String[] Endri = {"Endri", "https://blog.bufferapp.com/wp-content/uploads/2014/05/dreamstimefree_169606.jpg"};
        String[] Beni = {"Beni", "https://blog.bufferapp.com/wp-content/uploads/2014/05/1443725_58313555.jpg"};
        String[] Goni = {"Goni", "https://blog.bufferapp.com/wp-content/uploads/2014/05/1443725_58313555.jpg"};
        String[] Mira = {"Mira", "https://blog.bufferapp.com/wp-content/uploads/2014/05/6110974997_8b0dfa13a0_b.jpg"};
        String[] Tani = {"Tani", "https://blog.bufferapp.com/wp-content/uploads/2014/05/file0001897600074.jpg"};
        String[] Laert = {"Laert", "https://blog.bufferapp.com/wp-content/uploads/2014/05/6110974997_8b0dfa13a0_b.jpg"};
        myListItems.put("0xAAAAAAAA", new Person(Beni[0], Beni[1], "0xAAAAAAAA"));
        myListItems.put("0xAAAAAAAB", new Person(Mira[0], Mira[1], "0xAAAAAAAB"));
        myListItems.put("0xAAAAAAAC", new Person(Tani[0], Tani[1], "0xAAAAAAAC"));
        myListItems.put("channel_demo1", new Person(Laert[0], Laert[1], "channel_demo1"));
        myListItems.put("ardit_goni", new Person(Goni[0], Goni[1], "ardit_goni"));
        myListItems.put("ardit_endri", new Person(Endri[0], Endri[1], "ardit_endri"));
        elements.put("Ardit", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAA", new Person(Ardit[0], Ardit[1], "0xAAAAAAAA"));
        myListItems.put("0xAAAAAAAD", new Person(Mira[0], Mira[1], "0xAAAAAAAD"));
        myListItems.put("0xAAAAAAAE", new Person(Tani[0], Tani[1], "0xAAAAAAAE"));
        elements.put("Beni", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAB", new Person(Ardit[0], Ardit[1], "0xAAAAAAAB"));
        myListItems.put("0xAAAAAAAD", new Person(Beni[0], Beni[1], "0xAAAAAAAD"));
        myListItems.put("0xAAAAAAAF", new Person(Tani[0], Tani[1], "0xAAAAAAAF"));
        elements.put("Mira", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("ardit_endri", new Person(Ardit[0], Ardit[1], "ardit_endri"));
        elements.put("Endri", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        myListItems.put("0xAAAAAAAC", new Person(Ardit[0], Ardit[1], "0xAAAAAAAC"));
        myListItems.put("0xAAAAAAAE", new Person(Beni[0], Beni[1], "0xAAAAAAAE"));
        myListItems.put("0xAAAAAAAF", new Person(Mira[0], Mira[1], "0xAAAAAAAF"));
        elements.put("Tani", new HashMap<String, Person>(myListItems));
        myListItems.clear();
        return elements;

    }

    public Map<String, Person> getData(String key) {
        init();
        return data.get(key);
    }

}
