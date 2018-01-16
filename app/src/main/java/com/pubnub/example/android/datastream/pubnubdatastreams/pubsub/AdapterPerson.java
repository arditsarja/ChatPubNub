package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 01/03/2018.
 */

public class AdapterPerson extends ArrayAdapter<Person> {
    private Activity activity;
    private Map<String, Person> lPerson = new HashMap<>();
    private static LayoutInflater inflater = null;


    public AdapterPerson(Activity activity, int textViewResourceId) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
        }
    }

    public Map<String, Person> getlPerson() {
        return lPerson;
    }

    public Person getPersonFromchannel(String channel) {
        return lPerson.get(channel);
    }

    public AdapterPerson(Activity activity, int textViewResourceId, Map<String, Person> _lPerson) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            this.lPerson = _lPerson;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public void changeLayout() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void add(Person person) {
//        this.values.add(0, message);
        lPerson.put(person.channel, person);
        changeLayout();

    }

    public int getCount() {
        return lPerson.size();
    }

    public Person getItem(int position) {
        return (Person) lPerson.values().toArray()[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView display_image;
        public TextView display_name;
        public TextView last_message;
        public TextView number_of_new_messages;
        public TextView date_stamp;
        public ImageView seen;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        float scale = activity.getBaseContext().getResources().getDisplayMetrics().density;
        //10 dp in pixels
        int dpAsPixels = (int) (10*scale + 0.5f);
        int padingleft = (int) (25*scale + 0.5f);

        Person person = getItem(position);
        try {
//            if (convertView == null) {
                vi = inflater.inflate(R.layout.chat_layout, null);
                holder = new ViewHolder();

                holder.display_image = (ImageView) vi.findViewById(R.id.display_image);
                holder.display_name = (TextView) vi.findViewById(R.id.display_name);
                holder.date_stamp = (TextView) vi.findViewById(R.id.date);
                holder.last_message = (TextView) vi.findViewById(R.id.last_message);
                holder.number_of_new_messages = (TextView) vi.findViewById(R.id.number_of_new_messages);
                holder.seen = (ImageView) vi.findViewById(R.id.seen);


                vi.setTag(holder);
//            } else {
//                holder = (ViewHolder) vi.getTag();
//            }

            Picasso.with(getContext())
                    .load(person.image)
                    .resize(150, 150)
                    .transform(new CircleTransform())
                    .into(holder.display_image);
            holder.display_name.setText(person.name);
            holder.date_stamp.setText(person.getDateStamp());
            holder.last_message.setText(person.lastMessage);
            holder.number_of_new_messages.setText("" + person.numberOfnewMessage);
            if (person.newMessage) {
                person.seen=false;
                holder.last_message.setTypeface(holder.last_message.getTypeface(), Typeface.BOLD);
                holder.date_stamp.setTypeface(holder.date_stamp.getTypeface(), Typeface.BOLD);
                holder.number_of_new_messages.setVisibility(View.VISIBLE);
            }

            if(person.seen) {
                holder.seen.setVisibility(View.VISIBLE);
                holder.last_message.setPadding(padingleft, 0, dpAsPixels, dpAsPixels);
            }


        } catch (Exception e) {


        }
        return vi;
    }
}
