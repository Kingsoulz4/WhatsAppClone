package com.example.whatappclone;

import android.app.Application;
import android.content.Intent;

import com.example.whatappclone.services.MessageNotificationService;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MessageNotificationService.class));
    }
}
