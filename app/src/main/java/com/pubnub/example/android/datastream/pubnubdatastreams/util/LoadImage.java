package com.pubnub.example.android.datastream.pubnubdatastreams.util;

import android.content.Context;
import android.widget.ImageView;

import com.pubnub.example.android.datastream.pubnubdatastreams.R;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by User on 01/17/2018.
 */

public class LoadImage {
    public static void loadImageProfile(String image, ImageView imageView, Context applicationContext) {
        loadImageProfile( image, 100,  imageView,  applicationContext);
    }
public static void loadImageProfile(String image,int size, ImageView imageView, Context applicationContext) {
        if (image == null)
            image = "";
        try {
            Picasso.with(applicationContext)
                    .load(image)
                    .resize(size, size)
                    .transform(new CircleTransform())
                    .into(imageView);
        } catch (Exception e) {
            Picasso.with(applicationContext)
                    .load(R.drawable.avatar)
                    .resize(size, size)
                    .transform(new CircleTransform())
                    .into(imageView);
        }
    }

}
