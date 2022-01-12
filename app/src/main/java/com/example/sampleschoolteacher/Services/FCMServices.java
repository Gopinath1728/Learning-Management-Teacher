package com.example.sampleschoolteacher.Services;

import androidx.annotation.NonNull;

import com.example.sampleschoolteacher.Common.Common;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;


public class FCMServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> dataRecv = remoteMessage.getData();
        Common.showNotification(this, new Random().nextInt(),
                dataRecv.get("title"),
                dataRecv.get("content"),
                null);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Common.updateToken(this,s);
    }
}
