package com.tylerjette.growmindv05;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.KeyboardView;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addLocationActivity extends AppCompatActivity {

    private growmindDataSource dataSource; //added
    private static Boolean hasOldVarieties = false;
    private static ArrayList<String> oldVarietyList = new ArrayList(); //in case there are old varieties that need to be reconstructed given a change in the location

    SQLiteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){

            LinearLayout location_frame = findViewById(R.id.location_frame);
            location_frame.setOrientation(LinearLayout.HORIZONTAL);

            FrameLayout editTextFrame = findViewById(R.id.editTextContainerFrame);
            LinearLayout.LayoutParams editTextFrameLayoutParams = new LinearLayout.LayoutParams(editTextFrame.getLayoutParams());
            editTextFrameLayoutParams.setMargins(0,0,16,0);
            editTextFrameLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            editTextFrameLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            editTextFrame.setLayoutParams(editTextFrameLayoutParams);

            FrameLayout saveButtonFrame = findViewById(R.id.saveLocationButtonContainerFrame);
            LinearLayout.LayoutParams saveButtonFrameLayoutParams = new LinearLayout.LayoutParams(saveButtonFrame.getLayoutParams());
            saveButtonFrameLayoutParams.setMargins(16,0,0,0);
            saveButtonFrameLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            saveButtonFrameLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            saveButtonFrame.setLayoutParams(saveButtonFrameLayoutParams);
        }

        android.support.v7.widget.Toolbar activityToolbar = findViewById(R.id.add_location_custom_toolbar);
        setSupportActionBar(activityToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView titleUI = findViewById(R.id.addLocationToolbarTitle);
        titleUI.setText(R.string.add_location_action_bar_title);

        sqliteHelper = new SQLiteHelper(this);
        //SQLiteHelper sqliteHelper = SQLiteHelper.getInstance(this); //this is maybe a more efficient way for instantiate an SQLiteHelper?

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        //projection
        String varietyProjection[] = {varietyContract.varietyEntry.COLUMN_VARIETY_ID};

        try{
            Cursor cursor = db.query(
                    varietyContract.varietyEntry.TABLE_NAME,
                    varietyProjection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while(cursor.moveToNext()){
                //if there are items in this database table, then it will allow you to target this condition
                hasOldVarieties = true;
                String oldVariety = cursor.getString(cursor.getColumnIndexOrThrow(varietyContract.varietyEntry.COLUMN_VARIETY_ID));
                oldVarietyList.add(oldVariety);
            }
            cursor.close();
        }catch(SQLException ex){
        }

        dataSource = new growmindDataSource(this); //added

    }

    /**user clicks save...*/
    public void saveLocationData(View view){

        //final Intent intent = new Intent(this, Dashboard.class);

        EditText userLocationInput = findViewById(R.id.editText);
        String userInput = userLocationInput.getText().toString();
        String regex = "^(\\d{5})$";
        Pattern validZipcodePattern = Pattern.compile(regex);
        Matcher matcher = validZipcodePattern.matcher(userInput);
        boolean correctFormat = matcher.find();

        if(correctFormat){
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
            }

            /**this should really be in a runnable thread*/
            runnableThread correctFormatRunnable = new runnableThread(userInput);
            correctFormatRunnable.run();

        } else {
            /**if user location input is incorrect format*/
            Context context = this;
            CharSequence errorMsg = "Please enter a valid 5-digit US zipcode.";
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_add_location_invalidentry, (ViewGroup) findViewById(R.id.custom_toast_container_invalid));
            TextView text = layout.findViewById(R.id.customToast_invalidEntry_text);
            text.setText(errorMsg);
            int orientation = getResources().getConfiguration().orientation;
            int yOffset;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                yOffset = 140;
            }else{
                yOffset = 250;
            }
            Toast invalid = Toast.makeText(context, "", Toast.LENGTH_LONG);
            invalid.setGravity(Gravity.TOP, 0,yOffset);
            invalid.setView(layout);
            invalid.show();
        }
    }

    public String loadZipZoneJSONFromAsset() {
        String json = null;
        try {
            InputStream zipZone = getAssets().open("zipZone.json");
            int size = zipZone.available();
            byte[] buffer = new byte[size];
            zipZone.read(buffer);
            zipZone.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //load zipPlace.json
    public String loadZipPlaceJSONFromAsset(){
        String json = null;
        try{
            InputStream zipPlace = getAssets().open("zipPlace.json");
            int size = zipPlace.available();
            byte[] buffer = new byte[size];
            zipPlace.read(buffer);
            zipPlace.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    class runnableThread implements Runnable{
        String userInput;
        runnableThread(String userInput){
            this.userInput = userInput;
        }
        @Override
        public void run() {

            /**adding*/

            /***everything below is eligible for refactoring*/
            Boolean zipZoneFileCheck = false;
            Boolean zipPlaceFileCheck = false;
            String zone = "";
            String placeName = "";

            try {
                JSONObject zipZoneObj = new JSONObject(loadZipZoneJSONFromAsset().trim()); //zipZone JSON object
                JSONObject zipPlaceObj = new JSONObject(loadZipPlaceJSONFromAsset().trim()); //zipPlace JSON object
                JSONArray zipZoneArray = zipZoneObj.getJSONArray("zipZones");
                JSONArray zipPlaceArray = zipPlaceObj.getJSONArray("zipPlaces");

                //set a trigger to check that the user's input actually exists in the json files available
                for(int i=0;i<zipZoneArray.length();i++){
                    //JSONObject p = zipZoneArray.get(i).getString()
                    String p = zipZoneArray.getString(i);
                    if (p.contains(userInput)){
                        JSONObject d = (JSONObject) zipZoneArray.get(i);
                        String dString = d.getString("zone");

                        StringBuilder onlyNumbers = new StringBuilder();
                        for(int k = 0; k < dString.length(); k++){
                            if(Character.isDigit(dString.charAt(k))){
                                onlyNumbers.append(dString.charAt(k));
                            }
                        }
                        zone = onlyNumbers.toString();

                        zipZoneFileCheck = true;
                    }
                }
                for(int j = 0;j<zipPlaceArray.length(); j++){
                    String n = zipPlaceArray.getString(j);
                    if (n.contains(userInput)){
                        //zipPlaceSuccess = zipPlaceArray.getString(j).toString();
                        JSONObject f = (JSONObject) zipPlaceArray.get(j);
                        String cityString = f.getString("City");
                        String stateString = f.getString("State");
                        placeName = cityString + ", " + stateString;
                        zipPlaceFileCheck = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //if the activity failed to load and read the files, then go to another activity that brings
                //the user back to the main page.
            }

            if((zipZoneFileCheck) && (zipPlaceFileCheck)){
                //create a toast activity that says "data saved" and then returns the user to their dashboard
                Context context = getApplicationContext();
                CharSequence savedMsg = "Your location has been saved!";
                int duration = Toast.LENGTH_SHORT;
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_add_location_succeslocationsaved, (ViewGroup) findViewById(R.id.custom_toast_container_success));

                TextView text = layout.findViewById(R.id.location_saved_success);
                text.setText(savedMsg);

                int orientation = getResources().getConfiguration().orientation;
                int yOffset;
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    yOffset = 140;
                }else{
                    yOffset = 250;
                }
                Toast savedToast = Toast.makeText(context, savedMsg, duration);
                savedToast.setView(layout);
                savedToast.setGravity(Gravity.BOTTOM, 0,yOffset);
                savedToast.show();


                final Intent intent = new Intent(context, Dashboard.class);

                if(hasOldVarieties){

                    /**all this needs to do is simply...
                     *
                     *          0. notify the user that they are going to lose their current list of varieties and will have to resave them. Do they want to continue?
                     *           (use this opportunity to create a fragment for this UI blip)
                     *          1. delete the notifications from the Calendar Table,
                     *          2. delete the immediate notifications from the Immediate notifications table
                     *          3. delete the arrested notifications from the arrested notifications table
                     *          4. obviously, reset the location table,
                     *          5. send the user to the addVarieties activity
                     *          */

                    SQLiteDatabase writableDB = sqliteHelper.getWritableDatabase();
                    writableDB.execSQL(varietyContract.DELETE_OLD_VARIETY_DATA);
                    writableDB.execSQL(calendarContract.DELETE_OLD_CALENDAR_DATA);
                    writableDB.execSQL(immediateCalendarContract.DELETE_OLD_IMMEDIATE_CALENDAR_DATA);
                    writableDB.execSQL(arrestedEventsCalendarContract.DELETE_OLD_ARRESTED_CALENDAR_DATA);
                    writableDB.close();

                }
                dataSource.open();
                dataSource.createLocation(userInput, zone, placeName);
                dataSource.close();
                intent.putExtra("msg_from_addLocation", "thenAddVarieties");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        startActivity(intent);
                    }
                }, 2000);
            } else {
                /**create custom toast container*/
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_add_location_entrynotfound,
                        (ViewGroup) findViewById(R.id.custom_toast_container_notfound));
                /**set toast properties*/
                TextView text = layout.findViewById(R.id.customToast_entryNotFound_text);
                text.setText(R.string.errorMsg_entryNotFound);
                int orientation = getResources().getConfiguration().orientation;
                int yOffset;
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    yOffset = 140;
                }else{
                    yOffset = 250;
                }
                /**for repeating*/
                for (int i=0; i < 2; i++)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,yOffset);
                    toast.setView(layout);
                    toast.show();
                }
            }
        }//end of run();
    }
}
