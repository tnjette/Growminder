package com.tylerjette.growmindv05;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * this activity is just going to serve as an extension to the already present learnMoreAboutXVariety activity. Instead
 * of having access to the calendar for the variety in the learnMore activity, the information there is being broken into two parts,
 * simplifying the layout, and also allowing for a further simplification of the information being conveyed in the learnMoreActivity.
 * The information compiled at runtime for each variety will be conveyed using more of a bullet list, with each category having its own
 * segment of the UI.
 * **/

/**todo: differentiate the logic to accommodate navigation to the activity from either the addVarieties activity, learnMore... activity, or by
 * way of the eventCalendar activity, which bypasses the learnMoreActivity altogether.
 * */

public class learnMoreAboutXVariety_Calendar extends AppCompatActivity{

    //public static String TAG = learnMoreAboutXVariety_Calendar.class.getSimpleName();
    private RecyclerView recyclerView;
    private learnMoreActivityCalendarAdapter calendarAdapter;
    ArrayList<String> transferChecklist = new ArrayList<>();
    private String varietyID;
    private String varietyName;
    private String previousActivity = "";
    Intent activityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        activityIntent = getIntent();
        if(activityIntent.hasExtra("previousActivity")){
            previousActivity = activityIntent.getStringExtra("previousActivity");
        }

        if(activityIntent.hasExtra("transferChecklist_ext")){
            //this means the user us coming from the addVarietiesActivity, by way of the learnMoreActivity, and this is the checklist from their active checklist to be returned
            //to where it came from
            transferChecklist = activityIntent.getStringArrayListExtra("transferChecklist_ext");
        }

        Bundle calendarBundle = activityIntent.getBundleExtra("calendarBundle");
        ArrayList<learnMoreCalendarEvent> learnMoreCalendarEventList = (ArrayList<learnMoreCalendarEvent>)calendarBundle.getSerializable("cal");

        //retrieve the rest of the stringextras
        varietyName = activityIntent.getStringExtra("varietyName");
        String baselineID = activityIntent.getStringExtra("baselineID");
        varietyID= activityIntent.getStringExtra("varietyID");
        String botanicalName = activityIntent.getStringExtra("botanicalName");

        setContentView(R.layout.learn_more_activity_calendar);

        //set orientation reconfig
        //int orientation = getResources().getConfiguration().orientation;
        /*if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            //reconfig for landscape orientation
        }*/

        //set toolbar interface
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.learnMoreActivityCal_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set activity title
        TextView varietyNameTitle = findViewById(R.id.learnMoreVarietyCal_title);
        varietyNameTitle.setText(varietyName);

        //set activity subtitle (botanical name)
        //TextView varBotanicalName = findViewById(R.id.learnMoreVarietyCal_botanicalName);
        //varBotanicalName.setText(botanicalName);

        //set background image
        ImageView baseIMG = findViewById(R.id.learnMoreCal_mainImg);
        int varImg = new getImage(baselineID).getImage();
        baseIMG.setImageResource(varImg);

        //set recyclerview
        recyclerView = findViewById(R.id.learnMoreVarietyCal_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        calendarAdapter = new learnMoreActivityCalendarAdapter(this, learnMoreCalendarEventList); //need to create this list from the zoneX json file
        recyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home :
                /***I want the logic to revolve around the string previousActivity*/
                //if(getIntent().hasExtra("varietiesInDBPass")){
                if(previousActivity.equals("addVarieties")){
                   //send the user back to the learnMoreAboutXVariety activity (extension of addvarieties > learnmore > this)
                    Intent intent = new Intent(this, learnMoreAboutXVariety.class);
                    intent.putExtra("varietyID", varietyID);
                    intent.putExtra("varietyName", varietyName);
                    intent.putStringArrayListExtra("transferChecklist", transferChecklist);
                    intent.putExtra("previousActivity", "addVarieties");
                    startActivity(intent);
                }else if (previousActivity.equals("currentVarieties")){
                    //send the user back to the eventCal activity (extension of the eventCal activity)
                    Intent intent = new Intent(this, learnMoreAboutXVariety.class);
                    intent.putExtra("varietyID", varietyID);
                    intent.putExtra("varietyName", varietyName);
                    intent.putExtra("previousActivity", previousActivity);
                    startActivity(intent);
                }else if (previousActivity.equals("upcomingNotifications")){
                    Intent intent = new Intent(this, eventsCalendarActivity.class);
                    intent.putExtra("varietySpec", varietyID);
                    intent.putExtra("varietyName", varietyName);
                    intent.putExtra("previousActivity", previousActivity);
                    intent.putExtra("isFallCycle", activityIntent.getStringExtra("isFallCycle"));
                    intent.putExtra("eventDetail", activityIntent.getStringExtra("upcomingEventDetail"));
                    intent.putExtra("eventCycle", activityIntent.getBooleanExtra("eventCycle", false));
                    startActivity(intent);
                }else if(previousActivity.equals("dashboard")){
                    Intent intent = new Intent(this, eventsCalendarActivity.class);
                    //intent.putExtra("varietyID", varietyID);
                    /**change the name of this object to varietySpec*/
                    intent.putExtra("varietySpec", varietyID);
                    intent.putExtra("varietyName", varietyName);
                    intent.putExtra("previousActivity", previousActivity);
                    intent.putExtra("eventCycle", activityIntent.getBooleanExtra("eventCycle", false));
                    intent.putExtra("isFallCycle", activityIntent.getStringExtra("isFallCycle"));
                    intent.putExtra("eventDetail", activityIntent.getStringExtra("upcomingEventDetail"));
                    //Log.d(TAG, "event cycle being passed back to the previous activity : " + activityIntent.getStringExtra("eventCycle"));
                    startActivity(intent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

