package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by tylerjette on 12/4/17.
 */

public final class calendarContract {
    private calendarContract(){}

    public static final String CREATE_CALENDAR_TABLE =
            "CREATE TABLE " + calendarEntry.TABLE_NAME + " ("+
                    calendarEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL," +
                    calendarEntry.COLUMN_EVENT_DATE + " TEXT NOT NULL," +
                    calendarEntry.COLUMN_VALIDATION_YEAR + " TEXT NOT NULL," +
                    calendarEntry.COLUMN_EVENT_TITLE + " TEXT NOT NULL," +
                    calendarEntry.COLUMN_EVENT_DESCRIPTION + " TEXT NOT NULL)";


    public static final String DELETE_CALENDAR_TABLE =
            "DROP TABLE IF EXISTS " + calendarEntry.TABLE_NAME;

    public static final String DELETE_OLD_CALENDAR_DATA =
            "DELETE FROM " + calendarEntry.TABLE_NAME;

    //define table columns
    public static class calendarEntry implements BaseColumns {
        public static final String TABLE_NAME = "calendarOfEvents";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_EVENT_DATE = "eventDate";
        /**here we're defining the schema of the calendar entry class to include a "validationYear" String,
         * which will simply be a year as a string. ie : "2020". This way the system will know when it can run
         * the notification that corresponds with this event notice. This date will be compared with the current
         * year, and if the year represented by this string is equal to or is in the past of the currrent year,
         * then the notification can be run, otherwise, it is not.
         * */
        public static final String COLUMN_VALIDATION_YEAR = "validationYear";
        public static final String COLUMN_EVENT_TITLE = "eventTitle";
        public static final String COLUMN_EVENT_DESCRIPTION = "eventDescription";
    }
}
