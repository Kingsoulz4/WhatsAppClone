package com.example.whatappclone.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.whatappclone.ChatActivity;
import com.example.whatappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageNotificationService extends Service {

    public static final int NOTIF_ID = 2;
    public static final String NOTIF_CHANNEL_ID = "My_Channel_Id";
    MessageNotificationService messageNotificationService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        messageNotificationService = new MessageNotificationService();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction();
        Log.d("Service", "here");
        FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("Notimes here",snapshot.getKey());
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Log.d("Notimes here",snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notice() {
        Context context = this;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "abcxyz")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("New message")
                .setContentText("Message");
        Intent forwardIntent = new Intent(context.getApplicationContext(), ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, forwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS );
        //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getApplicationContext().getPackageName() + "/" + R.raw.iphone_sms_tonemix);
        //notificationBuilder.setSound(uri);



        int notificationID = 23;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(uri, audioAttributes);
            if (notificationManager != null) {
                List<NotificationChannel> channelList = notificationManager.getNotificationChannels();

                for (int i = 0; channelList != null && i < channelList.size(); i++) {
                    notificationManager.deleteNotificationChannel(channelList.get(i).getId());
                }
            }
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        notificationManager.notify(notificationID, notificationBuilder.build());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }


    private void startForeground() {
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "abcxyz")
//                .setSmallIcon(android.R.drawable.star_on)
//                .setContentTitle("Notification")
//                .setContentText("Service");
//        Intent forwardIntent = new Intent(this.getApplicationContext(), ForwardActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, forwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationBuilder.setContentIntent(pendingIntent);
//        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
//        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS );
//        //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.iphone_sms_tonemix);
//        //notificationBuilder.setSound(uri);
//
//        int notificationID = 9;
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//
//            NotificationChannel channel = new NotificationChannel(
//                    MainActivity.channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            channel.setSound(uri, audioAttributes);
//            if (notificationManager != null) {
//                List<NotificationChannel> channelList = notificationManager.getNotificationChannels();
//
//                for (int i = 0; channelList != null && i < channelList.size(); i++) {
//                    notificationManager.deleteNotificationChannel(channelList.get(i).getId());
//                }
//            }
//            notificationManager.createNotificationChannel(channel);
//            notificationBuilder.setChannelId(MainActivity.channelId);
//        }
//        notificationManager.notify(notificationID, notificationBuilder.build());

        Intent notificationIntent = new Intent(this, ChatActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel channel = new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    "Service readable chanel",
                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            channel.setSound(uri, audioAttributes);
//            if (notificationManager != null) {
//                List<NotificationChannel> channelList = notificationManager.getNotificationChannels();
//
//                for (int i = 0; channelList != null && i < channelList.size(); i++) {
//                    notificationManager.deleteNotificationChannel(channelList.get(i).getId());
//                }
//            }
            notificationManager.createNotificationChannel(channel);

        }



        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());

    }
}
