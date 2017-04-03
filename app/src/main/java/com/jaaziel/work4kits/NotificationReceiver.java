package com.jaaziel.work4kits;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.toolbox.Volley;

import java.util.Calendar;

/**
 * Created by Arthur on 25/03/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static Notification notification;
    private static NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationService", "Notificação recebida");
        createNewService(context);
        checaMudanca(context);

        //manda broadcast para MainActivity
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction("updateFragment");
        context.sendBroadcast(broadCastIntent);
    }

    private void checaMudanca(Context context) {
        RESTUtil rest = new RESTUtil(Volley.newRequestQueue(context), context);
    }

    private void createNewService(Context context) {
        int NOTIFICATION_CODE = IOSingleton.Instance().getNOTIFICATION_CODE();
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent questionIntent = PendingIntent.getBroadcast(context, NOTIFICATION_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long atual = Calendar.getInstance().getTimeInMillis();
        long vintesegundos = 1000*20;
        alarmMgr.set(AlarmManager.RTC_WAKEUP, atual+vintesegundos,  questionIntent);
    }

}
