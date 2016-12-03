package org.finalappproject.findapetsitter.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.model.User;

/**
 * Service responsible for saving the FCM user token to the Parse backend
 * implemented as described in https://github.com/codepath/android_guides/wiki/Google-Cloud-Messaging
 */
public class FirebaseMessagingRegistrationService extends IntentService {

    private static final String TAG = "FCMRegistrationService";

    public FirebaseMessagingRegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        final String token = instanceID.getToken();

        User user = (User) User.getCurrentUser();
        if (user != null) {
            try {
                user.fetchIfNeeded();
                user.setFcmToken(token);
                user.save();
            } catch (ParseException e) {
                Log.e(TAG, "Failed to save user FCM token", e);
            }
        }

    }

}
