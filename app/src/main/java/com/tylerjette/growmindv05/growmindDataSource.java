package com.tylerjette.growmindv05;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tylerjette on 11/8/17.
 */
public class growmindDataSource{
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    growmindDataSource(Context context){
            this.dbHelper = new SQLiteHelper(context);
    }

    public void open(){
            this.database = dbHelper.getWritableDatabase();
    }

    public void close(){
            dbHelper.close();
        }

    public void createLocation(String zipCode, String zone, String placeName){
        database.execSQL(locationContract.DELETE_OLD_LOCATION_DATA);
        ContentValues values = new ContentValues();
        values.put(locationContract.locationEntry.COLUMN_ZIPCODE, zipCode);
        values.put(locationContract.locationEntry.COLUMN_PLACENAME, placeName);
        values.put(locationContract.locationEntry.COLUMN_ZONE, zone);
        database.insert(locationContract.locationEntry.TABLE_NAME, null, values);
    }

    public void createVarietyList(ArrayList checkedVarietiesList){
        database.execSQL(varietyContract.DELETE_OLD_VARIETY_DATA);
        ContentValues values = new ContentValues();
        //loop through the String that gets passed
        for(int i = 0; i < checkedVarietiesList.size(); i++){
            String varID = checkedVarietiesList.get(i).toString();
            values.put(varietyContract.varietyEntry.COLUMN_VARIETY_ID, varID);
            database.insert(varietyContract.varietyEntry.TABLE_NAME, null, values);
        }
    }

    public void createGenericCalendar(List<String[]> listOfVarietyEvents){
        database.execSQL(calendarContract.DELETE_OLD_CALENDAR_DATA);
        //parse the List passed to this method.
        for(int i = 0; i < listOfVarietyEvents.size(); i++){
            ContentValues values = new ContentValues();
            values.put(calendarContract.calendarEntry.COLUMN_VARIETY_ID, listOfVarietyEvents.get(i)[0]);
            values.put(calendarContract.calendarEntry.COLUMN_EVENT_DATE, listOfVarietyEvents.get(i)[1]);
            values.put(calendarContract.calendarEntry.COLUMN_VALIDATION_YEAR, listOfVarietyEvents.get(i)[2]);
            values.put(calendarContract.calendarEntry.COLUMN_EVENT_TITLE, listOfVarietyEvents.get(i)[3]);
            values.put(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION, listOfVarietyEvents.get(i)[4]);
            database.insert(calendarContract.calendarEntry.TABLE_NAME, null, values);
        }
    }

    public void createImmediateEventsCalendar(List<String[]> listOfVarietyEvents){
        database.execSQL(immediateCalendarContract.DELETE_OLD_IMMEDIATE_CALENDAR_DATA);
        for(int i = 0; i < listOfVarietyEvents.size(); i++){
            ContentValues values = new ContentValues();
            values.put(immediateCalendarContract.immediateCalendarEntry.COLUMN_VARIETY_ID, listOfVarietyEvents.get(i)[0]);
            values.put(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DATE, listOfVarietyEvents.get(i)[1]);
            values.put(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_TITLE, listOfVarietyEvents.get(i)[2]);
            values.put(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DESCRIPTION, listOfVarietyEvents.get(i)[3]);
            database.insert(immediateCalendarContract.immediateCalendarEntry.TABLE_NAME, null, values);
        }
    }

    public void createArrestedEventsCalendar(List<String[]> listOfVarietyEvents){
        database.execSQL(arrestedEventsCalendarContract.DELETE_OLD_ARRESTED_CALENDAR_DATA);
        for(int i = 0; i < listOfVarietyEvents.size(); i++){
            ContentValues values = new ContentValues();
            values.put(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_VARIETY_ID, listOfVarietyEvents.get(i)[0]);
            values.put(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_DATE, listOfVarietyEvents.get(i)[1]);
            values.put(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_TITLE, listOfVarietyEvents.get(i)[2]);
            database.insert(arrestedEventsCalendarContract.arrestedCalendarEntry.TABLE_NAME, null, values);
        }
    }

    /*public void createNotificationRegistration(String notification_id, String notification_set_date,String notification_trigger_date, String notification_status, String notification_cancel_date){
            //there will be no delete function for the notificationTable, since the whole point is to keep track of all the
            //notifications that pass through the system.
            ContentValues values = new ContentValues();
            values.put(notificationContract.notificationRegistrationEntry.COLUMN_NOTIFICATION_ID, notification_id);
            values.put(notificationContract.notificationRegistrationEntry.COLUMN_NOTIFICATION_SET_DATE, notification_set_date);
            values.put(notificationContract.notificationRegistrationEntry.COLUMN_NOTIFICATION_TRIGGER_DATE,notification_trigger_date);
            values.put(notificationContract.notificationRegistrationEntry.COLUMN_NOTIFICATION_STATUS, notification_status);
            values.put(notificationContract.notificationRegistrationEntry.COLUMN_NOTIFICATION_CANCEL_DATE, notification_cancel_date);
            database.insert(notificationContract.notificationRegistrationEntry.TABLE_NAME,  null, values);
    }*/
    public void updateUserVisitationTable(int visits){
        int visitsUpdate = visits + 1;
        String convertToStr = String.valueOf(visitsUpdate);
        database.execSQL(userVisitationContract.DELETE_OLD_USER_VISITS);
        ContentValues values = new ContentValues();
        values.put(userVisitationContract.visitationEntry.COLUMN_USER_VISITS, convertToStr);
        database.insert(userVisitationContract.visitationEntry.TABLE_NAME, null, values);
    }

    public void createUserVisitationTable(int visits){
        int visitsUpdate = visits + 1;
        String convertToStr = String.valueOf(visitsUpdate);
        ContentValues values = new ContentValues();
        values.put(userVisitationContract.visitationEntry.COLUMN_USER_VISITS, convertToStr);
        database.insert(userVisitationContract.visitationEntry.TABLE_NAME, null, values);
    }

    public void createChannelIDTable(String channel_ID){
        ContentValues values = new ContentValues();
        values.put(channelIDContract.channelIDEntry.COLUMN_CHANNEL_ID, channel_ID);
        database.insert(channelIDContract.channelIDEntry.TABLE_NAME, null, values);
    }
}
