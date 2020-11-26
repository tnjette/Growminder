package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

public class arrestedEventsCalendarContract {

    private arrestedEventsCalendarContract(){}

    public static final String CREATE_ARRESTED_CALENDAR_TABLE =
            "CREATE TABLE " + arrestedCalendarEntry.TABLE_NAME + " ("+
                    arrestedCalendarEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL," +
                    arrestedCalendarEntry.COLUMN_EVENT_DATE + " TEXT NOT NULL," +
                    arrestedCalendarEntry.COLUMN_EVENT_TITLE + " TEXT NOT NULL)";

    public static final String DELETE_ARRESTED_CALENDAR_TABLE =
            "DROP TABLE IF EXISTS " + arrestedCalendarEntry.TABLE_NAME;

    public static final String DELETE_OLD_ARRESTED_CALENDAR_DATA =
            "DELETE FROM " + arrestedCalendarEntry.TABLE_NAME;

    //define table columns
    public static class arrestedCalendarEntry implements BaseColumns {
        public static final String TABLE_NAME = "calendarOfArrestedEvents";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_EVENT_DATE = "eventDate";
        public static final String COLUMN_EVENT_TITLE = "eventTitle";
    }
}
