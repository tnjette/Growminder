package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

/**
 * Created by tylerjette on 12/4/17.
 */

public final class immediateCalendarContract {
    private immediateCalendarContract(){}

    public static final String CREATE_IMMEDIATE_CALENDAR_TABLE =
        "CREATE TABLE " + immediateCalendarEntry.TABLE_NAME + " ("+
        immediateCalendarEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL," +
        immediateCalendarEntry.COLUMN_EVENT_DATE + " TEXT NOT NULL," +
        immediateCalendarEntry.COLUMN_EVENT_TITLE + " TEXT NOT NULL," +
        immediateCalendarEntry.COLUMN_EVENT_DESCRIPTION + " TEXT NOT NULL)";

    public static final String DELETE_IMMEDIATE_CALENDAR_TABLE =
            "DROP TABLE IF EXISTS " + immediateCalendarEntry.TABLE_NAME;

    public static final String DELETE_OLD_IMMEDIATE_CALENDAR_DATA =
            "DELETE FROM " + immediateCalendarEntry.TABLE_NAME;

    public static class immediateCalendarEntry implements BaseColumns {
        public static final String TABLE_NAME = "calendarOfImmediateEvents";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_EVENT_DATE = "eventDate";
        public static final String COLUMN_EVENT_TITLE = "eventTitle";
        public static final String COLUMN_EVENT_DESCRIPTION = "eventDescription";
    }
}
