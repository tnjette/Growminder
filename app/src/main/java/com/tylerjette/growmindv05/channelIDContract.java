package com.tylerjette.growmindv05;

import android.provider.BaseColumns;

public class channelIDContract {
    private channelIDContract(){}

    public static final String CREATE_CHANNEL_ID_TABLE =
            "CREATE TABLE " + channelIDEntry.TABLE_NAME + " ("+
                    channelIDEntry.COLUMN_CHANNEL_ID + " TEXT NOT NULL)";

    public static final String DELETE_MOST_RECENT_EVENTS_TABLE =
            "DROP TABLE IF EXISTS " + channelIDEntry.TABLE_NAME;

    public static final String DELETE_OLD_MOST_RECENT_EVENTS_DATA =
            "DELETE FROM " + channelIDEntry.TABLE_NAME;

    //define table columns
    public static class channelIDEntry implements BaseColumns {
        public static final String TABLE_NAME = "ChannelIDTable";
        public static final String COLUMN_CHANNEL_ID = "ChannelID";
    }
}
