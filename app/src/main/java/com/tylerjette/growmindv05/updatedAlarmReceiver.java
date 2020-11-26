package com.tylerjette.growmindv05;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class updatedAlarmReceiver extends BroadcastReceiver {

    public static final int SDK_INT = android.os.Build.VERSION.SDK_INT;

    @Override
    public void onReceive(Context context, Intent intent) {

        /***here you would include the logic to check the database, and see if there are any active notifications with a date
         * object that compares to the current date.*/

        Calendar todayIs = Calendar.getInstance();
        int currentYear = todayIs.get(Calendar.YEAR);
        int currentMonth = todayIs.get(Calendar.MONTH);
        int currentDay = todayIs.get(Calendar.DATE);

        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        /***new updated version of the db query will include a query to the each of the generic calendar, the immediate calendar, and the arrested calendar
         * In the end, you will want an ArrayList with the most appropriate dates and events. First of all, all of the immediate dates will have to be configured
         * to include the YEAR of conception, meaning that in consecutive years, the year part of the date Object will be overridden.
         *
         * */

        ArrayList<String[]> arrestedEvents = new ArrayList<>();
        ArrayList<String[]> immediateEventsListFromDB = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> listOfMostRecentEventsByDate = new LinkedHashMap<>();
        ArrayList<String[]> genericEventsList = new ArrayList<>();
        ArrayList<String[]> finalListOfMostRecentEvents = new ArrayList<>();


        /**retieve the arrestedCalender events list*/
        String arrestedCalendarProjection[] = {
                arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_VARIETY_ID,
                arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_DATE,
                arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_TITLE
        };
        Cursor arrestedCursor = db.query(
                arrestedEventsCalendarContract.arrestedCalendarEntry.TABLE_NAME,
                arrestedCalendarProjection,
                null,
                null,
                null,
                null,
                null
        );

        while (arrestedCursor.moveToNext()) {
            String arrestedDate = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_DATE));
            String arrestedVarietyID = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_VARIETY_ID));
            String arrestedEventTitle = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_TITLE));
            String[] event = {arrestedVarietyID, arrestedDate, arrestedEventTitle};
            arrestedEvents.add(event);
        }
        arrestedCursor.close();

        /**retrieve the immediate events*/
        String immediateCalendarProjection[] = {
                immediateCalendarContract.immediateCalendarEntry.COLUMN_VARIETY_ID,
                immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DATE,
                immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_TITLE,
                immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DESCRIPTION
        };

        Cursor immediateCursor = db.query(
                immediateCalendarContract.immediateCalendarEntry.TABLE_NAME,
                immediateCalendarProjection,
                null,
                null,
                null,
                null,
                null
        );
        while (immediateCursor.moveToNext()) {
            String varietyID = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_VARIETY_ID));
            String dateObj = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DATE));
            String eventTitle = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_TITLE));
            String eventDesc = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DESCRIPTION));

            String[] event = {varietyID, dateObj, eventTitle, eventDesc};
            immediateEventsListFromDB.add(event);
        }

        immediateCursor.close();


        /***January, 2018: this section needs to accommodate the introduction of the validationYear object*/

        /**retrieve the generic events*/
        String genericCalendarProjection[] = {
                calendarContract.calendarEntry.COLUMN_VARIETY_ID,
                calendarContract.calendarEntry.COLUMN_EVENT_TITLE,
                calendarContract.calendarEntry.COLUMN_EVENT_DATE,
                /***added**/
                calendarContract.calendarEntry.COLUMN_VALIDATION_YEAR,

                calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION
        };

        Cursor cursor = db.query(
                calendarContract.calendarEntry.TABLE_NAME,
                genericCalendarProjection,
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            /**get the date object, and first determine if the date is the most recent in the list*/
            String dateObject = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DATE));

            /**added*/
            String validationYearStr = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_VALIDATION_YEAR));

            //String eventTitle = genericCursor.getString(genericCursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
            //String eventDesc = genericCursor.getString(genericCursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));
            String[] dateSplit = dateObject.split(",");
            int month = 0;
            int day = 0;
            int workableYear = 0;
            if (dateSplit.length == 2) {
                //meaning there is no secondary year specification
                workableYear = currentYear;
                month = Integer.parseInt(dateSplit[0]);
                day = Integer.parseInt(dateSplit[1]);
            }else if (dateSplit.length == 3){
                /**this needs to change in order to accommodate the validationYear assessment*/
                //the conditional clause should correspond to the validationYear str
                //so, set the date using the validation string, and then check it.
                int validationYearInt = Integer.parseInt(validationYearStr);
                if(validationYearInt <= currentYear){
                    //this is when the validationyear is actually valid
                    workableYear = currentYear;
                }else{
                    //the date is still in the future
                    workableYear = validationYearInt;
                }
                month = Integer.parseInt(dateSplit[1]);
                day = Integer.parseInt(dateSplit[2]);
            }
            String dateBuildStr = String.valueOf(workableYear) + "," + String.valueOf(month) + "," + String.valueOf(day);

            //logic for retreiving the most relevant dates
            Calendar eventCal = Calendar.getInstance();
            eventCal.set(workableYear, month -1, day);

            //long eventCalInMillis = eventCal.getTimeInMillis(); //let's see what this does...
            int checkYear = workableYear;
            int checkMonth = month -1;
            int checkDay = day;

            Boolean checkIfToday = false;
            if((checkYear == currentYear) && (checkMonth == currentMonth) && (checkDay == currentDay)){
                checkIfToday = true;
            }

            /**determine if the date in the db that corresponds to a particular event has is in the past or not*/
            if ((eventCal.compareTo(todayIs) < 0) || checkIfToday) {
                //meaning the event has passed, then it qualifies as a past event
                String varietyIDObj = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_VARIETY_ID));
                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
                String eventDesc = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));
                if (listOfMostRecentEventsByDate.containsKey(dateBuildStr)) {
                    /**if the date in the db matches the current list of most recent date found*/
                    listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                    String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                    genericEventsList.add(event);
                    //add event to
                } else {
                    if (listOfMostRecentEventsByDate.size() > 0) { //meaning there is a date in the linked hashmap
                        /**compare the two dates*/
                        for (String key : listOfMostRecentEventsByDate.keySet()) {
                            //should only be one, btw
                            String[] keySplit = key.split(",");
                            Calendar previousCal = Calendar.getInstance();
                            previousCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1])-1, Integer.valueOf(keySplit[2]));
                            /**COMPARE THE dates*/
                            if (eventCal.compareTo(previousCal) > 0) {
                                //meaning that the event calendar is more recent than the one previously, but is still in the past relative to today
                                listOfMostRecentEventsByDate.clear(); //then clear the hashmap and start over
                                listOfMostRecentEventsByDate.put(dateBuildStr, new LinkedHashMap<String, String>());
                                listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                                genericEventsList.clear();
                                String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                                genericEventsList.add(event);
                            } else {
                                //do nothing, the event is too obscure
                            }
                        }
                    } else {
                        listOfMostRecentEventsByDate.put(dateBuildStr, new LinkedHashMap<String, String>());
                        listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                        String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                        genericEventsList.add(event);
                    }
                }
            } else {
                //do nothing, date is not in the past
            }/***by the end of this if else statement, you should have all of the most recent notifications as a linkedhashmap to use*/
        }//end of looping through the list of possible generic events

        cursor.close();

        Boolean immediateIsMoreRecent = false;

        for (String key : listOfMostRecentEventsByDate.keySet()) {
            String[] keySplit = key.split(",");
            Calendar genericCal = Calendar.getInstance();
            genericCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1])-1, Integer.valueOf(keySplit[2]));

            //loop through the list of immediate events
            for (int i = 0; i < immediateEventsListFromDB.size(); i++) {
                String date = immediateEventsListFromDB.get(i)[1]; //hopefully this is the dateObj in the array
                String[] dateSplit = date.split(",");
                Calendar immediateEventsCal = Calendar.getInstance();
                immediateEventsCal.set(Integer.valueOf(dateSplit[0]), Integer.valueOf(dateSplit[1])-1, Integer.valueOf(dateSplit[2]));
                if (immediateEventsCal.compareTo(genericCal) > 0) {
                    //the immediate events Calendar is more recent than the generic calendar
                    immediateIsMoreRecent = true;
                } else {
                    //do nothing, the immediate dates are irrelevant at this point in time.
                }
            }
        }

        if (immediateIsMoreRecent) {
            /**basically, replace the entire list with the immediate list*/
            //meaning that the immediate events are more recent than the generic events.
            //then the listOfMostRecentEventsByDate can be scrapped altogether
            listOfMostRecentEventsByDate.clear();
            genericEventsList.clear();

            /**the finalListOfMostRecentEvents list will then become the  immediateEventList at this point*/
            finalListOfMostRecentEvents = immediateEventsListFromDB;
            /**and that's all*/

        } else {
            /**here the immediate event list is obsolete, and you have to start checking the most recent events list against the arrested list*/

            for (int i = 0; i < genericEventsList.size(); i++) {
                for (int k = 0; k < arrestedEvents.size(); k++) {
                    if ((genericEventsList.get(i)[0].equals(arrestedEvents.get(k)[0]))
                            && (genericEventsList.get(i)[1].equals(arrestedEvents.get(k)[1]))
                            && (genericEventsList.get(i)[2].equals(arrestedEvents.get(k)[2]))
                            ) {
                        //meaning there is a match with the event in the mostrecentevents list and the arrested events list
                        genericEventsList.remove(i);
                        //genericEventsList.remove(genericEventsList.get(i));
                        i = i - 1; //reset the index to return to the index from before since index at i was removed.
                        //then repeat
                    } else {
                        //do nothing because there is no match
                    }
                }
            }

            finalListOfMostRecentEvents = genericEventsList; //I think.
        }

        /**at this point, you should be able to access the final list, whether it is composed of immediate events, or generic events*/
        /**first check that the dates in the array of events are the same*/
        Boolean allTheSame = true;
        for(int j = 0; j < finalListOfMostRecentEvents.size(); j++){
            if(j > 0){
                String date1 = finalListOfMostRecentEvents.get(j)[1];
                String date2 = finalListOfMostRecentEvents.get(j-1)[1];

                if(date1.equals(date2)){
                    //do nothing
                }else{
                    //DO SOMEthinG!
                    allTheSame = false;
                }
            }
        }

        String date = "";
        if(allTheSame){
            //then retrieve the date String
            if(finalListOfMostRecentEvents.size() > 0) {
                date = finalListOfMostRecentEvents.get(0)[1];


                /**adding*/
                /**then check if the date is the same as today's date.*/

                String[] dateSplit = date.split(",");

                Calendar eventDates = Calendar.getInstance();
                eventDates.set(Integer.valueOf(dateSplit[0]), Integer.valueOf(dateSplit[1])-1, Integer.valueOf(dateSplit[2]));

                /***this is what it all comes down to.*/
                //if(todayIs.compareTo(eventDates) == 0){

                if((todayIs.get(Calendar.YEAR) == eventDates.get(Calendar.YEAR)) && (todayIs.get(Calendar.MONTH) == eventDates.get(Calendar.MONTH)) &&
                        (todayIs.get(Calendar.DATE) == eventDates.get(Calendar.DATE))){




                    /***then RUN THE NOTIFICATION!*/

                    JSONObject readableNamesFile = new JSONObject();
                    try {
                        readableNamesFile = new JSONObject(loadReadableName(context).trim());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }


                    //ArrayList mostRecentEventList = new ArrayList();
                    String finalNotificationList="";

                    /**get the list of the most recent notifications from above*/
                    for (int h = 0; h < finalListOfMostRecentEvents.size(); h++) {
                        String baselineVarietyID = finalListOfMostRecentEvents.get(h)[0].replaceAll("[0-9]", "");
                        //get the readable name from the readableNameFile
                        String eventTitle = finalListOfMostRecentEvents.get(h)[2];
                        String eventDetail = finalListOfMostRecentEvents.get(h)[3];
                        String readableName = "";
                        try {
                            readableName = readableNamesFile.getJSONObject("readableNames").getString(baselineVarietyID.toLowerCase());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        int varietyImage;
                        switch (baselineVarietyID.toLowerCase()) {
                            case "artichoke":
                                varietyImage = R.drawable.artichoke;
                                break;
                            case "arugula":
                                varietyImage = R.drawable.arugula;
                                break;
                            case "asparagus":
                                varietyImage = R.drawable.asparagus;
                                break;
                            case "basil":
                                varietyImage = R.drawable.basil;
                                break;
                            case "beanssnap":
                                varietyImage = R.drawable.beanssnap;
                                break;
                            case "broccoli":
                                varietyImage = R.drawable.broccoli;
                                break;
                            case "brusselssprouts":
                                varietyImage = R.drawable.brusselssprouts;
                                break;
                            case "cabbage":
                                varietyImage = R.drawable.cabbage;
                                break;
                            case "cauliflower":
                                varietyImage = R.drawable.cauliflower;
                                break;
                            case "chard":
                                varietyImage = R.drawable.chard;
                                break;
                            case "chinesecabbage":
                                varietyImage = R.drawable.chinesecabbage;
                                break;
                            case "chives":
                                varietyImage = R.drawable.chives;
                                break;
                            case "cilantro":
                                varietyImage = R.drawable.cilantro;
                                break;
                            case "collardgreens":
                                varietyImage = R.drawable.collardgreens;
                                break;
                            case "cornsweet":
                                varietyImage = R.drawable.corn;
                                break;
                            case "cucumberspickle":
                                varietyImage = R.drawable.cucumberpickle;
                                break;
                            case "eggplants":
                                varietyImage = R.drawable.eggplant;
                                break;
                            case "garlic":
                                varietyImage = R.drawable.garlic;
                                break;
                            case "kale":
                                varietyImage = R.drawable.kale;
                                break;
                            case "lettucehead":
                                varietyImage = R.drawable.lettucehead;
                                break;
                            case "lettuceleaf":
                                varietyImage = R.drawable.lettuceleaf;
                                break;
                            case "onions":
                                varietyImage = R.drawable.onion;
                                break;
                            case "parsley":
                                varietyImage = R.drawable.parsley;
                                break;
                            case "peas":
                                varietyImage = R.drawable.peas;
                                break;
                            case "peppershot":
                                varietyImage = R.drawable.peppershot;
                                break;
                            case "pepperssweet":
                                varietyImage = R.drawable.pepperssweet;
                                break;
                            case "potatowhite":
                                varietyImage = R.drawable.potatowhite;
                                break;
                            case "pumpkin":
                                varietyImage = R.drawable.pumpkin;
                                break;
                            case "radish":
                                varietyImage = R.drawable.radish;
                                break;
                            case "rhubarb":
                                varietyImage = R.drawable.rhubarb;
                                break;
                            case "spinach":
                                varietyImage = R.drawable.spinach;
                                break;
                            case "squashsummer":
                                varietyImage = R.drawable.squashsummer;
                                break;
                            case "squashwinter":
                                varietyImage = R.drawable.squashwinter;
                                break;
                            case "tomatillos":
                                varietyImage = R.drawable.tomatillo;
                                break;
                            case "tomatoes":
                                varietyImage = R.drawable.tomato;
                                break;
                            case "watermelon":
                                varietyImage = R.drawable.watermelon;
                                break;
                            case "zuccini":
                                varietyImage = R.drawable.zuccini;
                                break;
                            default:
                                varietyImage = R.drawable.cake_1;
                                break;
                        }

                        String formattedEventTitle = getReadableEventTitle(eventTitle);
                        //notificationEvent eventByVar = new notificationEvent(readableName, eventTitle, eventDetail, varietyImage);
                        //String eventString = key + " : " + currentNotificationEventList.get(key);
                        //mostRecentEventList.add(eventByVar);
                        finalNotificationList = finalNotificationList + readableName + " : " + formattedEventTitle + "\n";
                    }//end of looping through the final list o

                    /**make call to setOneTimrAlarm*/
                    Long nowInMillis = todayIs.getTimeInMillis();
                    int notification_ID = 42;
                    //setOneTimeAlarm(context, notification_ID, nowInMillis, mostRecentEventList);
                    setOneTimeAlarm(context, notification_ID, nowInMillis, finalNotificationList);


                } else {
                    //do nothing
                }
            }else {
                //do nothing
            }
        }
    }

    public String loadReadableName(Context context){
        String json = null;
        try{
            InputStream botanicalNamesFile = context.getAssets().open("readableNamesByVariety.json");
            int size = botanicalNamesFile.available();
            byte[] buffer = new byte[size];
            botanicalNamesFile.read(buffer);
            botanicalNamesFile.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //public void setOneTimeAlarm(Context context, int notification_ID, Long fireTheRig, ArrayList eventArray) {
    public void setOneTimeAlarm(Context context, int notification_ID, Long fireTheRig, String events){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //retrieve the notification channel ID
            String channel_id = context.getResources().getString(R.string.notification_channel_id);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("New Growminder notification")
                    .setContentText(events)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(events))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);


            /**added to allow for navigation to the activity from the notification*/
                    Intent notifyIntent = new Intent(context, Dashboard.class);
                    // Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    mBuilder.setContentIntent(notifyPendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(notification_ID, mBuilder.build());

        } else {

            /**this is the original version of the notification setup*/
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Intent intent = new Intent(context, GrowmindNotificationReceiver.class);


            /***
             *
             *
             * todo: look into this line further, since the updated Add Varieties Activity is obsolete...so why is this a parameter for the Intent??
             *
             *
             */

            Intent intent = new Intent(context, updatedAddVarietiesActivity.class);



            //intent.putExtra("Growmind Notification", "You have a new notification from your Growmind App!");
            //intent.putStringArrayListExtra("Current Notifications:", eventArray);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification_ID, intent, PendingIntent.FLAG_ONE_SHOT);

            Intent notificationIntent = new Intent(context, Dashboard.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //create a taskStackBuilder for the backStack when the notification is opened.
            //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //stackBuilder.addNextIntentWithParentStack(notificationIntent);

            PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            //PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT, 0);
            //String notificationBody = "";
            //for(int i = 0; i < eventArray.size(); i++){
            //    String ev = eventArray.get(i).toString();
            //    notificationBody = notificationBody + ev + "\n";
            //}

            //RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.remote_notification_view);
            //RemoteViews rvExpanded = new RemoteViews(context.getPackageName(), R.layout.remote_notification_view_expanded);

            Notification.Builder builder = new Notification.Builder(context);

            if (SDK_INT >= 24) {
                builder.setSmallIcon(R.mipmap.ic_launcher_round)
                        .setTicker("New Growminder notification")
                        .setContentTitle("New Growminder notification")
                        .setStyle(new Notification.BigTextStyle().bigText(events))
                        .setContentIntent(notificationPendingIntent)
                        .setAutoCancel(true);
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher_round)
                        .setTicker("New Growminder notification")
                        .setContentTitle("New Growminder notification")
                        .setStyle(new Notification.BigTextStyle().bigText(events))
                        .setContentIntent(notificationPendingIntent)
                        .setAutoCancel(true);
            }

        /*else if (Build.VERSION.SDK_INT < 24){
            builder.setSmallIcon(R.mipmap.ic_launcher_round_old)
                    .setTicker("New Growminder Notification")
                    .setContentTitle("New Growminder Notification")
                    .setContentText(notificationBody)
                    //setStyle(new Notification.BigTextStyle().bigText(eventArray.toString()))
                    //.setContentIntent(contentIntent)
                    .setContentIntent(notificationPendingIntent)
                    .setAutoCancel(true);
        }*/

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());
            am.set(AlarmManager.RTC_WAKEUP, fireTheRig, pendingIntent);

        }



    }
    public String getReadableEventTitle (String eventTitle) {
        String formattedEventTitle = "";
        switch (eventTitle){
            case "startInside" :
                formattedEventTitle = "Indoor seeding";
                break;
            case "startHardenOff" :
                formattedEventTitle = "Hardening off seedlings";
                break;
            case "startOutside" :
                formattedEventTitle = "Transplanting";
                break;
            case "startOutsideCovered" :
                formattedEventTitle = "Transplanting";
                break;
            case "stopOutside" :
                formattedEventTitle = "End planting";
                break;
            case "startHarvest" :
                formattedEventTitle = "Harvest";
                break;
            case "directOutside" :
                formattedEventTitle = "Planting";
                break;
            case "directOutsideCovered" :
                formattedEventTitle = "Planting";
                break;
            case "firstMulch" :
                formattedEventTitle = "Mulching";
                break;
            case "transplantToLargerContainer":
                formattedEventTitle = "Transplant";
                break;
            case "fertilizePlantsFirstTime" :
                formattedEventTitle = "Fertilize";
                break;
            case "fertilizePlantsSecondTime" :
                formattedEventTitle = "Fertilize, again";
                break;
            case "firstThin":
                formattedEventTitle = "Thin seedlings";
                break;
            case "secondMulch":
                formattedEventTitle = "Mulch, again";
                break;
            case "secondThin":
                formattedEventTitle = "Thin seedlings, again";
                break;
            case "cutBack" :
                formattedEventTitle = "Trim";
                break;
            case "firstBlanch" :
                formattedEventTitle = "Blanch";
                break;
            case "secondBlanch" :
                formattedEventTitle = "Blanch, again";
                break;
            case "changeWaterCycle":
                formattedEventTitle = "Change water cycle";
                break;

            //second round
            case "startInside2" :
                formattedEventTitle = "Indoor seeding (Fall cycle)";
                break;
            case "startHardenOff2" :
                formattedEventTitle = "Hardening off seedlings (Fall cycle)";
                break;
            case "startOutside2" :
                formattedEventTitle = "Transplanting (Fall cycle)";
                break;
            case "startOutside2Covered" :
                formattedEventTitle = "Transplanting (Fall cycle)";
                break;
            case "stopOutside2" :
                formattedEventTitle = "End planting (Fall cycle)";
                break;
            case "startHarvest2" :
                formattedEventTitle = "Harvest (Fall Cycle)";
                break;
            case "directOutside2" :
                formattedEventTitle = "Planting (Fall Cycle)";
                break;
            case "directOutside2Covered" :
                formattedEventTitle = "Planting (Fall Cycle)";
                break;
            case "firstMulch2" :
                formattedEventTitle = "Mulching (Fall Cycle)";
                break;
            case "transplantToLargerContainer2":
                formattedEventTitle = "Transplant (Fall Cycle)";
                break;
            case "fertilizePlantsFirstTime2" :
                formattedEventTitle = "Fertilize (Fall Cycle)";
                break;
            case "fertilizePlantsSecondTime2" :
                formattedEventTitle = "Fertilize, again (Fall Cycle)";
                break;
            case "firstThin2":
                formattedEventTitle = "Thin seedlings (Fall Cycle)";
                break;
            case "secondMulch2":
                formattedEventTitle = "Mulch, again (Fall Cycle)";
                break;
            case "secondThin2":
                formattedEventTitle = "Thin seedlings, again (Fall Cycle)";
                break;
            case "cutBack2" :
                formattedEventTitle = "Trim (Fall Cycle)";
                break;
            case "firstBlanch2" :
                formattedEventTitle = "Blanch (Fall Cycle)";
                break;
            case "secondBlanch2" :
                formattedEventTitle = "Blanch, again (Fall Cycle)";
                break;
            case "changeWaterCycle2":
                formattedEventTitle = "Change water cycle (Fall Cycle)";
                break;
            case "EXPIRED":
                formattedEventTitle = "Planting window has passed";
                break;
            default :
                formattedEventTitle = "Planting window passed";
                break;
        }
        return formattedEventTitle;
    }
}