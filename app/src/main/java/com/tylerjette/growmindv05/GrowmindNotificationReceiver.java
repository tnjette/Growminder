package com.tylerjette.growmindv05;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

/**
 * Created by tylerjette on 12/12/17.
 */

public class GrowmindNotificationReceiver extends BroadcastReceiver {

    NotificationManager nm;
    @Override
    @TargetApi(26)
    public void onReceive (Context context, Intent intent){
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notification_id = 0;
        ArrayList<String> arr = new ArrayList<>();
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notificationIntent = new Intent(context, Dashboard.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT, 0);

        Notification.Builder builder = new Notification.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setTicker("New Growminder Notification")
            .setContentTitle("Growminder Notification")
            .setContentText("You know...like, stuff.")
            .setStyle(new Notification.BigTextStyle().bigText(arr.toString()))
            .setContentIntent(contentIntent)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notification_id, builder.build());
    }
}
