package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by tylerjette on 2/25/18.
 */

/**
 * this activity will be opened when the user clicks on the notification that has sent them an alarm, and when they open up the
 * "most recent notification" tab in the app menu bar. The content will be loaded by looking at the activeNotifications table of
 * the database, and then by retrieving the most recently passed date of the notification
 * */

public class upcomingNotificationsActivity extends AppCompatActivity{

    //private static final String TAG = upcomingNotificationsActivity.class.getSimpleName();
    //private RecyclerView notificationRecycler; //this will carry the UI with each of the separate notifications by variety(((OLD, pre-migration)))
    //private dashboardAdapter nAdapter; //haven't made this yet ((OLD, pre-migration))
    Context context = this;
    private RecyclerView upcomingNotificationsRecyclerView;
    private upcomingNotificationsAdapter dashAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /**new UI from dashboard*/
        setContentView(R.layout.activity_upcoming_notifications);

        /**set UI compatible for landscape config*/
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout toolbarLinearLayout = findViewById(R.id.upcoming_notification_activity_toolbarLayout);
            toolbarLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            toolbarLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

            TextView title = findViewById(R.id.upcoming_notification_activity_title);
            TextView subtitle = findViewById(R.id.upcoming_notification_activity_subtitle);

            LinearLayout.MarginLayoutParams titleParams = new LinearLayout.LayoutParams(title.getLayoutParams());
            LinearLayout.MarginLayoutParams subtitleParams = new LinearLayout.LayoutParams(subtitle.getLayoutParams());

            titleParams.setMargins(0,0,0,0);
            subtitleParams.setMargins(32,3,0,0);

            titleParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            subtitleParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;

