package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

public final class mostRecentEventsCalendarContract {
    private mostRecentEventsCalendarContract(){}

    public static final String CREATE_MOST_RECENT_EVENTS_TABLE =
            "CREATE TABLE " + mostRecentEventsEntry.TABLE_NAME + " ("+
                    mostRecentEventsEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL," +
                    mostRecentEventsEntry.COLUMN_VARIETY_EVENT_TITLE + " TEXT NOT NULL," +
                    mostRecentEventsEntry.COLUMN_VARIETY_EVENT_DESCRIPTION + "TEXT NOT NULL)";

    public static final String DELETE_MOST_RECENT_EVENTS_TABLE =
            "DROP TABLE IF EXISTS " + mostRecentEventsEntry.TABLE_NAME;

    public static final String DELETE_OLD_MOST_RECENT_EVENTS_DATA =
            "DELETE FROM " + mostRecentEventsEntry.TABLE_NAME;

    //define table columns
    public static class mostRecentEventsEntry implements BaseColumns {
        public static final String TABLE_NAME = "mostRecentEvent";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_VARIETY_EVENT_TITLE = "EventTitle";
        public static final String COLUMN_VARIETY_EVENT_DESCRIPTION = "varietyEventDescription";
    }
}
