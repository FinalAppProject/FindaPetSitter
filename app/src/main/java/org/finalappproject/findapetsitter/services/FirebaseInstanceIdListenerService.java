package org.finalappproject.findapetsitter.services;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Service responsible for listening to FCM token changes and starting the token registration service
 * for more info see: https://developers.google.com/cloud-messaging/android/android-migrate-fcm
 */
public class FirebaseInstanceIdListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, FirebaseMessagingRegistrationService.class);
        startService(intent);
    }
}
