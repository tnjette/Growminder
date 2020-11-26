package com.tylerjette.growmindv05;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/***
 * the main activity will read from the database, then populate the UI with the appropriate layout
 * */

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = MainActivity.class.getSimpleName();
    //public static final String msg_from_MainActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                goForthAndBeMerry();
            }
        }, 4000);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                goForthAndBeMerry();
            }
        }, 4000);
    }

    public void goForthAndBeMerry(){
        Boolean hasLocation = false;
        Boolean hasVarieties = false;
        Boolean hasChannelID = false;
        String userVisitations = "0";

        SQLiteHelper dbHelper = new SQLiteHelper(this); //this essentially creates the database tables
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] userVisitationProjection = {userVisitationContract.visitationEntry.COLUMN_USER_VISITS};
        String userVisitationSelection = userVisitationContract.visitationEntry.COLUMN_USER_VISITS;

        String[] locationProjection = {locationContract.locationEntry.COLUMN_ZIPCODE};
        String locationSelection = locationContract.locationEntry.COLUMN_ZIPCODE;

        String[] varietyProjection = {varietyContract.varietyEntry.COLUMN_VARIETY_ID};

        String[] channelIDProjection = {channelIDContract.channelIDEntry.COLUMN_CHANNEL_ID};
        String channelIDSelection = channelIDContract.channelIDEntry.COLUMN_CHANNEL_ID;

        try{
            Cursor cursor = db.query(
                    userVisitationContract.visitationEntry.TABLE_NAME,
                    userVisitationProjection,
                    userVisitationSelection,
                    null,
                    null,
                    null,
                    null
            );
            while(cursor.moveToNext()){
                userVisitations = cursor.getString(cursor.getColumnIndexOrThrow(userVisitationContract.visitationEntry.COLUMN_USER_VISITS));
            }
            cursor.close();
        }catch(SQLException ex){
            //do nothing
        }
        //Log.d(TAG, "USER VISITS LOGGED : " + userVisitations);

        try{
            Cursor cursor = db.query(
                    locationContract.locationEntry.TABLE_NAME,
                    locationProjection,
                    locationSelection,
                    null,
                    null,
                    null,
                    null
            );

            while(cursor.moveToNext()){
                hasLocation = true;
            }
            cursor.close();

        }catch(SQLException ex){
            //do nothing, Boolean hasLocation remains false
        }

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
                hasVarieties = true;
            }
            cursor.close();
        }catch(SQLException ex){
            //do nothing, Boolean hasVarieties remains false
        }

        try{
            Cursor cursor = db.query(
                    channelIDContract.channelIDEntry.TABLE_NAME,
                    varietyProjection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while(cursor.moveToNext()){
                hasChannelID = true;
            }
            cursor.close();
        }catch(SQLException ex){
            //do nothing, Boolean hasVarieties remains false
        }

        growmindDataSource dataSource = new growmindDataSource(this);
        dataSource.open();
        /**then update the visit table*/

        if (userVisitations.equals("0")){
            //Log.d(TAG, "user has logged in " + userVisitations + " times, therefore CREATING a new USER VISITATION table in the db.");
            //meaning there have been no other visits
            //need to create the visits table and then direct the user to the instructions activity
            dataSource.createUserVisitationTable(0);
        }else{
            //Log.d(TAG, "user has logged in " + userVisitations + " times, therefore UPDATING the USER VISITATION table in the db.");

            int updatedUserVisits = Integer.parseInt(userVisitations);
            dataSource.updateUserVisitationTable(updatedUserVisits);
        }

        /**adding section to create the channel ID table*/
        if (!hasChannelID){
            /**needs to specify the build.version is greater than 26*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                //then retreive the string channel code and put it into the table
                String channel_ID = getString(R.string.notification_channel_id);
                dataSource.createChannelIDTable(channel_ID);

                createNotificationChannel(channel_ID);

                //and create the
            } else {
                //no channel ID needed
            }

        }else {
            //already has channel ID in place, or is not necessary
        }
        dataSource.close();


        /**new*/
        Intent goToDashboard = new Intent(this, Dashboard.class);
        if (hasLocation && hasVarieties){
            goToDashboard.putExtra("msg_from_Main", "hasLocation&Varieties");
            startActivity(goToDashboard);
        }else if(hasLocation && !hasVarieties){
            goToDashboard.putExtra("msg_from_Main", "addVarietiesActivity");
            startActivity(goToDashboard);
        }else if(!hasLocation && !hasVarieties){
            goToDashboard.putExtra("msg_from_Main", "addLocationActivity");

            /**in this case, the user has nothing in the db. So they will be directed to an informational activity,
             * with instruction about how the app works. From here they can continue to the add location activity and onward.
             *
             * */
            //Log.d(TAG, "GOING TO APP INSTRUCTIONS from main activity");
            Intent goToAppInstructions = new Intent(this, instructions.class);
            startActivity(goToAppInstructions);
            //startActivity(goToDashboard);
        }else if (!hasLocation && hasVarieties){
            goToDashboard.putExtra("msg_from_Main", "has varieties but no Location");
            startActivity(goToDashboard);
        }
    }

    private void createNotificationChannel(String channel_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
