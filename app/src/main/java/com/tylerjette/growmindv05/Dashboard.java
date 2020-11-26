package com.tylerjette.growmindv05;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class Dashboard extends AppCompatActivity {

    private RecyclerView dashboardRecycler; //this will carry the UI with each of the separate notifications by variety
    private dashboardAdapter dAdapter; //haven't made this yet

    Context context = this;

    private static final String TAG = Dashboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_notification);

        /**implement the horizontal configuration UI*/
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            //reset linear layout for toolbar to orientation: horizontal.
            LinearLayout toolbarlayout = findViewById(R.id.dashboard_toolbar_module);
            toolbarlayout.setOrientation(LinearLayout.HORIZONTAL);
            toolbarlayout.setGravity(Gravity.CENTER_VERTICAL);

            TextView title = findViewById(R.id.dashboard_activity_title);
            TextView subtitle = findViewById(R.id.dashboard_activity_subtitle);

            LinearLayout.MarginLayoutParams titleParams = new LinearLayout.LayoutParams(title.getLayoutParams());
            LinearLayout.MarginLayoutParams subtitleParams = new LinearLayout.LayoutParams(subtitle.getLayoutParams());
            titleParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            subtitleParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;

            titleParams.setMargins(0,0,0,0);
            subtitleParams.setMargins(32,3,0,0);

            title.setLayoutParams(titleParams);
            subtitle.setLayoutParams(subtitleParams);
        }

        String intentInput = "";
        Intent intent = getIntent();

        /**activity redirects*/
        if(intent.hasExtra("msg_from_Main")){
            intentInput = intent.getStringExtra("msg_from_Main");
            if(intentInput.equals("addLocationActivity")){
                //go to addLocationActivity
                Intent goToLocationActivity = new Intent(this, addLocationActivity.class);
                goToLocationActivity.putExtra("msg_from_Dashboard", "thenAddVarieties");
                startActivity(goToLocationActivity);
            }else if (intentInput.equals("addVarietiesActivity")){
                Intent goToVarietiesActivity = new Intent(this, addVarietiesActivity.class);
                startActivity(goToVarietiesActivity);
            }else if (intentInput.equals("has varieties but no Location")){
                //do nothing
            }else if(intentInput.equals("hasLocation&Varieties")){
                businessAsUsual();
            }
        } else if(intent.hasExtra("msg_from_instructions")){
            intentInput = intent.getStringExtra("msg_from_instructions");

            if(intentInput.equals("addLocationActivity")){
                //go to addLocationActivity
                Intent goToLocationActivity = new Intent(this, addLocationActivity.class);
                startActivity(goToLocationActivity);
            } else {
                //businessAsUsual()...
            }
        }  else if(intent.hasExtra("msg_from_addLocation")){
            intentInput = intent.getStringExtra("msg_from_addLocation");
            if(intentInput.equals("thenAddVarieties")){
                Intent goToVarietiesActivity = new Intent(this, addVarietiesActivity.class);
                startActivity(goToVarietiesActivity);
            }
        } else {
            businessAsUsual();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_dashboard_toolbar);
        float textAlphaTo = 0.35f;
        ObjectAnimator.ofFloat(toolbar, "alpha",textAlphaTo).setDuration(750).start();

        FrameLayout backgroundFader = findViewById(R.id.dashboard_background_fader);
        int colorFrom = Color.parseColor("#00000000");
        int colorTo = Color.parseColor("#99000000");
        if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(5.0f);
        }
        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
            .setDuration(750)
            .start();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        float textAlphaFrom = 1.0f;

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_dashboard_toolbar);
        ObjectAnimator.ofFloat(toolbar, "alpha",textAlphaFrom).setDuration(750).start();

        FrameLayout backgroundFader = findViewById(R.id.dashboard_background_fader);
        int colorTo = Color.parseColor("#00000000");
        int colorFrom = Color.parseColor("#90000000");
        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(750)
                .start();
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.currentVarieties:
                goToCurrentVarietiesActivity();
                return true;
            case R.id.setLocationMenu:
                goToSetLocationActivity();
                return true;
            case R.id.setVarietiesMenu:
                goToSetVarietiesActivity();
                return true;
            case R.id.mostRecentNotification:
                goToUpcomingNotificationsActivity();
                return true;
            case R.id.returnToInstructions:
                goToInstructions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToCurrentVarietiesActivity(){
        Intent intent = new Intent(this, currentVarieties.class);
        startActivity(intent);
    }
    public void goToSetLocationActivity(){
        Intent intent = new Intent(this, addLocationActivity.class);
        startActivity(intent);
    }
    public void goToSetVarietiesActivity(){
        Intent intent = new Intent(this, addVarietiesActivity.class);
        intent.putExtra("previousActivity", "Dashboard");
        startActivity(intent);
    }
    public void goToUpcomingNotificationsActivity(){
        Intent intent = new Intent(this, upcomingNotificationsActivity.class);
        intent.putExtra("previousActivity", "Dashboard");
        startActivity(intent);
    }
    public void goToInstructions(){
        Intent intent = new Intent(this, instructions.class);
        intent.putExtra("previousActivity", "Dashboard");
        startActivity(intent);
    }

    public String loadReadableName(){
        String json;
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
        return json;
    }

    public void businessAsUsual(){

        Boolean hasVarieties = false;
        Calendar todayIs = Calendar.getInstance();
        int currentYear = todayIs.get(Calendar.YEAR);
        int currentMonth = todayIs.get(Calendar.MONTH);
        int currentDay = todayIs.get(Calendar.DATE);
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        /***new updated version of the db query will include a query to the each of the generic calendar, the immediate calendar, and the arrested calendar
         * In the end, you will want an ArrayList with the most appropriate dates and events. First of all, all of the immediate dates will have to be configured
         * to include the YEAR of conception, meaning that in consecutive years, the year part of the date Object will be overridden.
         *
         * */

        ArrayList<String[]> arrestedEvents = new ArrayList<>();
        ArrayList <String[]> immediateEventsListFromDB = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> listOfMostRecentEventsByDate = new LinkedHashMap<>();
        ArrayList<String[]> genericEventsList = new ArrayList<>();
        ArrayList <String[]> finalListOfMostRecentEvents = new ArrayList<>();

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

        while(arrestedCursor.moveToNext()){
            String arrestedDate = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_DATE));
            String arrestedVarietyID = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_VARIETY_ID));
            String arrestedEventTitle = arrestedCursor.getString(arrestedCursor.getColumnIndexOrThrow(arrestedEventsCalendarContract.arrestedCalendarEntry.COLUMN_EVENT_TITLE));
            String[] event = {arrestedVarietyID,arrestedDate,arrestedEventTitle};
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
        while(immediateCursor.moveToNext()){
            String varietyID = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_VARIETY_ID));
            String dateObj = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DATE));
            String eventTitle = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_TITLE));
            String eventDesc = immediateCursor.getString(immediateCursor.getColumnIndexOrThrow(immediateCalendarContract.immediateCalendarEntry.COLUMN_EVENT_DESCRIPTION));
            String [] event = {varietyID,dateObj,eventTitle,eventDesc};
            immediateEventsListFromDB.add(event);
        }

        immediateCursor.close();
        /**now you have the list of immediate notifications with the format of yyyy,mm,dd"*

        /**retrieve the generic events*/
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



        /***this is where the issue is...*/
        while(cursor.moveToNext()){
            hasVarieties = true;
            /**get the date object, and first determine if the date is the most recent in the list*/
            String dateObject = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DATE));
            /**remember, this returns a date string object with a month that is on a 0-based index, so all date processing needs to -1
             * at this point, all of these dates are in the format mm,dd or if they do have a year string, then they are nx,mm,dd.
             * */

            //String eventTitle = genericCursor.getString(genericCursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
            //String eventDesc = genericCursor.getString(genericCursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));
            String[] dateSplit = dateObject.split(",");
            int month = 0;
            int day = 0;
            int workableYear = 0;
            if(dateSplit.length == 2){
                //meaning there is no secondary year specification
                workableYear = currentYear;
                month = Integer.parseInt(dateSplit[0]);
                day = Integer.parseInt(dateSplit[1]);
            }else if(dateSplit.length == 3){
                int adjustedYear = currentYear;
                if(dateSplit[0].equals("n")){
                    adjustedYear = currentYear + 1;
                }else if (dateSplit[0].equals("nn")){
                    adjustedYear = currentYear + 2;
                }else if (dateSplit[0].equals("nnn")){
                    adjustedYear = currentYear + 3;
                }else if (dateSplit[0].equals("nnnn")){
                    adjustedYear = currentYear + 4;
                }else if (dateSplit[0].equals("nnnnn")){
                    adjustedYear = currentYear + 5;
                }
                workableYear = adjustedYear;
                month = Integer.parseInt(dateSplit[1]);
                day = Integer.parseInt(dateSplit[2]);
            }

            String dateBuildStr = String.valueOf(workableYear) + "," + String.valueOf(month) + "," + String.valueOf(day);

            //logic for retreiving the most relevant dates
            Calendar eventCal = Calendar.getInstance();
            eventCal.set(workableYear, month-1, day);

            //Log.d(TAG, "HERE IS THE EVENT CAL after it is set for comparison : " + eventCal.getTime().toString());

            /**check the dates by comparing each integer of YYYY,mm,dd*/
            int checkYear = workableYear;
            int checkMonth = month -1;
            int checkDay = day;

            Boolean checkIfToday = false;
            if((checkYear == currentYear) && (checkMonth == currentMonth) && (checkDay == currentDay)){
                //Log.d(TAG, "eventCal and todayIs are the SAME using the == operator on the respective int values for YYYY,MM,DD");

                checkIfToday = true;
            }
            if((eventCal.compareTo(todayIs)<0) || checkIfToday) {
                //Log.d(TAG, "event Cal (" + eventCal.getTime() + ") is before today");
                //meaning the event has passed, then it qualifies as a past event
                String varietyIDObj = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_VARIETY_ID));
                String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
                String eventDesc = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));
                if(listOfMostRecentEventsByDate.containsKey(dateBuildStr)){
                    /**if the date in the db matches the current list of most recent date found*/
                    listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                    String[] event = {varietyIDObj, dateBuildStr,eventTitle, eventDesc};
                    genericEventsList.add(event);
                    //add event to
                } else {
                    if(listOfMostRecentEventsByDate.size() > 0){
                        /**compare the two dates*/
                        for(String key: listOfMostRecentEventsByDate.keySet()){
                            String[] keySplit = key.split(",");
                            Calendar previousCal = Calendar.getInstance();
                            previousCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1])-1, Integer.valueOf(keySplit[2]));

                            if(eventCal.compareTo(previousCal) > 0){
                                listOfMostRecentEventsByDate.clear(); //then clear the hashmap and start over
                                listOfMostRecentEventsByDate.put(dateBuildStr, new LinkedHashMap<String,String>());
                                listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj,eventDesc);
                                genericEventsList.clear();
                                String[] event = {varietyIDObj,dateBuildStr,eventTitle, eventDesc};
                                genericEventsList.add(event);
                            }else {
                                //nothing
                            }
                        }
                    }else{
                        listOfMostRecentEventsByDate.put(dateBuildStr, new LinkedHashMap<String, String>());
                        listOfMostRecentEventsByDate.get(dateBuildStr).put(varietyIDObj, eventDesc);
                        String[] event = {varietyIDObj,dateBuildStr,eventTitle,eventDesc};
                        genericEventsList.add(event);
                    }
                }
            }else {
                //Log.d(TAG, "event Cal (" + eventCal.getTime() + ") is after today");
                //do nothing, date is not in the past
            }
        }//end of looping through the list of possible generic events

        cursor.close();

        Boolean immediateIsMoreRecent = false;
        //Log.d(TAG, "LIST OF MOST RECENT EVENTS BY DATE : "+ listOfMostRecentEventsByDate.keySet().toString());
        for(String key : listOfMostRecentEventsByDate.keySet()){
            String[] keySplit = key.split(",");
            Calendar genericCal = Calendar.getInstance();
            genericCal.set(Integer.valueOf(keySplit[0]), Integer.valueOf(keySplit[1])-1, Integer.valueOf(keySplit[2]));

            //loop through the list of immediate events
            for(int i = 0; i<immediateEventsListFromDB.size(); i++){
                String date = immediateEventsListFromDB.get(i)[1]; //hopefully this is the dateObj in the array
                String[] dateSplit = date.split(",");
                Calendar immediateEventsCal = Calendar.getInstance();
                immediateEventsCal.set(Integer.valueOf(dateSplit[0]), Integer.valueOf(dateSplit[1])-1, Integer.valueOf(dateSplit[2]));
                if(immediateEventsCal.compareTo(genericCal) > 0){
                    //the immediate events Calendar is more recent than the generic calendar
                    immediateIsMoreRecent = true;
                }else {
                    //do nothing, the immediate dates are irrelevant at this point in time.
                }
            }
        }
        /**but here, you have to consider that maybe all of the events are in the future, and in that case, if there is actually a list of notifications
         * in the immediateEventsList, then those items may be  relevant, in which case the boolean immediateIsMoreRecent should be set to true*/
        if((listOfMostRecentEventsByDate.size() == 0)&&(immediateEventsListFromDB.size() > 0)){
            //set the boolean value of immediateIsMoreRecent to True, since those items are still valid
            immediateIsMoreRecent = true;
        } else if ((listOfMostRecentEventsByDate.size() == 0) && (immediateEventsListFromDB.size() == 0)){
            /**then you need to create a forward-looking immediateEventsList which will say things like "the brocolli cycle hasn't begun
             * in your area. It will begin in March...*/

            /**1. get the list of varieties*/

            /**2. look at the todolist items for each variety, and create a notification for each based on the startInside, or
             * directOutside dates that correspond to each one.*/

        }

        if(immediateIsMoreRecent){
            /**basically, replace the entire list with the immediate list*/

            listOfMostRecentEventsByDate.clear();
            genericEventsList.clear();

            /**the finalListOfMostRecentEvents list will then become the  immediateEventList at this point*/
            finalListOfMostRecentEvents = immediateEventsListFromDB;

        }else{
            /**here the immediate event list is obsolete, and you have to start checking the most recent events list against the arrested list*/

            for(int i = 0; i < genericEventsList.size(); i++){
                for(int k = 0; k < arrestedEvents.size(); k++){

                    /**check if there is a match between the generic list and the arrested list, and then delete the item from the generic list, if necessary...*/
                    if((genericEventsList.get(i)[0].equals(arrestedEvents.get(k)[0]))
                            && (genericEventsList.get(i)[1].equals(arrestedEvents.get(k)[1]))
                            && (genericEventsList.get(i)[2].equals(arrestedEvents.get(k)[2]))
                            ){
                        //meaning there is a match with the event in the mostrecentevents list and the arrested events list
                        genericEventsList.remove(i);
                        //genericEventsList.remove(genericEventsList.get(i));
                        i = i-1; //reset the index to return to the index from before since index at i was removed.
                        //then repeat
                    } else {
                        //do nothing because there is no match
                    }
                }
            }
            //then turn the final list into the generic list
            finalListOfMostRecentEvents = genericEventsList;
        }

        /**UI configurations based on each condition (whether the user has varieties, or doesn't*/
        if(hasVarieties){
            //set the UI to include the recyclerview and the custom toolbar as the actionbar
            /**for custom toolbar*/
            android.support.v7.widget.Toolbar dashboardToolbar = findViewById(R.id.custom_dashboard_toolbar);

            /**trying to make the background image obsolete by simp[ly setting the background for the entire activity to the baseline activity frame.*/
            //Drawable bk = ResourcesCompat.getDrawable(getResources(), R.drawable.broccoli_background, null);
            //dashboardToolbar.setBackground(bk);

            //dashboardToolbar.inflateMenu(R.menu.dashboard_menu);
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            Boolean allTheSame = true;
            /**loop through the final list of Most Recent Events and then get the date object, as long as there are no mistakes and the dates are all the same.*/

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
                date = finalListOfMostRecentEvents.get(0)[1];
                /***this is throwing an error out of bounds*/
            }

            String finalDateStr = date;

            /**can you customize the action bar with just setting a custom layout?*/
            //Drawable d=getResources().getDrawable(R.drawable.seeds_dark);



