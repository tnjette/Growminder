package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

/**
 * Created by tylerjette on 12/4/17.
 */

public class varietyEventsContract {
    //TODO: define this class, it will be the same as the recipe_steps class in the tutorial
    private varietyEventsContract(){}

    public static final String CREATE_VARIETY_EVENTS_TABLE =
            "CREATE TABLE " + varietyEventsEntry.TABLE_NAME + " ("+
                    varietyEventsEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL" +
                    varietyEventsEntry.COLUMN_VARIETY_EVENT_DATE + "TEXT NOT NULL" +
                    varietyEventsEntry.COLUMN_VARIETY_EVENT_DESCRIPTION + "TEXT NOT NULL)";


    public static final String DELETE_VARIETY_EVENTS_TABLE =
            "DROP TABLE IF EXISTS " + varietyEventsEntry.TABLE_NAME;

    public static final String DELETE_OLD_VARIETY_EVENTS_DATA =
            "DELETE FROM " + varietyEventsEntry.TABLE_NAME;

    //define table columns
    public static class varietyEventsEntry implements BaseColumns {
        public static final String TABLE_NAME = "variety_events";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_VARIETY_EVENT_DATE = "varietyEventDate";
        public static final String COLUMN_VARIETY_EVENT_DESCRIPTION = "varietyEventDescription";
    }
}
