package com.example.user.secondhandtradingplatform;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.user.secondhandtradingplatform.R;

import activity.Main;

public class AlarmReceiver extends BroadcastReceiver {
    public static String TAG = "Receiver";
    String seller, product, time, date, location;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, UserProfile.class);
        seller = intent.getStringExtra("seller");
        product = intent.getStringExtra("product");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");
        location = intent.getStringExtra("location");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Main.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("交易日提示")
                .setContentText("記得今日約咗"+seller+ " " + time+"係"+location+"交收呀")
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}