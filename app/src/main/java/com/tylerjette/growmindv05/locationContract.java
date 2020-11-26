package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

/**
 * Created by tylerjette on 11/1/17.
 * DEFINES THE SCHEMA AND CONTRACT FOR THE DATABASE, which defines the tables and columns
 */

public final class locationContract {
    private locationContract(){}
    //this is private in order to avoid enabling a user to instantiate a constructor class unintentionally

    //create a string that corresponds to the onCreate statement in the SQLiteHelper class and utilizes the
    //constants defined below in the locationEntry class
    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + locationEntry.TABLE_NAME + " ( " +
                    locationEntry.COLUMN_ZIPCODE + " TEXT NOT NULL," +
                    locationEntry.COLUMN_PLACENAME + " TEXT NOT NULL," +
                    locationEntry.COLUMN_ZONE + " TEXT NOT NULL)";

    //create a String that corresponds to the delete statement in the SQLiteHelper class
    public static final String DELETE_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + locationEntry.TABLE_NAME;

    public static final String DELETE_OLD_LOCATION_DATA =
            "DELETE FROM " + locationEntry.TABLE_NAME;

    //inner class that defines the table columns
    public static class locationEntry implements BaseColumns{
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_ZIPCODE = "zipcode";
        public static final String COLUMN_PLACENAME = "placename";
        public static final String COLUMN_ZONE = "zone";
    }
}
