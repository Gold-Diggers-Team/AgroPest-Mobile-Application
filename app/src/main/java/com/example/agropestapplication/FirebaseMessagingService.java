package com.example.agropestapplication;

import com.google.firebase.messaging.RemoteMessage;

public abstract class FirebaseMessagingService {
    public abstract void onMessageReceived(RemoteMessage remoteMessage);
}
