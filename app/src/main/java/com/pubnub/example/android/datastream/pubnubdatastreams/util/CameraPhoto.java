package com.pubnub.example.android.datastream.pubnubdatastreams.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPhoto {
    private String photoPath;
    private Context context;

    public String getPhotoPath() {
        return this.photoPath;
    }

    public CameraPhoto(Context context) {
        this.context = context;
    }

    public Intent takePhotoIntent() {
        Intent in = new Intent("android.media.action.IMAGE_CAPTURE");
        if (in.resolveActivity(this.context.getPackageManager()) != null) {
            File photoFile = this.createImageFile();
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //use this if Lollipop_Mr1 (API 22) or above
                    in.putExtra("output", FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", photoFile));
                } else {
                    in.putExtra("output", Uri.fromFile(photoFile));
                }
//                    in.putExtra("output", Uri.fromFile(photoFile));
            }
        }

        return in;
    }

    private File createImageFile() {
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //use this if Lollipop_Mr1 (API 22) or above
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {

            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
//        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        File image = new File(storageDir, imageFileName + ".jpg");
        this.photoPath = image.getAbsolutePath();
        return image;
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(this.photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);
    }
}
