package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

/**
 * Created by tylerjette on 3/27/18.
 */
public class userVisitationContract {
    //TODO: define this class, it will be the same as the recipe_steps class in the tutorial
    private userVisitationContract(){}

    public static final String CREATE_USER_VISITATION_TABLE =
            "CREATE TABLE " + visitationEntry.TABLE_NAME + " ("+
                    visitationEntry.COLUMN_USER_VISITS + " TEXT NOT NULL)";


    public static final String DELETE_USER_VISITATION_TABLE =
            "DROP TABLE IF EXISTS " + visitationEntry.TABLE_NAME;

    public static final String DELETE_OLD_USER_VISITS =
            "DELETE FROM " + visitationEntry.TABLE_NAME;

    //define table columns
    public static class visitationEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_visits";
        public static final String COLUMN_USER_VISITS = "number_of_visits";
    }
}
