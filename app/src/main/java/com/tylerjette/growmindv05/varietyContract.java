package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

import java.sql.Blob;

/**
 * Created by tylerjette on 11/9/17.
 */

public final class varietyContract {
    private varietyContract(){}

    public static final String CREATE_VARIETY_TABLE =
            "CREATE TABLE " + varietyEntry.TABLE_NAME + " ("+
                    varietyEntry.COLUMN_VARIETY_ID + " TEXT NOT NULL)";

            //todo: create step table as well?


    public static final String DELETE_VARIETY_TABLE =
            "DROP TABLE IF EXISTS " + varietyEntry.TABLE_NAME;

    public static final String DELETE_OLD_VARIETY_DATA =
            "DELETE FROM " + varietyEntry.TABLE_NAME;

    //define table columns
    public static class varietyEntry implements BaseColumns{
        public static final String TABLE_NAME = "varieties";
        public static final String COLUMN_VARIETY_ID = "varietyID";
        public static final String COLUMN_VARIETY_NAME = "varietyName";
    }
}
