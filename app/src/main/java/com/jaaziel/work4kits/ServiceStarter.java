package com.jaaziel.work4kits;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Arthur on 25/03/2017.
 */

public class ServiceStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNewService(context);
    }

    private void createNewService(Context context) {
        int NOTIFICATION_CODE = IOSingleton.Instance().getNOTIFICATION_CODE();
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent questionIntent = PendingIntent.getBroadcast(context, NOTIFICATION_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long atual = Calendar.getInstance().getTimeInMillis();
        long vintesegundos = 1000*60*20;
        alarmMgr.set(AlarmManager.RTC_WAKEUP, atual+vintesegundos,  questionIntent);
    }
}