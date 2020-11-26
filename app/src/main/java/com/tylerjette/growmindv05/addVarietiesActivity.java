package com.tylerjette.growmindv05;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class addVarietiesActivity extends AppCompatActivity{

    private static final String TAG = addVarietiesActivity.class.getSimpleName();
    private RecyclerView varietiesRecyclerView;
    private varietyAdapter vAdapter;
    private String userZone;
    //private String userVarietyItem;
    private growmindDataSource dataSource;
    Context context = this;
    private JSONObject toDoJSONObj = new JSONObject();
    //private static final String GROWMIND_CHANNEL_ID = "com.tylerjette.growmindv05.notification_channel";
    //private static final String GROWMIND_CHANNEL_NAME = "GROWMIND Notification Channel";
    static ArrayList<String> varietiesInDB = new ArrayList<>();
    public static ArrayList<String> checkList = new ArrayList<>(); //this needs to be equal to varieties in DB
    private ArrayList<String> passedChecklist = new ArrayList<>();
    private PendingIntent pendingIntent;
    private Handler updateHandler = new Handler();
    //AlarmManager am;
    public boolean selectAllState;
    public int numberOfAvailableVarieties = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent = getIntent();

        //Step 1 : migrate the logic below that relates to the onRestoreInstanceState method
        if(savedInstanceState != null){
            checkList = savedInstanceState.getStringArrayList("checked");
            passedChecklist.clear(); //in case there are multiple savedInstanceStates in the activity lifecycle.
            for(int j = 0; j < checkList.size(); j++){
                passedChecklist.add(checkList.get(j));
            }
            /**implementing selectAll button interface*/

            if(savedInstanceState.containsKey("selectAllState")){
                selectAllState = savedInstanceState.getBoolean("selectAllState");
            }

        }else {
            if((activityIntent.hasExtra("previousActivity")) && (activityIntent.getStringExtra("previousActivity").equals("Dashboard"))){
                checkList = new ArrayList<>();
                //Log.d(TAG, "************************INITIATED A NEW CHECKLISTARRAY, COMING FROM DASHBOARD: " + checkList.toString());

            }else if (activityIntent.hasExtra("transferChecklist")){
                checkList = activityIntent.getStringArrayListExtra("transferChecklist");
                //Log.d(TAG, "************************ASSIGNED THE CHECKLIST AS THE VARIETIESINDB object via activityIntent: " + checkList.toString());

                /**currently this is not working*/
                passedChecklist.clear();
                for(int j = 0; j < checkList.size(); j++){
                    passedChecklist.add(checkList.get(j));
                }

            }else {
                checkList = new ArrayList<>();
               // Log.d(TAG, "************************Default checklist Assignment as new Arraylist " + checkList.toString());

            }
        }

        int orientation = getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_add_varieties);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout toolbarLL = findViewById(R.id.add_varieties_activity_toolbar_linearlayout);
            toolbarLL.setOrientation(LinearLayout.HORIZONTAL);
            toolbarLL.setGravity(Gravity.CENTER_VERTICAL);

            FrameLayout recyclerModuleFL = findViewById(R.id.recycler_view_module);
            ConstraintLayout.MarginLayoutParams moduleParams = new ConstraintLayout.LayoutParams(recyclerModuleFL.getLayoutParams());
            moduleParams.setMargins(0,90,0,120);
            recyclerModuleFL.setLayoutParams(moduleParams);

            TextView zoneInDB = findViewById(R.id.zoneInDB);
            LinearLayout.MarginLayoutParams zoneTextParams = new LinearLayout.LayoutParams(zoneInDB.getLayoutParams());
            zoneTextParams.setMargins(48,0,0,0);
            zoneInDB.setLayoutParams(zoneTextParams);

            TextView title = findViewById(R.id.add_varieties_toolbar_title);
            LinearLayout.MarginLayoutParams titleParams = new LinearLayout.LayoutParams(title.getLayoutParams());
            titleParams.setMargins(0,0,0,0);
            title.setLayoutParams(titleParams);

        } else {
            // In portrait
        }

        android.support.v7.widget.Toolbar mTopToolbar = findViewById(R.id.add_varieties_activity_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView activityTitle = findViewById(R.id.add_varieties_toolbar_title);
        activityTitle.setText(R.string.add_varieties_action_bar_title);

        varietiesInDB.clear(); //this ensures that the varieties are not being repeated.

        SQLiteHelper dbHelper = SQLiteHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //define the projection for location query
        String[] locationProjection = {locationContract.locationEntry.COLUMN_ZONE};
        String locationSelection = locationContract.locationEntry.COLUMN_ZONE;

        //define projection and selection for variety query
        String[] varietyProjection = {varietyContract.varietyEntry.COLUMN_VARIETY_ID};

        try { //this captures the user's zone in the database, if it exists...
            Cursor cursor = db.query(
                    locationContract.locationEntry.TABLE_NAME,
                    locationProjection,
                    locationSelection,
                    null,
                    null,
                    null,
                    null
            );
            String zoneT = "";
            while (cursor.moveToNext()) {
                String item = cursor.getString(
                        cursor.getColumnIndexOrThrow(locationContract.locationEntry.COLUMN_ZONE)
                );
                zoneT = item;
                userZone = zoneT;
            }
            cursor.close();
            TextView zoneInDbUserPrompt = findViewById(R.id.zoneInDB);
            String zoneInDbString = getString(R.string.add_varieties_user_notice_has_location, zoneT);
            zoneInDbUserPrompt.setText(zoneInDbString);
        }catch(SQLException sqlEx){
            //if loading the database fails (ie: there is no database, or data available, then
            sqlEx.printStackTrace();
            TextView altNotice = findViewById(R.id.zoneInDB);
            String exceptionText = getApplicationContext().getString(R.string.add_varieties_user_has_no_location);
            altNotice.setText(exceptionText);
        }

        try {
            Cursor cursor = db.query(
                    varietyContract.varietyEntry.TABLE_NAME,
                    varietyProjection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                String item = cursor.getString(
                        cursor.getColumnIndexOrThrow(varietyContract.varietyEntry.COLUMN_VARIETY_ID)
                );
                varietiesInDB.add(item);
            }
            cursor.close();
        }catch(SQLException sqlEx){
            sqlEx.printStackTrace();
        }

        final ArrayList suitableVarList = new ArrayList();
        try{
            JSONObject zoneFile = new JSONObject(loadbyZone(userZone).trim()); //this loads the file corresponding to the user's zone

            //load the botanicalNames json file
            JSONObject botanicalNamesFile = new JSONObject(loadBotanicalNames().trim());

            JSONArray zoneFileArray = zoneFile.getJSONArray("zone"+userZone);
            for(int i = 0; i < zoneFileArray.length(); i++){
                String notSuitable = "not suitable";
                String vars = zoneFileArray.getString(i);
                if(!vars.contains(notSuitable)){
                    JSONObject varietyObj = (JSONObject) zoneFileArray.get(i); //singular json variety object
                    String varietyObjName = varietyObj.getString("name");
                    String varietyObjID = varietyObj.getString("id");
                    String baselineID = varietyObjID.replaceAll("[0-9]", "");
                    int varietyObjImage = new getImage(baselineID.toLowerCase()).getImage();

                    String varietyBotanicalName = botanicalNamesFile.getJSONObject("botanicalNames").getString(baselineID.toLowerCase());

                    variety suitableVar = new variety(varietyObjName, varietyObjID, varietyBotanicalName, varietyObjImage, false);

                    suitableVarList.add(suitableVar);
                    numberOfAvailableVarieties++;

                }else{
                    //do nothing
                }
            }

            TextView selectAllButton = findViewById(R.id.selectAllButton);
            if(savedInstanceState != null){
                checkList = new ArrayList<>();
                for(int k = 0; k < passedChecklist.size(); k++){
                    checkList.add(passedChecklist.get(k));
                }
                varietiesRecyclerView = findViewById(R.id.varieties_recycler_view);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                varietiesRecyclerView.setLayoutManager(llm);
                vAdapter = new varietyAdapter(context, suitableVarList,  checkList, userZone,numberOfAvailableVarieties, selectAllButton);
                varietiesRecyclerView.setAdapter(vAdapter);
            }else if(activityIntent.hasExtra("transferChecklist")){
                checkList = new ArrayList<>();
                for(int k = 0; k < passedChecklist.size(); k++){
                    checkList.add(passedChecklist.get(k));
                }
                varietiesRecyclerView = findViewById(R.id.varieties_recycler_view);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                varietiesRecyclerView.setLayoutManager(llm);
                vAdapter = new varietyAdapter(context, suitableVarList,  checkList, userZone,numberOfAvailableVarieties, selectAllButton);
                varietiesRecyclerView.setAdapter(vAdapter);
            }else {
                checkList = varietiesInDB;
                varietiesRecyclerView = findViewById(R.id.varieties_recycler_view);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                varietiesRecyclerView.setLayoutManager(llm);
                vAdapter = new varietyAdapter(context, suitableVarList, varietiesInDB, userZone, numberOfAvailableVarieties, selectAllButton);
                varietiesRecyclerView.setAdapter(vAdapter);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            toDoJSONObj = new JSONObject(loadToDos().trim());
        }catch(JSONException e){
            e.printStackTrace();
        }

        TextView selectAllText = findViewById(R.id.selectAllButton);
        if(checkList.size() == numberOfAvailableVarieties){
            /**set the UI to "deselect all"*/
            selectAllText.setText(R.string.deselect_all_button);
            selectAllText.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_alldeselect));

        }else{
            selectAllText.setText(R.string.select_all_button);
            selectAllText.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_allselect));
        }

        selectAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selectAllText = findViewById(R.id.selectAllButton);
                if(checkList.size() == numberOfAvailableVarieties){
                    /**set the UI to "deselect all"*/
                    selectAllText.setText(R.string.select_all_button);
                    /**reassign the checkList to null*/
                    checkList.clear();
                    selectAllText.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_allselect));

                    /**re-render the varieties adapter*/
                    varietiesRecyclerView = findViewById(R.id.varieties_recycler_view);
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    varietiesRecyclerView.setLayoutManager(llm);
                    vAdapter = new varietyAdapter(context, suitableVarList, checkList, userZone, numberOfAvailableVarieties, selectAllText);
                    varietiesRecyclerView.setAdapter(vAdapter);
                }else{
                    selectAllText.setText(R.string.deselect_all_button);
                    checkList.clear();
                    for(int i = 0; i< suitableVarList.size(); i++){
                        variety suitableVar = (variety)suitableVarList.get(i);
                        checkList.add(suitableVar.getZoneVarID());
                    }
                    selectAllText.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_alldeselect));

                    varietiesRecyclerView = findViewById(R.id.varieties_recycler_view);
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    varietiesRecyclerView.setLayoutManager(llm);
                    vAdapter = new varietyAdapter(context, suitableVarList, checkList, userZone, numberOfAvailableVarieties, selectAllText);
                    varietiesRecyclerView.setAdapter(vAdapter);

                }
            }
        });
    } //end of onCreate method

    protected void onSaveInstanceState(Bundle outstate){
        outstate.putStringArrayList("checked", checkList);
        //outstate.putStringArrayList("suitableVarietiesList", suitableVarList);
        super.onSaveInstanceState(outstate);
    }

    /*** load the zone___.json file ***/
    public String loadbyZone(String zone){ //loads the zonefile when the first try/catch statement is successful
        String json = null;
        try{
            InputStream zoneFile = getAssets().open("zone" + zone + ".json");
            int size = zoneFile.available();
            byte[] buffer = new byte[size];
            zoneFile.read(buffer);
            zoneFile.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**** load the todos.json file ***/
    public String loadToDos(){
        String toDoJson = null;
        try{
            InputStream todoFile = getAssets().open("todos.json");
            int size = todoFile.available();
            byte[] buffer = new byte[size];
            todoFile.read(buffer);
            todoFile.close();
            toDoJson = new String(buffer, "UTF-8");

        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return toDoJson;
    }

    public String loadBotanicalNames(){
        String json = null;
        try{
            InputStream botanicalNamesFile = getAssets().open("botanicalNames.json");
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

    public JSONObject getReadableNameFile(){
        String json = null;
        JSONObject readableNamesfile = new JSONObject();
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
        try{
            readableNamesfile = new JSONObject(json);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return readableNamesfile;
    }

    /****saves the user's variety data... ***/
    //@TargetApi(26)?
    public void saveVarietyData(View v){

        //todo: check that there are actually varieties to save, and then run the runnable thread,
        //otherwise, just skip it and return the user to the dashboard.

        if(checkList.size() > 0){
            runnableThread runnableThread = new runnableThread();
            runnableThread.run();
        }else{

            int duration = Toast.LENGTH_LONG;
            String savedmsg = "";
            if(checkList.size() == 0){
                savedmsg = "You did not save any varieties.";
            }else if (checkList.size() > 0){
                savedmsg = "Your varieties have been saved!";
            }
            Toast.makeText(context, savedmsg, duration).show();
            Intent intent = new Intent(this, Dashboard.class);
            startAlarmService();
            startActivity(intent);
        }
    } //end of saveData function for the clicking of the save button

    public void startAlarmService() {
        //build versions less than SDK version 26 (no notification channel needed)
        Context appContext = getApplicationContext();
        Intent alarmIntent = new Intent(appContext, updatedAlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(appContext, 43224, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    class runnableThread implements Runnable{
        @Override
        public void run() {
            ArrayList checkedVarietyArray = null;
            checkedVarietyArray = checkList; //gets the checklist from the db and the current activity
            JSONObject zoneFile;
            JSONArray zoneFileArray;
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            String yearStr = String.valueOf(year); //this is what you will add to the date strings

            /**PART 2: INSERT DATA INTO THE DATABASE*/

            /**insert the list of checked varieties from te activity into the VARIETY TABLE*/
            dataSource = new growmindDataSource(getApplicationContext());
            dataSource.open();
            dataSource.createVarietyList(checkedVarietyArray);

            /***...then process the data from the activity into a usable format for entry into the notification by date table*/
            try {

                /**get the zone file...*/
                zoneFile = new JSONObject(loadbyZone(userZone).trim()); //this loads the file corresponding to the user's zone
                zoneFileArray = zoneFile.getJSONArray("zone" + userZone);

                JSONObject readableNamesFile = new JSONObject();
                readableNamesFile = getReadableNameFile();

                /**this is what will be used to hold the data for the varietyevents table*/
                List<String[]> finalListOfGenericVarietyEvents = new ArrayList<String[]>(); //trying without the final keyword so as to allow for
                List<String[]> finalListOfImmediateVarietyEvents = new ArrayList<>();
                List<String[]> finalListOfArrestedVarietyEvents = new ArrayList<>();

                /**Loop through the zonefile...*/
                for (int i = 0; i < zoneFileArray.length(); i++) {
                    JSONObject varietyObj = (JSONObject) zoneFileArray.get(i);

                    /**read the id field of each object in the zonefile...*/
                    String varID = varietyObj.getString("id");

                    /**check to see if the id is in the checkedVarietyList from the activity...*/
                    if (checkedVarietyArray.contains(varID)) {

                        String baselineVarietyID= varID.replaceAll("[0-9]","").toLowerCase();

                        /**load the todoJSON object*/
                        JSONObject varietyTodoObj = toDoJSONObj.getJSONObject(baselineVarietyID);

                        List<String[]> genericVarietyEventList = new ArrayList<String[]>();
                        List<String[]> immediateVarietyEventsList = new ArrayList<String[]>();
                        List<String[]> arrestedVarietyEventsList = new ArrayList<String[]>();

                        if(varietyObj.has("round2")){

                            /**separate the variety object into two separate objects*/
                            JSONObject firstRoundKeys = (JSONObject) varietyObj.get("round1");
                            JSONObject secondRoundKeys = (JSONObject) varietyObj.get("round2");

                            /***adding generic lists to capture all of the dates, whether they have passed or not these will be stored in their own table in the db */
                            genericVarietyEventList = new createGenericVarietyEventList(varID,firstRoundKeys, secondRoundKeys, varietyTodoObj).getGenericVarietyEventList();

                            immediateVarietyEventsList = new createImmediateVarietyEventList(/**adding the readableNamesFile*/readableNamesFile,/**adding the todoJSONObject*/ toDoJSONObj,varietyObj, varID, firstRoundKeys,secondRoundKeys,varietyTodoObj, year, yearStr).getImmediateVarietyEventList();

                            arrestedVarietyEventsList = new createArrestedVarietyEventList(varID, firstRoundKeys, secondRoundKeys, yearStr, year).getArrestedVarietyEventList();

                        } else if(!varietyObj.has("round2")){

                            genericVarietyEventList = new createGenericVarietyEventList(varID,varietyObj, null, varietyTodoObj).getGenericVarietyEventList();
                            immediateVarietyEventsList = new createImmediateVarietyEventList(readableNamesFile, toDoJSONObj,varietyObj, varID, varietyObj,null,varietyTodoObj,year,yearStr).getImmediateVarietyEventList();
                            arrestedVarietyEventsList = new createArrestedVarietyEventList(varID, varietyObj, null,yearStr,year).getArrestedVarietyEventList();
                        }

                        /**loop through the generic variety List*/
                        for(int g = 0; g < genericVarietyEventList.size(); g++){
                            finalListOfGenericVarietyEvents.add(genericVarietyEventList.get(g));
                        }

                        /**loop through the immediate variety event list*/
                        for(int h = 0; h < immediateVarietyEventsList.size(); h++){
                            finalListOfImmediateVarietyEvents.add(immediateVarietyEventsList.get(h));
                        }

                        /**loop through the arrested variety Event list*/
                        for(int f = 0; f < arrestedVarietyEventsList.size(); f++){
                            finalListOfArrestedVarietyEvents.add(arrestedVarietyEventsList.get(f));
                        }

                    }//end of looping through specific variety events
                }//end of forLoop() through all varieties by zone

                dataSource.createGenericCalendar(finalListOfGenericVarietyEvents);
                dataSource.createImmediateEventsCalendar(finalListOfImmediateVarietyEvents);
                dataSource.createArrestedEventsCalendar(finalListOfArrestedVarietyEvents);

            }catch(JSONException ex){
                ex.printStackTrace();
            }

            dataSource.close();
            //since the following needs to be attached to the UI thread, use a handler//
            updateHandler.post(new Runnable() {
                @Override
                public void run() {
                    int duration = Toast.LENGTH_LONG;
                    String savedmsg = "";
                    if(checkList.size() == 0){
                        savedmsg = "You did not save any varieties.";
                    }else if (checkList.size() > 0){
                        savedmsg = "Your varieties have been saved!";
                    }
                    Toast.makeText(context, savedmsg, duration).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startAlarmService();
                    startActivity(intent);
                    checkList = null;
                }
            });
        }
    }//end of runnable thread
}