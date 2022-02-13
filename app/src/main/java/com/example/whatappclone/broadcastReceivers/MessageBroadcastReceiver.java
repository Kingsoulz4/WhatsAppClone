package com.example.whatappclone.broadcastReceivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.whatappclone.ChatActivity;
import com.example.whatappclone.MainActivity;
import com.example.whatappclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageBroadcastReceiver extends BroadcastReceiver {

    int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notice(context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notice(Context context) {
        Log.d("Notice", "something");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "abcxyz")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Notification")
                .setContentText("Connection change");
        Intent forwardIntent = new Intent(context.getApplicationContext(), ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, forwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS );
        //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getApplicationContext().getPackageName() + "/" + R.raw.iphone_sms_tonemix);
        //notificationBuilder.setSound(uri);



        notificationID = 23;

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
}
