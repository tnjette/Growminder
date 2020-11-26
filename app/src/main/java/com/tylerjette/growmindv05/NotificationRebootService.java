package com.tylerjette.growmindv05;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by tylerjette on 3/8/18. This class reactivates the former pending notifications that were lost when the user's phone rebooted.
 * based on what is in the database's activeNotification Table
 */

public class NotificationRebootService extends JobIntentService { //this used to extend just IntentService

    /**adding methods to accommodate the JobIntentService extension*/
    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationRebootService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Boolean anythingInDb = false;
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String genericCalendarProjection[] = {
            calendarContract.calendarEntry.COLUMN_VARIETY_ID
        };

        Cursor cursor = db.query(
            calendarContract.calendarEntry.TABLE_NAME,
                genericCalendarProjection,
                    null, null, null, null, null
        );

        while (cursor.moveToNext()){
            anythingInDb = true;
        }
        cursor.close();

        if(anythingInDb){
                //then continue to query the whole database, and reset the alarm system...
            setAlarm();
        }else{
                //do nothing, move on with your life.
        }
    }
    private PendingIntent pendingIntent;

    private void setAlarm(){
        Context appContext = getApplicationContext();
        Intent alarmIntent = new Intent(appContext, updatedAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(appContext, 43224, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
