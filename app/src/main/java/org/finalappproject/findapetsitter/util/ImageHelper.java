package org.finalappproject.findapetsitter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.finalappproject.findapetsitter.application.AppConstants;

import java.io.ByteArrayOutputStream;

/**
 * Helper class to support working with images
 */
public abstract class ImageHelper {
    private static final String LOG_TAG = "ImageHelper";

    /**
     * Launch galery/image picker activity as described in
     * https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media#accessing-stored-media
     *
     * @param activity
     */
    public static void startImagePickerActivity(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, AppConstants.CODE_IMAGE_PICKER);
        } else {
            Toast.makeText(activity, "Failed to launch image picker", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param fileName
     * @param bitmap
     * @param quality
     * @return
     */
    public static ParseFile createParseFile(String fileName, Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        byte[] image = stream.toByteArray();
        return new ParseFile(fileName, image);
    }

    /**
     * @param fileName
     * @param bitmap
     * @return
     */
    public static ParseFile createParseFile(String fileName, Bitmap bitmap) {
        // Compress image to lower quality scale 1 - 100
        return createParseFile(fileName, bitmap, 100);
    }

    public static void loadImage(final Context context, ParseFile parseFile, final int placeholderResourceId, final ImageView imageView) {
        // Profile image
        if (parseFile != null) {
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Glide.with(context).load(data).centerCrop().dontAnimate().into(imageView);
                    } else {
                        Log.e(LOG_TAG, "Failed to load parse file", e);
                        imageView.setImageResource(placeholderResourceId);
                    }
                }
            });
        } else {
            imageView.setImageResource(placeholderResourceId);
        }
    }
}
