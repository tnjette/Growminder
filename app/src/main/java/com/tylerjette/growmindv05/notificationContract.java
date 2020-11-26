package com.tylerjette.growmindv05;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by tylerjette on 12/16/17.
 */

public class notificationContract {
    private notificationContract(){}
    //public static Calendar cal = Calendar.getInstance();
    public static final String CREATE_NOTIFICATION_TABLE =
            "CREATE TABLE " + notificationRegistrationEntry.TABLE_NAME + " ( " +
                    notificationRegistrationEntry.COLUMN_NOTIFICATION_ID + " TEXT NOT NULL," +
                    notificationRegistrationEntry.COLUMN_NOTIFICATION_SET_DATE + " TEXT NOT NULL," +
                    notificationRegistrationEntry.COLUMN_NOTIFICATION_TRIGGER_DATE + " TEXT NOT NULL,"+
                    notificationRegistrationEntry.COLUMN_NOTIFICATION_STATUS + " TEXT NOT NULL," +
                    notificationRegistrationEntry.COLUMN_NOTIFICATION_CANCEL_DATE + " TEXT NOT NULL)";
                    //notificationEntry.COLUMN_NOTIFICATION_CANCEL_DATE + " )";

    public static final String DELETE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + notificationRegistrationEntry.TABLE_NAME;

    public static final String DELETE_OLD_NOTIFICATION_DATA =
            "DELETE FROM " + notificationRegistrationEntry.TABLE_NAME;

    //inner class that defines the table columns
    public static class notificationRegistrationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notificationRegistration";
        public static final String COLUMN_NOTIFICATION_ID = "notificationID";
        public static final String COLUMN_NOTIFICATION_SET_DATE = "setDate";
        public static final String COLUMN_NOTIFICATION_TRIGGER_DATE = "triggerDate";
        public static final String COLUMN_NOTIFICATION_STATUS = "status";
        public static final String COLUMN_NOTIFICATION_CANCEL_DATE = "cancelDate";
    }

    public static final String UNREGISTER_OLD_NOTIFICATIONS =
            "UPDATE " + notificationRegistrationEntry.TABLE_NAME +
                    " SET " + notificationRegistrationEntry.COLUMN_NOTIFICATION_STATUS + " = " + " 'CANCELLED'," +
                            notificationRegistrationEntry.COLUMN_NOTIFICATION_CANCEL_DATE + " = date('now') "+
                    " WHERE " + notificationRegistrationEntry.COLUMN_NOTIFICATION_STATUS + " = 'ACTIVE'";

    /**ACTIVE NOTIFICATIONS TABLES*/

    /**create Active notifications*/
    public static final String CREATE_ACTIVE_NOTIFICATIONS_TABLE =
            "CREATE TABLE " + activeNotificationEntry.TABLE_NAME + " ( " +
                    activeNotificationEntry.COLUMN_NOTIFICATION_ID + " TEXT NOT NULL," +
                    activeNotificationEntry.COLUMN_NOTIFICATION_DATE + " TEXT NOT NULL," +
                    activeNotificationEntry.COLUMN_NOTIFICATION_TRIGGER + " TEXT NOT NULL," +
                    activeNotificationEntry.COLUMN_NOTIFICATION_BODY + " TEXT NOT NULL)";

    /**this is going to involve a little trick in which we will take all of the information in the corresponding "hashmap" and turn it into a long
     * -ass string with a delimiter, for example "//" which will then be used to parse the string into the appropriate comma-separated-values or as
     * key value pairs, more likely.*/

    public static class activeNotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "activeNotifications";
        public static final String COLUMN_NOTIFICATION_ID = "notificationID";
        public static final String COLUMN_NOTIFICATION_DATE = "notificationDate";
        public static final String COLUMN_NOTIFICATION_TRIGGER = "notificationTrigger";
        public static final String COLUMN_NOTIFICATION_BODY = "notificationBody";
        //public static final LinkedHashMap<String, String> COLUMN_NOTIFICATION_BODY= new LinkedHashMap<>();
        //public static final String[] COLUMN_NOTIFICATION_BODY = new String[0];
    }

    public static final String DELETE_ACTIVE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + activeNotificationEntry.TABLE_NAME;

    public static final String DELETE_DEACTIVATED_NOTIFICATIONS =
            "DELETE FROM " + activeNotificationEntry.TABLE_NAME;
}
