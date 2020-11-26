package com.tylerjette.growmindv05;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;


/**I don't think this is being used currently*/

/**
 * this is an updated version of the original addVarieties Activity, from package version
 * v05
 *
 * Now, instead of this class opening and storing data into an sql instance, and processing
 * notification information and creating notifications, this class will only open, delete if necessary,
 * and store data to the db, and initialize an alarm system that will operate daily,
 *
 * */



public class updatedAddVarietiesActivity extends AppCompatActivity{

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_add_varieties_activity);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(updatedAddVarietiesActivity.this, updatedAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(updatedAddVarietiesActivity.this, 0, alarmIntent, 0);

        findViewById(R.id.stopAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }

    public void cancel() {
        final Intent alarmIntent = new Intent(this, updatedAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(updatedAddVarietiesActivity.this, 43224, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }
}
