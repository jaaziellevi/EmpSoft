package com.jaaziel.work4kits;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Arthur on 25/03/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static Notification notification;
    private static NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    private static void showNotification(Context context) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, Work4kits.class), 0);
        notification = new NotificationCompat.Builder(context)
                .setTicker(context.getResources().getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentIntent(pi)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentText("Nova vaga")
                .build();
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
