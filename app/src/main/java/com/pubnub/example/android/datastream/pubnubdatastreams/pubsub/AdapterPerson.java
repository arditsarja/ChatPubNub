package com.pubnub.example.android.datastream.pubnubdatastreams.pubsub;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 01/03/2018.
 */

public class AdapterPerson extends ArrayAdapter<Person> {
    private Activity activity;
    private ArrayList<Person> lPerson = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public AdapterPerson(Activity activity, int textViewResourceId) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
        }
    }

    public AdapterPerson(Activity activity, int textViewResourceId, ArrayList<Person> _lPerson) {
        super(activity, textViewResourceId, _lPerson);
        try {
            this.activity = activity;
            this.lPerson = _lPerson;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public void update(Person person, int index) {
        if (lPerson.size() == index)
            lPerson.add(person);
        else
            lPerson.set(index, person);

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
        lPerson.add(person);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


    public int getCount() {
        return lPerson.size();
    }

    public Person getItem(Person position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView display_image;
        public TextView display_name;
        public TextView last_message;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.chat_layout, null);
                holder = new ViewHolder();

                holder.display_image = (ImageView) vi.findViewById(R.id.display_image);
                holder.display_name = (TextView) vi.findViewById(R.id.display_name);
                holder.last_message = (TextView) vi.findViewById(R.id.last_message);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            Picasso.with(getContext())
                    .load(lPerson.get(position).image)
                    .resize(150, 150)
                    .transform(new CircleTransform())
                    .into(holder.display_image);
            holder.display_name.setText(lPerson.get(position).name);
            holder.last_message.setText(lPerson.get(position).lastMessage);
//            holder.last_message.setText("test");


        } catch (Exception e) {


        }
        return vi;
    }
}