            title.setLayoutParams(titleParams);
            subtitle.setLayoutParams(subtitleParams);

        }

        Calendar todayIs = Calendar.getInstance();
        int currentYear = todayIs.get(Calendar.YEAR);
        int currentMonth = todayIs.get(Calendar.MONTH);
        int currentDay = todayIs.get(Calendar.DATE);

        /**migrated from dashboard*/
        List<varietyCal> varietyCalList = new ArrayList<varietyCal>();

        SQLiteHelper helper = SQLiteHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] locationProjection = {locationContract.locationEntry.COLUMN_ZONE};
        String locationSelection = locationContract.locationEntry.COLUMN_ZONE;
        //String[] varietyProjection = {varietyContract.varietyEntry.COLUMN_VARIETY_ID};

        //string parameters for retrieval of the activeNotifications table
        /*String[] ANProjection = {notificationContract.activeNotificationEntry.COLUMN_NOTIFICATION_TRIGGER,
            notificationContract.activeNotificationEntry.COLUMN_NOTIFICATION_BODY,
                notificationContract.activeNotificationEntry.COLUMN_NOTIFICATION_DATE
        };*/
        //String upcomingNotification[] = new String[3];
        //int triggerValidator = 0;

        //String readableDateStr = "";

        //set up an int value of a currentTime in millis to be compared against the notifications in the db
        //Calendar now = Calendar.getInstance();
        //Long nowLong = now.getTimeInMillis();
        //Integer nowIntVal = Integer.valueOf(nowLong.intValue());

        //Log.d(TAG, "now as an Integer = " + nowIntVal);

        ArrayList<String[]> arrestedEvents = new ArrayList<>();
        ArrayList<String[]> immediateEventsListFromDB = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> listOfUpcomingEventsByDate = new LinkedHashMap<>();
        ArrayList<String[]> genericEventsList = new ArrayList<>();
        ArrayList<String[]> filteredGenericList = new ArrayList<>();
        ArrayList<String[]> finalListOfUpcomingEvents = new ArrayList<>();

        Boolean hasLocation = false;
        Boolean hasVarieties = false;

        //String userZone = "";
        try {

            /**query the location table*/
            Cursor locationCursor = db.query(
                    locationContract.locationEntry.TABLE_NAME,
                    locationProjection,
                    locationSelection,
                    null,
                    null,
                    null,
                    null
            );
            while (locationCursor.moveToNext()) {
                hasLocation = true;
                //userZone = locationCursor.getString(
                //        locationCursor.getColumnIndexOrThrow(locationContract.locationEntry.COLUMN_ZONE)
                //);
            }

            locationCursor.close();

            /**query the arrested events table*/
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

            /**query the immediate events table*/
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


            /**query the generic events*/
            String genericCalendarProjection[] = {
                    calendarContract.calendarEntry.COLUMN_VARIETY_ID,
                    calendarContract.calendarEntry.COLUMN_EVENT_TITLE,
                    calendarContract.calendarEntry.COLUMN_EVENT_DATE,
                    calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION
            };

            Cursor cursor = db.query(
                    calendarContract.calendarEntry.TABLE_NAME,
                    genericCalendarProjection,
                    null, null, null, null, null
            );


            /*******this needs to be refactored***/
            while (cursor.moveToNext()) {
                hasVarieties = true;
                /**get the date object, and first determine if the date is the most recent in the list*/
                String varietyIDObj = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_VARIETY_ID));
                String dateObject = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DATE));
                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
                String eventDesc = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));

                //listOfUpcomingEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                String[] event = {varietyIDObj, dateObject, eventTitle, eventDesc};
                genericEventsList.add(event);

            }
            cursor.close();
        }catch(SQLException ex){

        }

        for(int i =0; i < genericEventsList.size(); i++){

                String varietyIDObj = genericEventsList.get(i)[0];
                String dateObject = genericEventsList.get(i)[1];
                String eventTitle = genericEventsList.get(i)[2];
                String eventDesc = genericEventsList.get(i)[3];

                String[] dateSplit = dateObject.split(",");
                //Log.d(TAG, "Here is the date pulled from the db : " + dateObject);
                int month = 0;
                int day = 0;
                int workableYear = 0;
                if (dateSplit.length == 2) {
                    //meaning there is no secondary year specification
                    workableYear = currentYear;
                    month = Integer.parseInt(dateSplit[0]);
                    day = Integer.parseInt(dateSplit[1]);
                } else if (dateSplit.length == 3) {
                    int adjustedYear = 0;
                    if (dateSplit[0].equals("n")) {
                        adjustedYear = currentYear + 1;
                    } else if (dateSplit[0].equals("nn")) {
                        adjustedYear = currentYear + 2;
                    } else if (dateSplit[0].equals("nnn")) {
                        adjustedYear = currentYear + 3;
                    } else if (dateSplit[0].equals("nnnn")) {
                        adjustedYear = currentYear + 4;
                    } else if (dateSplit[0].equals("nnnnn")) {
                        adjustedYear = currentYear + 5;
                    }
                    workableYear = adjustedYear;
                    month = Integer.parseInt(dateSplit[1]); //this needs to be + 1
                    day = Integer.parseInt(dateSplit[2]);
                }

                String dateBuildStr = String.valueOf(workableYear) + "," + String.valueOf(month) + "," + String.valueOf(day);

                //Log.d(TAG, "year : " + String.valueOf(workableYear));
                //Log.d(TAG, "month : " + String.valueOf(month));
                //Log.d(TAG, "day : " + String.valueOf(day));

                //logic for retreiving the most relevant dates
                Calendar eventCal = Calendar.getInstance();
                eventCal.set(workableYear, month-1, day); //added -1
                //month is on 0 base system

                int checkYear = workableYear;
                int checkMonth = month -1;
                int checkDay = day;

                Boolean checkIfToday = false;
                if((checkYear == currentYear) && (checkMonth == currentMonth) && (checkDay == currentDay)){
                    checkIfToday = true;
                }

                if ((eventCal.compareTo(todayIs) > 0) && (!checkIfToday)) {
                   if (listOfUpcomingEventsByDate.containsKey(dateBuildStr)) {
                        //Log.d(TAG, "list Contains KEY (which is dateBuildStr Object) = " + dateBuildStr + " therefore, only going to add this event to the existing Date Object.");
                        //**if the date in the db matches the current list of most recent date found//
                        listOfUpcomingEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                        String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                        filteredGenericList.add(event);
                        //add event to
                    } else {
                        if (listOfUpcomingEventsByDate.size() > 0) { //meaning there is a date in the linked hashmap
                            //Log.d(TAG, "THERE IS ANOTHER DATE IN THE LIST, NEEDS TO BE COMPARED to the date in question, which is : " + dateBuildStr);
                            //**compare the two dates
                            for (String key : listOfUpcomingEventsByDate.keySet()) {
                                //Log.d(TAG, "retrieving the Key in order to compare, Key : " + key);
                                //should only be one, btw
                                String[] keySplit = key.split(",");
                                Calendar previousCal = Calendar.getInstance();
                                previousCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1]) -1, Integer.valueOf(keySplit[2]));
                                //**COMPARE THE dates
                                if (eventCal.compareTo(previousCal) < 0) {
                                    //Log.d(TAG, eventCal.getTime().toString() + " is before " + previousCal.getTime().toString());
                                    //Log.d(TAG, "dateBuild String = " + dateBuildStr);
                                    //meaning that the eventCal obj preceeds the one previously, but is still in the Future relative to today
                                    listOfUpcomingEventsByDate.clear(); //then clear the hashmap and start over
                                    listOfUpcomingEventsByDate.put(dateBuildStr, new LinkedHashMap<String, String>());
                                    listOfUpcomingEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                                    filteredGenericList.clear();
                                    String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                                    filteredGenericList.add(event);
                                } else {
                                    //do nothing, the event is too obscure
                                }
                            }
                        } else {
                            //Log.d(TAG, "adding a new DATE to the listOfUpcomingEventsByDate = " + dateBuildStr);
                            listOfUpcomingEventsByDate.put(dateBuildStr, new LinkedHashMap<String, String>());
                            listOfUpcomingEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                            String[] event = {varietyIDObj, dateBuildStr, eventTitle, eventDesc};
                            filteredGenericList.add(event);
                        }
                    }
                } else if (eventCal.compareTo(todayIs) < 0) {
                    //date is in the past. Here you can test the events that have passed in order to test them with the future year date
                    //Log.d(TAG, "event calendar for " + varietyIDObj + ", " + eventTitle + " is in the past : " + eventCal.getTime().toString());

                    if (dateSplit.length == 2) {

                        //meaning there is no secondary year specification
                        workableYear = currentYear + 1; //***adding another year here to test this suite of events again
                        month = Integer.parseInt(dateSplit[0]);
                        day = Integer.parseInt(dateSplit[1]);

                        Calendar eventCalAgain = Calendar.getInstance();
                        eventCalAgain.set(workableYear, month-1, day); //added -1
                        //month is on 0 base system

                        int checkYearAgain = workableYear;
                        int checkMonthAgain = month -1;
                        int checkDayAgain = day;

                        Boolean checkIfTodayAgain = false;
                        if((checkYearAgain == currentYear) && (checkMonthAgain == currentMonth) && (checkDayAgain == currentDay)){
                            checkIfTodayAgain = true;
                        }

                        String dateBuildStrAgain = String.valueOf(workableYear) + "," + String.valueOf(month) + "," + String.valueOf(day);

                        if ((eventCalAgain.compareTo(todayIs) > 0) && (!checkIfTodayAgain)) {
                            //Log.d(TAG, eventCal.getTime().toString() + " IS IN THE FUTURE!!");
                            //String varietyIDObj = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_VARIETY_ID));
                            //String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
                            //String eventDesc = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));
                            if (listOfUpcomingEventsByDate.containsKey(dateBuildStrAgain)) {
                                //Log.d(TAG, "list Contains KEY (which is dateBuildStr Object) = " + dateBuildStr + " therefore, only going to add this event to the existing Date Object.");
                                //**if the date in the db matches the current list of most recent date found
                                listOfUpcomingEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                                String[] event = {varietyIDObj, dateBuildStrAgain, eventTitle, eventDesc};
                                filteredGenericList.add(event);
                                //add event to
                            } else {
                                if (listOfUpcomingEventsByDate.size() > 0) { //meaning there is a date in the linked hashmap
                                    //Log.d(TAG, "THERE IS ANOTHER DATE IN THE LIST, NEEDS TO BE COMPARED to the date in question, which is : " + dateBuildStr);
                                    //**compare the two dates
                                    for (String key : listOfUpcomingEventsByDate.keySet()) {
                                        //Log.d(TAG, "retrieving the Key in order to compare, Key : " + key);
                                        //should only be one, btw
                                        String[] keySplit = key.split(",");
                                        Calendar previousCal = Calendar.getInstance();
                                        previousCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1]) -1, Integer.valueOf(keySplit[2]));
                                        //**COMPARE THE dates
                                        if (eventCalAgain.compareTo(previousCal) < 0) {
                                            //Log.d(TAG, eventCal.getTime().toString() + " is before " + previousCal.getTime().toString());

                                            //Log.d(TAG, "dateBuild String = " + dateBuildStr);
                                            //meaning that the eventCal obj preceeds the one previously, but is still in the Future relative to today
                                            listOfUpcomingEventsByDate.clear(); //then clear the hashmap and start over
                                            listOfUpcomingEventsByDate.put(dateBuildStrAgain, new LinkedHashMap<String, String>());
                                            listOfUpcomingEventsByDate.get(dateBuildStrAgain).put(varietyIDObj, eventDesc);
                                            //Log.d(TAG, "dateBuildStr : " +  dateBuildStrAgain + " varietyID : " + varietyIDObj + " eventDesc : " + eventDesc);
                                            filteredGenericList.clear();
                                            String[] event = {varietyIDObj, dateBuildStrAgain, eventTitle, eventDesc};
                                            filteredGenericList.add(event);
                                        } else {
                                            //do nothing, the event is too obscure
                                        }
                                    }
                                } else {
                                    //Log.d(TAG, "adding a new DATE to the listOfUpcomingEventsByDate = " + dateBuildStr);
                                    listOfUpcomingEventsByDate.put(dateBuildStrAgain, new LinkedHashMap<String, String>());
                                    listOfUpcomingEventsByDate.get(dateBuildStrAgain).put(varietyIDObj, eventDesc);
                                    //Log.d(TAG, "dateBuildStr : " +  dateBuildStrAgain + " varietyID : " + varietyIDObj + " eventDesc : " + eventDesc);

                                    String[] event = {varietyIDObj, dateBuildStrAgain, eventTitle, eventDesc};
                                    filteredGenericList.add(event);
                                }
                            }
                        }
                    } else if (dateSplit.length == 3) {
                        int adjustedYear = 0;
                        if (dateSplit[0].equals("n")) {
                            adjustedYear = currentYear + 1;
                        } else if (dateSplit[0].equals("nn")) {
                            adjustedYear = currentYear + 2;
                        } else if (dateSplit[0].equals("nnn")) {
                            adjustedYear = currentYear + 3;
                        } else if (dateSplit[0].equals("nnnn")) {
                            adjustedYear = currentYear + 4;
                        } else if (dateSplit[0].equals("nnnnn")) {
                            adjustedYear = currentYear + 5;
                        }
                        workableYear = adjustedYear;
                        month = Integer.parseInt(dateSplit[1]); //this needs to be + 1
                        day = Integer.parseInt(dateSplit[2]);
                    }
                }//***by the end of this if else statement, you should have all of the most recent notifications as a linkedhashmap to use

            }//end of looping through the list of possible generic events

            for(int g = 0; g < arrestedEvents.size(); g++){

                    String dateString = arrestedEvents.get(g)[1];
                    String[] dateSplit = dateString.split(",");
                    int year = Integer.valueOf(dateSplit[0]);
                    int month = Integer.valueOf(dateSplit[1]);
                    int day = Integer.valueOf(dateSplit[2]);


                    Calendar arrestedDate = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                    arrestedDate.set(year,month-1,day);

                    String formattedArrestedDate = sdf.format(arrestedDate.getTime());
                    //Log.d(TAG, "FORMATTED ARRESTED DATE : " + formattedArrestedDate);
                    arrestedEvents.get(g)[1] = formattedArrestedDate;

            }
            for(int h = 0; h < filteredGenericList.size(); h++){

                String dateString = filteredGenericList.get(h)[1];
                //Log.d(TAG, "CHECKING DATE FORMAT : " + dateString);

                String[] dateSplit = dateString.split(",");
                    int year = Integer.valueOf(dateSplit[0]);
                    int month = Integer.valueOf(dateSplit[1]);
                    int day = Integer.valueOf(dateSplit[2]);

                    Calendar genericDate = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                    genericDate.set(year,month-1,day);

                    String formattedGenericDate = sdf.format(genericDate.getTime());
                    //Log.d(TAG, "FORMATTED GENERIC DATE : " + formattedGenericDate);
                    filteredGenericList.get(h)[1] = formattedGenericDate;

            }

            ArrayList<String[]> updatedGenericEventsList = new ArrayList<>();

            for (int i = 0; i < filteredGenericList.size(); i++) {
                    //Log.d(TAG, "GENERIC EVENTS LIST SIZE = " + filteredGenericList.size());


                    Boolean arrested = false;
                    for (int k = 0; k < arrestedEvents.size(); k++) {
                        if ((genericEventsList.get(i)[0].equals(arrestedEvents.get(k)[0]))
                                //&& (genericEventsList.get(i)[1].equals(arrestedEvents.get(k)[1]))
                                && (genericEventsList.get(i)[1].equals(arrestedEvents.get(k)[1]))
                                && (genericEventsList.get(i)[2].equals(arrestedEvents.get(k)[2]))
                                ) {
                            //meaning there is a match with the event in the mostrecentevents list and the arrested events list
                            /***genericEventsList.remove(i);**/
                            //genericEventsList.remove(genericEventsList.get(i));
                            //i = i - 1; //reset the index to return to the index from before since index at i was removed.
                            //Log.d(TAG, "i now equals : " + i);
                            arrested = true;
                            //then repeat
                        } else {
                            //do nothing because there is no match
                            //updatedGenericEventsList.add(genericEventsList.get(i));
                            //Log.d(TAG, "NO MATCH");
                        }
                    }
                    if(arrested){
                        filteredGenericList.remove(i);
                    }
                }
            finalListOfUpcomingEvents = filteredGenericList;
            //}

        Boolean allTheSame = true;

        //the following for-loop loops through the finalListOfUpcomingEvents and double checks that they are all the same date, and puts the event items into a list (varietyCalList)
        for(int j = 0; j < finalListOfUpcomingEvents.size(); j++){
            if(j > 0){
                String date1 = finalListOfUpcomingEvents.get(j)[1];
                String date2 = finalListOfUpcomingEvents.get(j-1)[1];

                if(date1.equals(date2)){
                    //do nothing
                }else{
                    allTheSame = false;
                }
            }

            String varID = finalListOfUpcomingEvents.get(j)[0];
            String baselineID = finalListOfUpcomingEvents.get(j)[0].replaceAll("[0-9]", "").toLowerCase();
            String readableName = getReadableName(baselineID);
            //String formattedEventTitle = "";
            String eventTitle = finalListOfUpcomingEvents.get(j)[2];
            String eventDesc = finalListOfUpcomingEvents.get(j)[3];

            Boolean isFallCycle = false;
            String formattedEventTitle = new getReadableEventTitle(eventTitle).getFinalTitleStr();
            /**the Boolean isFallCycle needs to be distinguishable (t/f) still. this is currently not happening and is rendering all of the
             * events as false
             * */
            /**turn the event title to a chars array*/
            if(eventTitle.contains("2")){
                isFallCycle = true;
            }


            int varietyImage = new getImage(baselineID).getImage();
            varietyCalList.add(new varietyCal(varID, readableName, varietyImage, formattedEventTitle, isFallCycle, eventDesc));
        }

        String date = "";
        if((allTheSame) && (finalListOfUpcomingEvents.size() > 0)){
            //then retrieve the date String
            date = finalListOfUpcomingEvents.get(0)[1]; //this is the final date string to pass to the UI for the NEXT notification evetn
        }
        /*run a series of filters to determine the needed state of the UI based on what has been retrieved from the DB*/

        Toolbar toolbar = findViewById(R.id.upcoming_notification_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (!hasVarieties && !hasLocation){
            upcomingNotificationsRecyclerView = findViewById(R.id.upcoming_notifications_recycler_view);
            upcomingNotificationsRecyclerView.setVisibility(View.GONE);

            FrameLayout baselineFrame = findViewById(R.id.upcoming_notifications_recycler_view_module);
            baselineFrame.setVisibility(View.GONE);

            TextView nothingToSeeHereTop = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_top);
            nothingToSeeHereTop.setText(R.string.nothing_to_see_here);

            TextView nothingToSeeHereBottom = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_bottom);
            nothingToSeeHereBottom.setText(R.string.nothing_to_see_here_user_has_no_location);
            nothingToSeeHereBottom.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    goToAddLocationActivity();
                }
            });
        }

        if (hasVarieties && hasLocation){ //implication is that there is a location and varieties in the DB

            TextView title = findViewById(R.id.upcoming_notification_activity_title);
            title.setText(R.string.upcoming_notifications_title);

            getReadableDate re = new getReadableDate(date);
            String refin = re.getFinalDateStr();

            String reStr = getString(R.string.upcoming_notifications_subtitle, refin);
            TextView subtitle = findViewById(R.id.upcoming_notification_activity_subtitle);
            subtitle.setText(reStr);
            subtitle.setAllCaps(false);

            FrameLayout baselineFrameLayout = findViewById(R.id.upcomingNotifications_nothingToSeeHereMsgFrame);
            baselineFrameLayout.setVisibility(View.GONE);
            TextView obsoleteMsg = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_bottom);
            obsoleteMsg.setVisibility(View.GONE);

            upcomingNotificationsRecyclerView = findViewById(R.id.upcoming_notifications_recycler_view);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            dashAdapter = new upcomingNotificationsAdapter(this, varietyCalList);
            upcomingNotificationsRecyclerView.setLayoutManager(llm);
            upcomingNotificationsRecyclerView.setAdapter(dashAdapter);

        }else if(hasLocation && !hasVarieties){

            //this is also redundant, but just in case the user finds themselves here without any varieties in the database.
            //userVarieties.setText(R.string.dashboard_user_has_no_varieties);

            upcomingNotificationsRecyclerView = findViewById(R.id.upcoming_notifications_recycler_view);
            upcomingNotificationsRecyclerView.setVisibility(View.GONE);

            FrameLayout baselineFrame = findViewById(R.id.upcoming_notifications_recycler_view_module);
            baselineFrame.setVisibility(View.GONE);

            /**set UI to baseline with image as background*/
            ImageView baselineImage = findViewById(R.id.upcoming_notifications_activity_background_baseline);
            baselineImage.setImageResource(R.drawable.okra);

            TextView NothingToSeeHereTop = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_top);
            NothingToSeeHereTop.setText(R.string.nothing_to_see_here);

            TextView nothingToSeeHereBottom = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_bottom);
            nothingToSeeHereBottom.setText(R.string.nothing_to_see_here_user_has_no_varieties);
            nothingToSeeHereBottom.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    goToAddVarietiesActivity();
                }
            });
        } else if (!hasLocation && hasVarieties){ /***added for the sake of being logically thorough*/
            upcomingNotificationsRecyclerView = findViewById(R.id.upcoming_notifications_recycler_view);
            upcomingNotificationsRecyclerView.setVisibility(View.GONE);

            FrameLayout baselineFrame = findViewById(R.id.upcoming_notifications_recycler_view_module);
            baselineFrame.setVisibility(View.GONE);

            /**set UI to baseline with image as background*/
            ImageView baselineImage = findViewById(R.id.upcoming_notifications_activity_background_baseline);
            baselineImage.setImageResource(R.drawable.okra);

            TextView NothingToSeeHereTop = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_top);
            NothingToSeeHereTop.setText(R.string.nothing_to_see_here);

            TextView nothingToSeeHereBottom = findViewById(R.id.upcomingNotifications_nothingToSeeHere_msg_bottom);
            nothingToSeeHereBottom.setText(R.string.nothing_to_see_here_user_has_no_varieties);
            nothingToSeeHereBottom.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    goToAddVarietiesActivity();
                }
            });
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public String getReadableName(String entry){
        String json = null;
        String readableName = "";
        try{
            InputStream botanicalNamesFile = getAssets().open("readableNamesByVariety.json");
            int size = botanicalNamesFile.available();
            byte[] buffer = new byte[size];
            botanicalNamesFile.read(buffer);
            botanicalNamesFile.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }

        try {
            JSONObject readableNames = new JSONObject(json).getJSONObject("readableNames");
            readableName = readableNames.getString(entry);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return readableName;
    }

    public void goToAddVarietiesActivity(){
        Intent intent = new Intent(this, addVarietiesActivity.class);
        intent.putExtra("comingFromDashboard", true);//though, not really, but that's okay.
        startActivity(intent);
    }
    public void goToAddLocationActivity(){
        Intent intent = new Intent(this, addLocationActivity.class);
        //any extras??
        startActivity(intent);
    }
}
