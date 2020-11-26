package com.tylerjette.growmindv05;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tylerjette on 11/6/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SQLite_GrowmindV0.5_Database.db";

    private static SQLiteHelper mInstance = null;
    public static SQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new SQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //this oncreate statement corresponds to the contract classes
        db.execSQL(locationContract.CREATE_LOCATION_TABLE);
        db.execSQL(varietyContract.CREATE_VARIETY_TABLE);
        db.execSQL(calendarContract.CREATE_CALENDAR_TABLE);
        db.execSQL(userVisitationContract.CREATE_USER_VISITATION_TABLE);
        db.execSQL(immediateCalendarContract.CREATE_IMMEDIATE_CALENDAR_TABLE);
        db.execSQL(arrestedEventsCalendarContract.CREATE_ARRESTED_CALENDAR_TABLE);
        db.execSQL(mostRecentEventsCalendarContract.CREATE_MOST_RECENT_EVENTS_TABLE);
        db.execSQL(channelIDContract.CREATE_CHANNEL_ID_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(locationContract.DELETE_LOCATION_TABLE);
        db.execSQL(varietyContract.DELETE_VARIETY_TABLE);
        db.execSQL(calendarContract.DELETE_CALENDAR_TABLE);
        db.execSQL(userVisitationContract.DELETE_USER_VISITATION_TABLE);
        db.execSQL(immediateCalendarContract.DELETE_IMMEDIATE_CALENDAR_TABLE);
        db.execSQL(arrestedEventsCalendarContract.DELETE_ARRESTED_CALENDAR_TABLE);
        db.execSQL(mostRecentEventsCalendarContract.DELETE_MOST_RECENT_EVENTS_TABLE);

        //CREATE NEW DB
        onCreate(db);
    }
}