/**this is the original actionbar*/

            /**since the user has notifications in the queue that they can use, set the UI to this in the custom Toolbar*/
            getReadableDate re = new getReadableDate(finalDateStr);
            String readableDateStr = re.getFinalDateStr();
            //String readableDate = getReadableDate(finalDateStr);
            //subtitle.setText("sent " + readableDateStr);*/

            TextView activityTitle = findViewById(R.id.dashboard_activity_title);
            activityTitle.setText(R.string.dashboard_title_has_notifications);

            TextView activitySubtitle = findViewById(R.id.dashboard_activity_subtitle);
            //activitySubtitle.setText(R.string.dashboard_subtitle_has_notifications, readableDateStr);
            String activitySubtitleText = getApplicationContext().getString(R.string.dashboard_subtitle_has_notifications,readableDateStr);
            activitySubtitle.setText(activitySubtitleText);

            JSONObject readableNamesFile = new JSONObject();
            try{
                readableNamesFile =  new JSONObject(loadReadableName().trim());
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            ArrayList mostRecentEventList = new ArrayList();

            /**get the list of the most recent notifications from above*/
            for(int h = 0; h < finalListOfMostRecentEvents.size(); h++){
                String baselineZoneVarID = finalListOfMostRecentEvents.get(h)[0];
                String baselineVarietyID = baselineZoneVarID.replaceAll("[0-9]", "");
                //get the readable name from the readableNameFile
                String eventTitle = finalListOfMostRecentEvents.get(h)[2];
                String formattedEventTitle = "";
                /**convert the eventTitle code into a readable format*/
                Boolean isFallCycle = false;
                formattedEventTitle = new getReadableEventTitle(eventTitle).getFinalTitleStr();

                String eventDetail = finalListOfMostRecentEvents.get(h)[3];

                CharSequence fallChar = "fall";
                if(eventDetail.toLowerCase().contains(fallChar)){
                    isFallCycle = true;
                }

                String readableName = "";
                try{
                    readableName = readableNamesFile.getJSONObject("readableNames").getString(baselineVarietyID.toLowerCase());
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
                int varietyImage = new getImage(baselineVarietyID.toLowerCase()).getImage();

                if(isFallCycle){
                    Boolean isDuplicateVariety = false; //this will check whether there is a duplicate
                    Boolean springCycleIsRelevant = true;
                    Boolean fallCycleIsRelevant = true;

                    //loop through
                    for(int k = 0; k < mostRecentEventList.size(); k++){
                        notificationEvent listed = (notificationEvent)mostRecentEventList.get(k);
                        if((listed.getBaselineID().equals(baselineZoneVarID))){
                            //don't do anything, don't add to the list
                            isDuplicateVariety = true;

                            String checkListedTitle = listed.getEventTitle();

                            if(checkListedTitle.equals("Planting window has passed") || checkListedTitle.equals("Planting window has not begun")){
                                springCycleIsRelevant = false;
                            }

                            if(formattedEventTitle.equals("Planting window has passed") || formattedEventTitle.equals("Planting window has not begun")){
                                fallCycleIsRelevant = false;
                            }
                        }
                    }

                    if(isDuplicateVariety){
                        if(springCycleIsRelevant && fallCycleIsRelevant){
                            //set the fall cycle
                            notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                            mostRecentEventList.add(eventByVar);
                        }else if (!springCycleIsRelevant && fallCycleIsRelevant){
                            //set the fall cycle
                            notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                            mostRecentEventList.add(eventByVar);
                        }else if (springCycleIsRelevant && !fallCycleIsRelevant){
                            //don't set the fall cycle
                        }else if (!springCycleIsRelevant && !fallCycleIsRelevant){
                            //don't set the fall cycle
                        }
                    }else {
                        //not duplicate, ok to proceed
                        notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                        mostRecentEventList.add(eventByVar);
                    }
                } else {
                    //the current event is from the Spring cycle

                    Boolean isDuplicateVariety = false; //this will check whether there is a duplicate
                    Boolean springCycleIsRelevant = true;
                    Boolean fallCycleIsRelevant = true;
                    for(int k = 0; k < mostRecentEventList.size(); k++){
                        notificationEvent listed = (notificationEvent)mostRecentEventList.get(k);
                        if((listed.getBaselineID().equals(baselineZoneVarID))){
                            //don't do anything, don't add to the list
                            isDuplicateVariety = true;

                            String checkListedTitle = listed.getEventTitle();

                            if(checkListedTitle.equals("Planting window has passed") || checkListedTitle.equals("Planting window has not begun")){
                                fallCycleIsRelevant = false;
                            }

                            if(formattedEventTitle.equals("Planting window has passed") || formattedEventTitle.equals("Planting window has not begun")){
                                springCycleIsRelevant = false;
                            }
                        }
                    }

                    if(isDuplicateVariety){
                        if(springCycleIsRelevant && fallCycleIsRelevant){
                            //set the fall cycle
                            notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                            mostRecentEventList.add(eventByVar);
                        }else if (!springCycleIsRelevant && fallCycleIsRelevant){
                            //don't set the spring cycle
                        }else if (springCycleIsRelevant && !fallCycleIsRelevant){
                            //set the spring cycle
                            notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                            mostRecentEventList.add(eventByVar);
                        }else if (!springCycleIsRelevant && !fallCycleIsRelevant){
                            //set the fall cycle
                            notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                            mostRecentEventList.add(eventByVar);
                        }
                    }else {
                        //not duplicate, ok to proceed
                        notificationEvent eventByVar = new notificationEvent(baselineZoneVarID, readableName, formattedEventTitle,eventDetail, varietyImage, isFallCycle);
                        mostRecentEventList.add(eventByVar);
                    }
                }
            }//end of looping through the final list of most recent variety events

            Button obsoleteBtn = findViewById(R.id.dashboard_nothingToSeeHere_msg_bottom);
            obsoleteBtn.setVisibility(View.GONE);

            dashboardRecycler = findViewById(R.id.dashboard_recycler_view);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            dashboardRecycler.setLayoutManager(llm);
            dAdapter = new dashboardAdapter(context, mostRecentEventList);
            dashboardRecycler.setAdapter(dAdapter);
        } else if(!hasVarieties){

            dashboardRecycler = findViewById(R.id.dashboard_recycler_view);
            dashboardRecycler.setVisibility(View.GONE);

            FrameLayout dashboardRecyclerViewModule = findViewById(R.id.dashboard_recycler_view_module);
            dashboardRecyclerViewModule.setVisibility(View.GONE);

            /**for custom toolbar*/
            android.support.v7.widget.Toolbar dashboardToolbar = findViewById(R.id.custom_dashboard_toolbar);
            dashboardToolbar.inflateMenu(R.menu.dashboard_menu);
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            FrameLayout primary = findViewById(R.id.dashboard_background_filter_primary);
            primary.setBackgroundColor(getResources().getColor(R.color.background_standard));

            FrameLayout secondary = findViewById(R.id.dashboard_background_filter_secondary);
            secondary.setBackgroundColor(getResources().getColor(R.color.secondary_background_filter));

            TextView nothingToSeeHereTop = findViewById(R.id.dashboard_nothingToSeeHere_msg_top);
            nothingToSeeHereTop.setText(R.string.nothing_to_see_here);

            Button nothingToSeeHereBottom = findViewById(R.id.dashboard_nothingToSeeHere_msg_bottom);
            nothingToSeeHereBottom.setText(R.string.nothing_to_see_here_user_has_no_varieties);

            nothingToSeeHereBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSetVarietiesActivity();
                }
            });
        }
    }
}
