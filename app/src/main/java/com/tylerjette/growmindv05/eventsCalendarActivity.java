package com.tylerjette.growmindv05;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.provider.CalendarContract;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tylerjette on 3/12/18.
 * this activity will be executable from the dashboard activity, and depending on the intent extra sent from what variety the user clicks on in the
 * dashboard activity, the UI will filter the events per that activity.
 */

public class eventsCalendarActivity extends AppCompatActivity implements descriptionExpansionFragment.OnFragmentInteractionListener{

    private static String TAG = eventsCalendarActivity.class.getSimpleName();
    private static String varietyNameStash;
    private static String varietyID;
    private String previousActivity;
    private Handler mHandler = new Handler();
    private FragmentManager fragMan;
    private int UIStateTracker = 0; //so, pre-onCreate state = 0;
    public boolean pieClick = false;

    private int counter = 0;

    /**the following are implemented to accommodate the UI flexibility*/
    TextView mostRecentEvent;
    LinearLayout eventActivityAccommodatingTextFrame;
    ConstraintLayout eventCalActivity_mainUIFrame;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int height = displayMetrics.heightPixels;

    String descriptionText = "";
    String descriptionOfInterest = "";
    int finalVarietyImage;

    //ImageView circleImage;
    //createCircleImage createCircleImageObj;


    /***since this activity is accessible from two different activities, the up-nav button needs to be flexible, and direct the user in two directions depending on where they
     * came from. Where they came from is made evident by the intent extras introduced in the preceding activities. The following method does just that.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                if(getIntent().hasExtra("upcomingEventTitle")){
                    Intent intent = new Intent(this, upcomingNotificationsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (getIntent().hasExtra("currentEventTitle")){
                    Intent intent = new Intent(this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (getIntent().hasExtra("previousActivity")){
                    if(getIntent().getStringExtra("previousActivity").equals("dashboard")){
                        Intent intent = new Intent(this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }if(getIntent().getStringExtra("previousActivity").equals("upcomingNotifications")){
                        Intent intent = new Intent(this, upcomingNotificationsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            /*case R.id.seeEntireCalendarButton:
                //open the variety calendar activity
                Intent intent = new Intent(this, learnMoreAboutXVariety.class);
                intent.putExtra("varietyName", varietyNameStash);
                intent.putExtra("varietyID", varietyID);
                startActivity(intent);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //getSupportActionBar().hide();
        setContentView(R.layout.activity_events_calendar);

        //set horizontal orientation layout
        int screenOrientation = getResources().getConfiguration().orientation;


        /***configuration = landscape*/
        if(screenOrientation == Configuration.ORIENTATION_LANDSCAPE){

            //adjust pieBlock
            RelativeLayout rel = findViewById(R.id.pieUIBlock);
            ConstraintLayout.LayoutParams newPieBlockLayout = new ConstraintLayout.LayoutParams(rel.getLayoutParams());
            newPieBlockLayout.height = ViewGroup.LayoutParams.MATCH_PARENT;
            newPieBlockLayout.width = 0;
            newPieBlockLayout.setMargins(0,80,0,80);
            rel.setLayoutParams(newPieBlockLayout);

            //adjust textBlock
            LinearLayout textLayout = findViewById(R.id.eventActivityAccommodatingTextFrame);
            ConstraintLayout.LayoutParams newTextLayout = new ConstraintLayout.LayoutParams(textLayout.getLayoutParams());
            newTextLayout.height = ViewGroup.LayoutParams.MATCH_PARENT;
            //newTextLayout.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            newTextLayout.width = 0;
            textLayout.setGravity(Gravity.CENTER_VERTICAL);
            newTextLayout.setMargins(0, 80, 0, 80);
            textLayout.setLayoutParams(newTextLayout);
            ConstraintSet cs = new ConstraintSet();
            ConstraintLayout cl = findViewById(R.id.eventCalActivity_mainUIFrame);
            cs.clone(cl);
            cs.constrainPercentWidth(R.id.pieUIBlock, 0.5f);
            cs.constrainPercentWidth(R.id.eventActivityAccommodatingTextFrame, 0.5f);
            cs.connect(R.id.pieUIBlock, ConstraintSet.LEFT, R.id.eventCalActivity_mainUIFrame, ConstraintSet.LEFT,0);
            cs.connect(R.id.pieUIBlock, ConstraintSet.RIGHT, R.id.eventActivityAccommodatingTextFrame, ConstraintSet.LEFT,0);
            cs.connect(R.id.eventActivityAccommodatingTextFrame, ConstraintSet.LEFT, R.id.pieUIBlock, ConstraintSet.RIGHT,0);
            cs.connect(R.id.eventActivityAccommodatingTextFrame, ConstraintSet.RIGHT, R.id.eventCalActivity_mainUIFrame, ConstraintSet.RIGHT,0);
            cs.applyTo(cl);
        }else {
            //nothing
        }

        height = displayMetrics.heightPixels;

        //get the android build version
        //int buildVersion = android.os.Build.VERSION.SDK_INT;

        //get the intent
        final Intent activityIntent = getIntent();
        String intentBaselineIDStr = "";
        String varietyName= "";
        String upcomingEventTitle = "";
        String mostRecentEventTitle = "";
        String upcomingEventDetail = "";

        Boolean isFallCycle = false;

        if(activityIntent.hasExtra("previousActivity")){
            previousActivity = activityIntent.getStringExtra("previousActivity");
            if(previousActivity.equals("dashboard")){
                //the user can skip the learnMore actviitvy
            }
        }

        /**new*/
        if(activityIntent.hasExtra("currentEventTitle")){
            /**then basically set the UI up for the current notifications spec
             * this will direct where to programmatically set where the back button navigates to in the UI.
             *
             * ie: set the "current event title" and
             *
             *
             * */
        }
        /***/

        if(activityIntent.hasExtra("varietySpec")){
            intentBaselineIDStr = activityIntent.getStringExtra("varietySpec");
            //Log.d(TAG, "intentStr  (varietySpec) : "+intentBaselineIDStr);
            varietyID = intentBaselineIDStr;
        }

        if(activityIntent.hasExtra("varietyName")){
            varietyName = activityIntent.getStringExtra("varietyName");
            //Log.d(TAG, "varietyName : " + varietyName);
            varietyNameStash = varietyName;
        }

        if(activityIntent.hasExtra("upcomingEventTitle")){
            upcomingEventTitle = activityIntent.getStringExtra("upcomingEventTitle");
            //this Should trigger whether the activity calculates the dates for this variety looking into the future,
            //instead of all of the dates being based on the "current Calendar.YEAR, which if it's december, then most things will be in the past.
            //I guess the entire calendar cycle would be reset after the harvest has closed, which may be kind of arbitrary and ambiguous, since the
            //time frame for the extent of the "harvest" period is purely theroretical, in which the end date is merely projected 1 month or 2 months after
            //the beginning date. so, I don't know, maybe the first of the year is actually the best turn around time, since the earliest notification is actually
            //january 5th, in the case of brussels sprouts, I believe.

            //if the upcomingEventTitle exists, then the layout needs to orient towards the user's understanding that the information is the next upcoming notice.
            //Log.d(TAG, "upcoming event title = " + upcomingEventTitle);

        }

        if(activityIntent.hasExtra("mostRecentEventTitle")){
            mostRecentEventTitle = activityIntent.getStringExtra("mostRecentEventTitle");

            //Log.d(TAG, "most recent Event title = "+ mostRecentEventTitle);
        }
        if(activityIntent.hasExtra("eventDetail")){
            upcomingEventDetail = activityIntent.getStringExtra("eventDetail");
            descriptionText = activityIntent.getStringExtra("eventDetail");
            descriptionOfInterest = descriptionText;
        }

        if(activityIntent.hasExtra("eventCycle")){
            isFallCycle = activityIntent.getBooleanExtra("eventCycle", false);
        }

        //reference db
        SQLiteHelper helper = new SQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String queryprojection[] = {calendarContract.calendarEntry.COLUMN_VARIETY_ID, //changed to variety_ID
                calendarContract.calendarEntry.COLUMN_EVENT_DATE,
                calendarContract.calendarEntry.COLUMN_EVENT_TITLE,
                calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION};
        StringBuilder querySelection = new StringBuilder();
        querySelection.append(calendarContract.calendarEntry.COLUMN_VARIETY_ID);  //changed to varietyID
        querySelection.append("=?");
        String querySelectionStr = querySelection.toString();
        String querySelectionArgs[] = {intentBaselineIDStr};

        //ArrayList<Long> dateArr = new ArrayList<>();
        List<varietyCalendarEvent> listOfVarietyEvents = new ArrayList<>();

        Cursor cursor = db.query(
                calendarContract.calendarEntry.TABLE_NAME,
                queryprojection,
                querySelectionStr,
                querySelectionArgs,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){
            /**first, get each of the events from the db*/
            String eventTitle = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_TITLE));
            String eventDate = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DATE));
            String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow(calendarContract.calendarEntry.COLUMN_EVENT_DESCRIPTION));

            /**and put them into an object*/
            //varietyCalendarMap.put(eventDate, eventDescription);
            varietyCalendarEvent varCalEv = new varietyCalendarEvent(eventTitle,eventDescription, eventDate);
            listOfVarietyEvents.add(varCalEv);
        }
        cursor.close();



        /**then...get all of the dates in order to format the pie chart, but now, you need to format the percentages of the pie chart based on
         * how many days each phase of the growing calendar lasts.
         * */

        /**
         * first, set the beginning and ending date of each phase, then each of the dates into a milliseconds long , then subtract them and then divide
         * by the number of milliseconds in a day which is 86400000
         * */

        //List<varietyCalendarEvent> round1List = new ArrayList<>();
        //List<varietyCalendarEvent> round2List = new ArrayList<>(); //this may not get used if there is no second round

        Boolean hasRound1SeedlingPhase = false;
        Boolean hasRound2SeedlingPhase = false;
        Boolean hasRound1PlantingDirectPhase = false;
        Boolean hasRound2PlantingDirectPhase=false;
        Boolean hasRound1HardenOffPhase=false;
        Boolean hasRound2HardenOffPhase=false;
        Boolean hasRound1TransplantingPhase=false;
        Boolean hasRound2TransplantingPhase=false;
        Boolean hasRound1HarvestEndDate = false;
        Boolean hasRound2HarvestEndDate = false;

        Boolean cycleHasNotStarted = false;
        Boolean cycleHasPassed = false;

        //Boolean round2 = false;

        //final LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase1 = new LinkedHashMap<>();
        //final LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase2 = new LinkedHashMap<>();

        /**configure pieChart*/
        final PieChart pieChart = findViewById(R.id.animatedEventsCalendarPieView);
        float pieChartRotationValue = 0.0f;
        pieChart.setClickable(false);
        pieChart.setFocusable(true);
        pieChart.setNoDataText("");
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setRotationEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setNoDataText("");
        pieChart.getDescription().setText("");

        /**add the year scale piechart*/
        final PieChart refChart = findViewById(R.id.yearScale);
        refChart.setClickable(false);
        refChart.setFocusable(true);
        refChart.setNoDataText("");
        refChart.setHoleColor(Color.TRANSPARENT);
        refChart.setTransparentCircleRadius(61f);
        refChart.setRotationEnabled(false);
        refChart.getLegend().setEnabled(false);
        refChart.setNoDataText("");
        refChart.getDescription().setText("");

        /**create pieChart dataEntry array*/
        ArrayList<PieEntry> pieChartDataEntries = new ArrayList<>();

        /**create the refChart dataEntry array, Note that this will either be 1, 2, 3, 4, or 5 entries, depending on how many years
         * the cycle spans*/
        ArrayList<PieEntry> refChartDataEntries = new ArrayList<>();

        /**create color array for the main pie chart*/
        final ArrayList<Integer> colors = new ArrayList<>();

        /**create color array for the refChart pie chart*/
        final ArrayList<Integer> refChartColors = new ArrayList<>();


        /**break the logic into whether or not it's the spring or fall cycle*/
        if(!isFallCycle){
            final LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase1 = new LinkedHashMap<>();

            String currentPhaseName1 = "";
            String beginningPhaseName = "";

            int beginningOfCycleYear; //these are used later to calc the cycle length in total.
            String beginningOfCycleDate = "";
            int endingOfCycleYear; //these are used later to calc the cycle length in total.
            String endOfCycleDate = "";
            //meaning this is entirely for the spring cycle


            for(int i = 0; i < listOfVarietyEvents.size(); i++) {  //loop through the list of events//each of the events should refer to a specific variety

                //Log.d(TAG, "Here is a varietyEvent from the list: " + listOfVarietyEvents.get(i).getEventDate() + " , " + listOfVarietyEvents.get(i).getEventTitle());
                //break the list into two parts if there are 2 rounds
                varietyCalendarEvent varCal = listOfVarietyEvents.get(i);
                String eventTitle = varCal.getEventTitle();

                /**retrieve the event title to determine the Boolean values for each phase*/
                if (eventTitle.equals("startInside")) {
                    String[] seedPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    beginningOfCycleDate = varCal.getEventDate();
                    varietyPhase1.put("Seedlings", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Seedlings").put("beginning", seedPhaseBeginning);
                    hasRound1SeedlingPhase = true;
                    beginningPhaseName = "Seedlings";
                } else if (eventTitle.equals("startHardenOff")) {
                    String[] seedPhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                    String[] hardeningPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase1.get("Seedlings").put("end", seedPhaseEnding);
                    varietyPhase1.put("Hardening-off seedlings", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Hardening-off seedlings").put("beginning", hardeningPhaseBeginning);
                    hasRound1HardenOffPhase = true;
                } else if ((eventTitle.equals("startOutside")) || (eventTitle.equals("startOutsideCovered"))) {

                    /**since the hardening off phase may or may not precede the transplanting phase, then you have to check the boolean value to see if the
                     * hardening off phase even exists and proceed from there.
                     * */
                    hasRound1TransplantingPhase = true;

                    //figure out if this ends the hardening phase, or the startInside phase
                    if (hasRound1HardenOffPhase) {
                        String[] hardeningPhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                        varietyPhase1.get("Hardening-off seedlings").put("end", hardeningPhaseEnding);
                    } else if (!hasRound1HardenOffPhase) {
                        String[] seedlingPhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                        varietyPhase1.get("Seedlings").put("end", seedlingPhaseEnding);
                    }
                    String[] transplantingPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase1.put("Transplanting", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Transplanting").put("beginning", transplantingPhaseBeginning);

                } else if ((eventTitle.equals("directOutside")) || (eventTitle.equals("directOutsideCovered"))) {
                    hasRound1PlantingDirectPhase = true;
                    beginningOfCycleDate = varCal.getEventDate();
                    String[] directPlantingPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase1.put("Planting", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Planting").put("beginning", directPlantingPhaseBeginning);
                    beginningPhaseName = "Planting";
                } else if (eventTitle.equals("stopOutside")) {
                    String[] plantingPhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                    if (hasRound1TransplantingPhase) {
                        varietyPhase1.get("Transplanting").put("end", plantingPhaseEnding);
                    } else if (hasRound1PlantingDirectPhase) {
                        varietyPhase1.get("Planting").put("end", plantingPhaseEnding);
                    }
                    String[] growingPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase1.put("Growing", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Growing").put("beginning", growingPhaseBeginning);
                } else if (eventTitle.equals("startHarvest")) {
                    String[] growingPhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                    String[] harvestPhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase1.get("Growing").put("end", growingPhaseEnding);

                    /**configure the duration of the harvest time, depending on whether there is a discrete stopharvest date*/
                    varietyPhase1.put("Harvesting", new LinkedHashMap<String, String[]>());
                    varietyPhase1.get("Harvesting").put("beginning", harvestPhaseBeginning);
                    /**end harvest duration configuration*/
                } else if (eventTitle.equals("stopHarvest")) {
                    hasRound1HarvestEndDate = true;
                    String[] harvestEnd = {varCal.getEventTitle(), varCal.getEventDate()};
                    varietyPhase1.get("Harvesting").put("end", harvestEnd);
                    endOfCycleDate = varCal.getEventDate();
                }
            }

            /**check that there is a end date for the harvest phase, and if not, then dynamically create one*/
            if(!hasRound1HarvestEndDate){

                String harvestBeginning = varietyPhase1.get("Harvesting").get("beginning")[1];
                Calendar now = Calendar.getInstance();
                String[] harvestBeginningSplit = harvestBeginning.split(",");

                Calendar beginningCal = Calendar.getInstance();

                int beginningYear;
                int beginningMonth = 0;
                int beginningDay = 0;

                int endingYear;
                int endingMonth = 0;
                int endingDay = 0;

                beginningYear = now.get(Calendar.YEAR);

                if(harvestBeginningSplit.length == 2){
                    beginningMonth = Integer.valueOf(harvestBeginningSplit[0]) -1;
                    beginningDay = Integer.valueOf(harvestBeginningSplit[1]);
                } else if(harvestBeginningSplit.length > 2){

                    if(harvestBeginningSplit[0].equals("n")){
                        beginningYear = beginningYear + 1;
                    }else if(harvestBeginningSplit[0].equals("nn")){
                        beginningYear = beginningYear + 2;
                    }else if (harvestBeginningSplit[0].equals("nnn")){
                        beginningYear = beginningYear + 3;
                    }else if (harvestBeginningSplit[0].equals("nnnn")){
                        beginningYear = beginningYear + 4;
                    }else if(harvestBeginningSplit[0].equals("nnnnn")){
                        beginningYear = beginningYear + 5;
                    }

                    beginningMonth = Integer.valueOf(harvestBeginningSplit[1]) -1;
                    beginningDay = Integer.valueOf(harvestBeginningSplit[2]);
                }

                //then you can set the calendar object and
                beginningCal.set(beginningYear, beginningMonth, beginningDay);

                //then adjust the date one month into the future
                beginningCal.add(Calendar.DATE, 30);

                int yearVal = beginningCal.get(Calendar.YEAR);
                int monthVal = beginningCal.get(Calendar.MONTH) + 1;
                int dayVal = beginningCal.get(Calendar.DATE);

                String endCalStr = String.valueOf(yearVal) + "," + String.valueOf(monthVal) + "," + String.valueOf(dayVal);
                String[] endSubmission = {"stopHarvest", endCalStr};
                varietyPhase1.get("Harvesting").put("end", endSubmission);

                endOfCycleDate = endCalStr;

            }else {
                //don't do anything because the end date has already been submitted
            }

            final LinkedHashMap<String,String> round1HashMap = new LinkedHashMap<>();

            //Boolean isInPhase1 = false;

            String hasCurrentPhaseCycle1BeginningDate = "";
            String hasCurrentPhaseCycle1EndingDate = "";

            Calendar jan1 = Calendar.getInstance();
            Calendar cycleStartDate = Calendar.getInstance();
            Calendar cycleEndDate = Calendar.getInstance();
            String[] cycleStartDateSplit = beginningOfCycleDate.split(",");

            //beginning date integers for comparison
            int cycleStartYear = 0;
            int cycleStartMonth = 0;
            int cycleStartDay = 0;

            if(cycleStartDateSplit.length == 2){
                //set the year to the current year
                cycleStartYear = jan1.get(Calendar.YEAR);
                cycleStartMonth = Integer.valueOf(cycleStartDateSplit[0]) - 1;
                cycleStartDay = Integer.valueOf(cycleStartDateSplit[1]);
            }else if (cycleStartDateSplit.length == 3){
                if(cycleStartDateSplit[0] == "n"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 1;
                }else if(cycleStartDateSplit[0] == "nn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 2;
                }else if(cycleStartDateSplit[0] == "nnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 3;
                }else if(cycleStartDateSplit[0] == "nnnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 4;
                }else if(cycleStartDateSplit[0] == "nnnnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 5;
                }

                cycleStartMonth = Integer.valueOf(cycleStartDateSplit[1]) - 1;
                cycleStartDay = Integer.valueOf(cycleStartDateSplit[2]);
            }

            //int cycleStartYear = Integer.valueOf(cycleStartDateSplit[0]);
            String[] cycleEndDateSplit = endOfCycleDate.split(",");

            //ending date integers for comparison
            int cycleEndYear = 0;
            int cycleEndMonth = 0;
            int cycleEndDay = 0;

            //Log.d(TAG, "END OF CYCLE DATE STRING : "+ endOfCycleDate);
            //Log.d(TAG, "cycleEndDateSplit .length" + cycleEndDateSplit.length);
            if(cycleEndDateSplit.length == 2){
                //Log.d(TAG, "CYCLE END DATE SPLIT.LENGTH = 2");
                //set the year to the current year
                cycleEndYear = jan1.get(Calendar.YEAR);
                cycleEndMonth = Integer.valueOf(cycleEndDateSplit[0]) - 1;
                cycleEndDay = Integer.valueOf(cycleEndDateSplit[1]);

                //Log.d(TAG, "cycleEndYear : " + cycleEndYear);
            }else if (cycleEndDateSplit.length == 3){
                //Log.d(TAG, "cycleEndYear : " + cycleEndDateSplit[0]); //for test on ln. 608
                if(cycleEndDateSplit[0].equals("n")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 1;
                }else if(cycleEndDateSplit[0].equals("nn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 2;
                }else if(cycleEndDateSplit[0].equals("nnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 3;
                }else if(cycleEndDateSplit[0].equals("nnnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 4;
                }else if(cycleEndDateSplit[0].equals("nnnnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 5;
                }else{
                    /**todo: logic fault here.java.lang.NumberFormatException: For input string: "nn" */
                    cycleEndYear = Integer.valueOf(cycleEndDateSplit[0]);
                }

                cycleEndMonth = Integer.valueOf(cycleEndDateSplit[1]) - 1;
                cycleEndDay = Integer.valueOf(cycleEndDateSplit[2]);
            }

            //Log.d(TAG, "cycleendyear here : " + cycleEndYear);


            /**now you can compare the two (beginning and ending) YEAR integers to determine the length of the cycle*/
            int totalCycleYearSpan = cycleEndYear - cycleStartYear;
            /**above is the total length of the cycle**/
            //Log.d(TAG, "totalCycleYearSpan : " + String.valueOf(totalCycleYearSpan));

            refChartColors.add(Color.argb(0,255,255,255));

            if(totalCycleYearSpan > 0){

                //Log.d(TAG, "cycle is > 1 year");
                /**calc the total cycle length*/
                cycleStartDate.set(cycleStartYear, cycleStartMonth, cycleStartDay);
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);
                long startInMillis = cycleStartDate.getTimeInMillis();
                long endInMillis = cycleEndDate.getTimeInMillis();
                long divisor = 86400000;
                long finalCycleLength = endInMillis - startInMillis;
                long cycleLenDays = finalCycleLength/divisor;
                int finalLengthInt = (int)cycleLenDays;

                /**calc the cycle gap and cycle delay*/
                int newStartYear = cycleStartYear + 1;
                Calendar newComparableStartDate = Calendar.getInstance();
                newComparableStartDate.set(newStartYear, cycleStartMonth, cycleStartDay);
                long comparableInMillis = newComparableStartDate.getTimeInMillis();
                long cycleGapInMillis = comparableInMillis - endInMillis;
                long cycleGapInDays = cycleGapInMillis/divisor;
                int finalCycleGap = (int)cycleGapInDays; //this value will inform a period to be added to the dataEntries array as a new entry with no color added
                //above is the total gap between the ending and beginning of the cycle
                //now all you need is to know how much to rotate the entire pie

                /**set cycle gap*/
                colors.add(Color.argb(0,0,0,0));
                String[] delayEntry = {"cycleGap", "", "", "", ""};/**updated to include extra brackets*/
                pieChartDataEntries.add(new PieEntry(finalCycleGap, "", delayEntry));

                //then inform the degree of rotation
                jan1.set(cycleStartYear, 0, 1);
                long jan1InMillis = jan1.getTimeInMillis();
                long delayMillis = startInMillis - jan1InMillis;
                long delayInDays = delayMillis/divisor;
                int delayDaysInt = (int)delayInDays;

                //then compare the gap with the delay, and if the gap is less than the delay, then the delay would only be that value minus the gap
                /**configure the pieChart rotation value*/
                /*if(finalCycleGap <= delayDaysInt){
                    int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                    double d = 365.00/360.00;
                    double rem = (double)pieRotationInDays * d;
                    pieChartRotationValue = (float)rem; //final rotation value
                }*/

                /**the length of the cycle can still be less than 1 year, but can overlap newYears.*/
                if(totalCycleYearSpan == 1){
                    if(finalLengthInt > 365){
                        //cycleSpan extends into two years, and the total length is more than a year
                        /**todo: set the refChart to 2 years*/

                        //configure the refChart (2 years)
                        for(int i = 0; i < 2; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 730.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }

                    }else{
                        //cycleSpan extends into two years, but the total length is actually less than one year
                        //where is the starting point relative to jan1? that difference will simply be dictated by the rotation
                        //what you need is the space between the end and start dates. set the start date to the following year, and then just subtract the end date from the startdate
                        //to determine the actual lengtth of that cycle gap.

                        /**calc the cycle gap and cycle delay*/
                        /*int newStartYear = cycleStartYear + 1;
                        Calendar newComparableStartDate = Calendar.getInstance();
                        newComparableStartDate.set(newStartYear, cycleStartMonth, cycleStartDay);
                        long comparableInMillis = newComparableStartDate.getTimeInMillis();
                        long cycleGapInMillis = comparableInMillis - endInMillis;
                        long cycleGapInDays = cycleGapInMillis/divisor;
                        int finalCycleGap = (int)cycleGapInDays; //this value will inform a period to be added to the dataEntries array as a new entry with no color added
                        //above is the total gap between the ending and beginning of the cycle
                        //now all you need is to know how much to rotate the entire pie
                        colors.add(Color.argb(0,0,0,0));
                        String[] delayEntry = {"cycleGap", "", "", ""};
                        pieChartDataEntries.add(new PieEntry(finalCycleGap, "", delayEntry));

                        //then inform the degree of rotation
                        jan1.set(cycleStartYear, 0, 1);
                        long jan1InMillis = jan1.getTimeInMillis();
                        long delayMillis = startInMillis - jan1InMillis;
                        long delayInDays = delayMillis/divisor;
                        int delayDaysInt = (int)delayInDays;

                        //then compare the gap with the delay, and if the gap is less than the delay, then the delay would only be that value minus the gap
                        /**configure the pieChart rotation value*/
                        /*if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 365.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }*/

                        /**configure the refChart (single year)*/
                        String[] yearEntry = {"Jan1", "","",""};
                        refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));

                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 365.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }
                }else if(totalCycleYearSpan == 2){
                    if(finalLengthInt > 730){
                        for(int i = 0; i < 3; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1095.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 2; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 730.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }

                }else if(totalCycleYearSpan ==3){
                    if(finalLengthInt > 1095){
                        for(int i = 0; i < 4; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1460.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 3; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1095.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }

                }else if(totalCycleYearSpan == 4){
                    if(finalLengthInt > 1460){
                        for(int i = 0; i < 5; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1825.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 4; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1460.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }

                }else if(totalCycleYearSpan == 5){
                    if(finalLengthInt > 1825){
                        for(int i = 0; i < 6; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 2190.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 5; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1825.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }
                }

                /***TODO: you have to configure the pie slices to each cycle length. Right???
                 * look into this. For ex: if the total cycle length is 2 years, then cut the slices into half of
                 * //what they are currently set to...? maybe?
                 * **/

                //Log.d(TAG, "this is the roTATION VALUE: " + String.valueOf(pieChartRotationValue));

            }else{
                //it's just a single year thing, you can therefore set the start and end delays relative to Jan 1st and Dec. 31st, respectively.

                Log.d(TAG, "it's just a single year thing, for the spring cycle");
                /**set the cycle start delay relative to a January 1st pivot point*/
                jan1.set(cycleStartYear, 0, 1);
                cycleStartDate.set(cycleStartYear, cycleStartMonth, cycleStartDay);

                long jan1InMillis = jan1.getTimeInMillis();
                long beginningDateInMillis = cycleStartDate.getTimeInMillis();

                long cycleStartDelay = beginningDateInMillis - jan1InMillis;
                long divisor = 86400000;
                long finalStartDelay = cycleStartDelay/divisor;

                int finalDelayInt = (int)finalStartDelay;

                //Log.d(TAG, "THIS IS THE FINAL DELAY FOR THE BEGINNING OF THE CYCLE FROM JANUARY 1 : " + finalDelayInt + " days");

                /**add the delay phase here, but it can't be clickable, and needs to be totally transparent**/
                colors.add(Color.argb(0,0,0,0));
                String[] delayEntry = {"delay", "", "", "", ""};
                pieChartDataEntries.add(new PieEntry(finalDelayInt, "", delayEntry));

                /**set the refChart to a single year*/
                refChartColors.add(Color.argb(0,255,255,255));
                String[] yearEntry = {"Jan1", "","",""};
                refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));

                //int transparent = Color.argb(0, Color.red(0), Color.green(0), Color.blue(0));
                //@ColorInt int transparent = Color.parseColor("#00d9ff04");


                /**you will also have to set the end delays, but maybe create a trigger here, and then migrate this section below to AFTER THE LOOP through allof the cycle's events*/

                /*Calendar dec31 = Calendar.getInstance();
                dec31.set(cycleStartYear, 11, 31 );
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);

                //ok, now you can compare them
                long dec31InMillis = dec31.getTimeInMillis();
                long endDateInMillis = cycleEndDate.getTimeInMillis();

                long yearRemainder = dec31InMillis - endDateInMillis;
                long finalYearRemainder = yearRemainder/divisor;

                int finalRemainderInt = (int)finalYearRemainder; //this value is to set the end remainder phase
                colors.add(Color.argb(0,0,0,0));
                String[] remainderEntry = {"remainder","","",""};
                pieChartDataEntries.add(new PieEntry(finalRemainderInt, "", remainderEntry));
*/



                /***/

            }

            Boolean entirePhaseIsInFuture = true;
            Boolean entirePhaseHasPast = true;

            /**Looop through the varietyPhase for the spring cycle*/
            for(String key: varietyPhase1.keySet()){
                //Log.d(TAG, "varietyPhase1. keyset. (key) : " + key);
                final String phaseName = key;
                final String round1PhaseDetail = varietyPhase1.get(key).get("beginning")[2];

                round1HashMap.put(key, round1PhaseDetail);

                final String beginningDate = varietyPhase1.get(key).get("beginning")[1];
                final String endingDate = varietyPhase1.get(key).get("end")[1];



                //Log.d(TAG, "\n\n\nBEGINNING DATE AT THE BEGINNING OF THE LOOP : " + beginningDate);
                //Log.d(TAG, "ENDING DATE AT THE BEGINNING OF THE LOOP: " + endingDate);

                int timeRel = 0;

                //now you can process the dates and get the number of days
                String[] beginningDateSplit = beginningDate.split(",");
                String[] endDateSplit = endingDate.split(",");

                int beginningYear;
                int beginningMonth = 0;
                int beginningDay = 0;

                int endingYear;
                int endingMonth = 0;
                int endingDay = 0;

                Calendar now = Calendar.getInstance();
                beginningYear = now.get(Calendar.YEAR);
                endingYear = now.get(Calendar.YEAR);

                if(beginningDateSplit.length == 2){
                    beginningMonth = Integer.valueOf(beginningDateSplit[0]) -1;
                    beginningDay = Integer.valueOf(beginningDateSplit[1]);
                } else if(beginningDateSplit.length > 2){

                    if(beginningDateSplit[0].equals("n")){
                        beginningYear = beginningYear + 1;
                    }else if(beginningDateSplit[0].equals("nn")){
                        beginningYear = beginningYear + 2;
                    }else if (beginningDateSplit[0].equals("nnn")){
                        beginningYear = beginningYear + 3;
                    }else if (beginningDateSplit[0].equals("nnnn")){
                        beginningYear = beginningYear + 4;
                    }else if(beginningDateSplit[0].equals("nnnnn")){
                        beginningYear = beginningYear + 5;
                    }

                    beginningMonth = Integer.valueOf(beginningDateSplit[1]) -1;
                    beginningDay = Integer.valueOf(beginningDateSplit[2]);
                }

                if(endDateSplit.length == 2){
                    endingMonth = Integer.valueOf(endDateSplit[0]) -1;
                    endingDay = Integer.valueOf(endDateSplit[1]);
                } else if(endDateSplit.length > 2){

                    if(endDateSplit[0].equals("n")){
                        endingYear = endingYear + 1;
                    }else if(endDateSplit[0].equals("nn")){
                        endingYear = endingYear + 2;
                    }else if (endDateSplit[0].equals("nnn")){
                        endingYear = endingYear + 3;
                    }else if (endDateSplit[0].equals("nnnn")){
                        endingYear = endingYear + 4;
                    }else if(endDateSplit[0].equals("nnnnn")){
                        endingYear = endingYear + 5;
                    } else {
                        endingYear = Integer.valueOf(endDateSplit[0]);
                    }
                    endingMonth = Integer.valueOf(endDateSplit[1]) -1;
                    endingDay = Integer.valueOf(endDateSplit[2]);
                }
                /**set the dates and calculate the time between them*/
                //int durationBetween;
                Calendar beginningCal = Calendar.getInstance();
                beginningCal.set(beginningYear, beginningMonth, beginningDay);
                long beginningCalInMillis = beginningCal.getTimeInMillis();
                Calendar endCal = Calendar.getInstance();
                endCal.set(endingYear, endingMonth, endingDay-1); /***added the -1 to differentiate from the beginning and ending date on the pie chart*/

                long endingCalInMillis = endCal.getTimeInMillis();

                /***convert the calendarobjects into date objects to compare*///******NEW******
                /***convert the dates into date objects to conpare them*/ //****NEW*****
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                Date beginD = new Date();
                Date endD = new Date();
                Date nowD = new Date();
                int nowMonth = now.get(Calendar.MONTH);
                int nowYear = now.get(Calendar.YEAR);
                int nowDay = now.get(Calendar.DATE);

                //Log.d(TAG, "NOW MONTH : " + nowMonth);
                String dbegin = String.valueOf(beginningYear) + "," + String.valueOf(beginningMonth+1) + "," + String.valueOf(beginningDay);
                String dend = String.valueOf(endingYear) + "," + String.valueOf(endingMonth+1) + "," + String.valueOf(endingDay);
                String dNow = String.valueOf(nowYear) + "," + String.valueOf(nowMonth+1) + "," + String.valueOf(nowDay);
                try{
                    beginD = sdf.parse(dbegin);
                    endD = sdf.parse(dend);
                    nowD = sdf.parse(dNow);
                }catch(ParseException ex){
                    ex.printStackTrace();
                }

                /**calculate number of days*/
                long remainder = endingCalInMillis - beginningCalInMillis;
                long singleDay = 86400000;
                long finalLongDuration = remainder/singleDay;
                int finalDuration = (int)finalLongDuration;

                String phaseState = "";

                /**figure the timeRel*/
                if((beginningCal.compareTo(now) <= 0 /*past*/) && (endCal.compareTo(now) < 0) /*past*/){
                    timeRel = -1;
                    //Log.d(TAG, "PHASE NAME = " + phaseName + ", timerel = " + timeRel);

                    colors.add(Color.argb(255, 153,153,153));
                    phaseState = "-1";
                    entirePhaseIsInFuture = false;
                }else if((beginningCal.compareTo(now) <= 0 /*past*/) && (endCal.compareTo(now) >= 0) /*future*/){
                    timeRel = 0;
                    //Log.d(TAG, "PHASE NAME = " + phaseName + ", timerel = " + timeRel);

                    //isInPhase1 = true;
                    currentPhaseName1 = phaseName;

                    hasCurrentPhaseCycle1BeginningDate = varietyPhase1.get(key).get("beginning")[1];
                    hasCurrentPhaseCycle1EndingDate = varietyPhase1.get(key).get("end")[1];

                    String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle1BeginningDate).getFinalDateStr();
                    String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle1EndingDate).getFinalDateStr();

                    TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                    beginDate.setText(readableBeginDateStr);
                    beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                    endDate.setText(readableEndDateStr);
                    endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                    TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                    beginDateBeginning.setText(R.string.present_began);
                    endDateBeginning.setText(R.string.present_ends);

                    beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));
                    endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                    String currentTitle = getApplicationContext().getString(R.string.current_phase, currentPhaseName1);
                    currentPhaseTitle.setText(currentTitle);
                    currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    colors.add(Color.argb(255,255,64,129));
                    phaseState = "0";

                    entirePhaseIsInFuture = false;
                    entirePhaseHasPast = false;

                }else if((beginningCal.compareTo(now) > 0 /*future*/) && (endCal.compareTo(now) >= 0)/*future*/){
                    timeRel = 1;
                    colors.add(Color.argb(255,58,174,52));
                    phaseState = "1";
                    //Log.d(TAG, "PHASE NAME = " + phaseName + ", timerel = " + timeRel);
                    entirePhaseHasPast= false;
                }

                String eventBeginningDate = varietyPhase1.get(key).get("beginning")[1];
                String eventEndingDate = varietyPhase1.get(key).get("end")[1];
                /***instead of passing the entire object, just pass the above two strings*/

                String readableBeginDateStr = new getReadableDate(eventBeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(eventEndingDate).getFinalDateStr(); //THIS CAN BE INSERTED INTO THE "DATA" object for the data entry

                /***this is ALL YOU NEED*/
                String[] entryData = {phaseState, round1PhaseDetail, readableBeginDateStr, readableEndDateStr, phaseName}; /**updated to include the phaseName*/
                pieChartDataEntries.add(new PieEntry(finalDuration, phaseName, entryData));
            } //end of the loop that iterates over each of the events in the cycle. maybe you can determine what the total length of the cycle is, then set the calendar pie to that value.


            if(!entirePhaseHasPast && entirePhaseIsInFuture){
                //entire phase is in the future
                //implement the UI to accommodate the Notice accordingly.

                hasCurrentPhaseCycle1BeginningDate = varietyPhase1.get(beginningPhaseName).get("beginning")[1];
                hasCurrentPhaseCycle1EndingDate = varietyPhase1.get(beginningPhaseName).get("end")[1];
                String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle1BeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle1EndingDate).getFinalDateStr();

                TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                beginDate.setText(readableBeginDateStr);
                beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                endDate.setText(readableEndDateStr);
                endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                beginDateBeginning.setText(R.string.future_begins);
                beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));
                endDateBeginning.setText(R.string.future_ends);
                endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                currentPhaseTitle.setText(R.string.growing_cycle_has_not_begun);
                currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));


                /***todo: there are more UI and text configurations to accommodate having the entire phase be in the future*/



            }else if(entirePhaseHasPast && !entirePhaseIsInFuture){
                //entire phase is in the past
                hasCurrentPhaseCycle1BeginningDate = varietyPhase1.get("Harvesting").get("beginning")[1];
                hasCurrentPhaseCycle1EndingDate = varietyPhase1.get("Harvesting").get("end")[1];
                String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle1BeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle1EndingDate).getFinalDateStr();

                TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                beginDate.setText(readableBeginDateStr);
                beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                endDate.setText(readableEndDateStr);
                endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                beginDateBeginning.setText(R.string.past_began);
                beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));
                endDateBeginning.setText(R.string.past_ended);
                endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                currentPhaseTitle.setText(R.string.growing_cycle_has_passed);
                currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                /***todo: there are more UI and text configurations to accommodate having the entire phase be in the past*/



            }else if(!entirePhaseHasPast && !entirePhaseIsInFuture){
                //phase must be in progress, and nothing needs to happen because the UI has already been configured above.
            }else if (entirePhaseHasPast && entirePhaseIsInFuture){
                //something is not right.
            }
            /**now implement the end remainder phase*/
            if(totalCycleYearSpan > 0){

            }else {
                Calendar dec31 = Calendar.getInstance();
                dec31.set(cycleStartYear, 11, 31 );
                //Log.d(TAG, "CYCLE END YEAR : " + cycleEndYear + " , cycleEndMonth : " + cycleEndMonth + ", cycleEnd Date : " + cycleEndDay);
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);

                //ok, now you can compare them
                long dec31InMillis = dec31.getTimeInMillis();
                long endDateInMillis = cycleEndDate.getTimeInMillis();
                long divisor = 86400000;

                long yearRemainder = dec31InMillis - endDateInMillis;
                long finalYearRemainder = yearRemainder/divisor;

                //Log.d(TAG, "final Year Remainder : " + finalYearRemainder);

                int finalRemainderInt = (int)finalYearRemainder; //this value is to set the end remainder phase
                //Log.d(TAG, "finalRemainder Int : " + finalRemainderInt);

                colors.add(Color.argb(0,0,0,0));
                String[] remainderEntry = {"remainder","","","", ""};/**UPDATED TO INCLUDE EXTRA brackets for phaseName in template*/
                pieChartDataEntries.add(new PieEntry(finalRemainderInt, "", remainderEntry));
            }

        /***the end of the spring cycle*/
        } else if (isFallCycle){
            /***the following is entirely based on whether the UI is rendering for the FALL CYCLE**/

            final LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase2 = new LinkedHashMap<>();

            /**set the toolbar subtitle to include "fall cycle"*/
            TextView toolbarSubtitle = findViewById(R.id.event_calendar_toolbar_subtitle);
            toolbarSubtitle.setText(R.string.fall_cycle);

            Boolean isInPhase2 = false;
            String currentPhaseName2 = "";
            String beginningPhaseName = "";

            int beginningOfCycleYear; //these are used later to calc the cycle length in total.
            String beginningOfCycleDate = "";
            int endingOfCycleYear; //these are used later to calc the cycle length in total.
            String endOfCycleDate = "";

            for(int i = 0; i < listOfVarietyEvents.size(); i++){
                varietyCalendarEvent varCal = listOfVarietyEvents.get(i);
                String eventTitle = varCal.getEventTitle();
                if(eventTitle.equals("startInside2")) {
                    beginningOfCycleDate = varCal.getEventDate();
                    String[] seedPhase2Beginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.put("Seedlings", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Seedlings").put("beginning", seedPhase2Beginning);
                    beginningPhaseName = "Seedlings";
                    hasRound2SeedlingPhase = true;
                }else if (eventTitle.equals("startHardenOff2")) {
                    String[] seedPhase2Ending = {varCal.getEventTitle(), varCal.getEventDate()};
                    String[] hardeningPhase2Beginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.get("Seedlings").put("end", seedPhase2Ending);
                    varietyPhase2.put("Hardening-off seedlings", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Hardening-off seedlings").put("beginning", hardeningPhase2Beginning);
                    hasRound2HardenOffPhase = true;
                }else if ((eventTitle.equals("startOutside2")) || (eventTitle.equals("startOutside2Covered"))){

                    /**since the hardening off phase may or may not precede the transplanting phase, then you have to check the boolean value to see if the
                     * hardening off phase even exists and proceed from there.
                     * */
                    hasRound2TransplantingPhase = true;

                    //figure out if this ends the hardening phase, or the startInside phase
                    if (hasRound2HardenOffPhase){
                        String[] hardening2PhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                        varietyPhase2.get("Hardening-off seedlings").put("end", hardening2PhaseEnding);
                    }else if(!hasRound2HardenOffPhase){
                        String[] seedling2PhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                        varietyPhase2.get("Seedlings").put("end", seedling2PhaseEnding);
                    }
                    String[] transplanting2PhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.put("Transplanting", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Transplanting").put("beginning", transplanting2PhaseBeginning);

                } else if ((eventTitle.equals("directOutside2"))||(eventTitle.equals("directOutside2Covered"))) {
                    beginningOfCycleDate = varCal.getEventDate();
                    hasRound2PlantingDirectPhase = true;
                    String[] directPlanting2PhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.put("Planting", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Planting").put("beginning", directPlanting2PhaseBeginning);
                    beginningPhaseName = "Planting";
                } else if (eventTitle.equals("stopOutside2")){
                    String[] planting2PhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                    if(hasRound2TransplantingPhase){
                        varietyPhase2.get("Transplanting").put("end", planting2PhaseEnding);
                    }else if(hasRound2PlantingDirectPhase){
                        varietyPhase2.get("Planting").put("end", planting2PhaseEnding);
                    }
                    String[] growing2PhaseBeginning = {varCal.getEventTitle(), varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.put("Growing", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Growing").put("beginning", growing2PhaseBeginning);
                }else if (eventTitle.equals("startHarvest2")){
                    //round2 = true;
                    String[] growing2PhaseEnding = {varCal.getEventTitle(), varCal.getEventDate()};
                    String[] harvestPhaseBeginning = {varCal.getEventTitle(),varCal.getEventDate(), varCal.getEventDetail()};
                    varietyPhase2.get("Growing").put("end", growing2PhaseEnding);

                    varietyPhase2.put("Harvesting", new LinkedHashMap<String, String[]>());
                    varietyPhase2.get("Harvesting").put("beginning", harvestPhaseBeginning);
                }else if(eventTitle.equals("stopHarvest2")){
                    hasRound2HarvestEndDate = true;
                    String[] harvestEnd = {varCal.getEventTitle(),varCal.getEventDate()};
                    varietyPhase2.get("Harvesting").put("end", harvestEnd);
                    endOfCycleDate = varCal.getEventDate();
                }
            }

            if(!hasRound2HarvestEndDate){
                //meaning that there is no end date for the harvest, implying that you need to configure it yourself, about a month should do
                String harvestBeginning = varietyPhase2.get("Harvesting").get("beginning")[1];
                Calendar now = Calendar.getInstance();
                String[] harvestBeginningSplit = harvestBeginning.split(",");

                Calendar beginningCal = Calendar.getInstance();

                int beginningYear;
                int beginningMonth = 0;
                int beginningDay = 0;

                int endingYear;
                int endingMonth = 0;
                int endingDay = 0;

                beginningYear = now.get(Calendar.YEAR);

                if(harvestBeginningSplit.length == 2){
                    beginningMonth = Integer.valueOf(harvestBeginningSplit[0]) -1;
                    beginningDay = Integer.valueOf(harvestBeginningSplit[1]);
                } else if(harvestBeginningSplit.length > 2){

                    if(harvestBeginningSplit[0].equals("n")){
                        beginningYear = beginningYear + 1;
                    }else if(harvestBeginningSplit[0].equals("nn")){
                        beginningYear = beginningYear + 2;
                    }else if (harvestBeginningSplit[0].equals("nnn")){
                        beginningYear = beginningYear + 3;
                    }else if (harvestBeginningSplit[0].equals("nnnn")){
                        beginningYear = beginningYear + 4;
                    }else if(harvestBeginningSplit[0].equals("nnnnn")){
                        beginningYear = beginningYear + 5;
                    }

                    beginningMonth = Integer.valueOf(harvestBeginningSplit[1]) -1;
                    beginningDay = Integer.valueOf(harvestBeginningSplit[2]);
                }

                //then you can set the calendar object and
                beginningCal.set(beginningYear, beginningMonth, beginningDay);
                //then adjust the date one month into the future
                beginningCal.add(Calendar.DATE, 30);

                int yearVal = beginningCal.get(Calendar.YEAR);
                int monthVal = beginningCal.get(Calendar.MONTH) + 1;
                int dayVal = beginningCal.get(Calendar.DATE);

                String endCalStr = String.valueOf(yearVal) + "," + String.valueOf(monthVal) + "," + String.valueOf(dayVal);
                String[] endSubmission = {"stopHarvest2", endCalStr};
                varietyPhase2.get("Harvesting").put("end", endSubmission);

                endOfCycleDate = endCalStr;
            }else {
                //don't do anything because the end date has already been submitted
            }

            final LinkedHashMap<String, String> round2HashMap = new LinkedHashMap<>();

            /**end*/

            String hasCurrentPhaseCycle2BeginningDate = "";
            String hasCurrentPhaseCycle2EndingDate = "";

            /***add delay module */


            Calendar jan1 = Calendar.getInstance();
            Calendar cycleStartDate = Calendar.getInstance();
            Calendar cycleEndDate = Calendar.getInstance();
            String[] cycleStartDateSplit = beginningOfCycleDate.split(",");
            Log.d(TAG, "beginning of cycle date : " + beginningOfCycleDate);

            //beginning date integers for comparison
            int cycleStartYear = 0;
            int cycleStartMonth = 0;
            int cycleStartDay = 0;
            Log.d(TAG, "cycle start date split .length() = " + cycleStartDateSplit.length);
            if(cycleStartDateSplit.length == 2){
                //set the year to the current year
                cycleStartYear = jan1.get(Calendar.YEAR);
                cycleStartMonth = Integer.valueOf(cycleStartDateSplit[0]) - 1;
                cycleStartDay = Integer.valueOf(cycleStartDateSplit[1]);
            }else if (cycleStartDateSplit.length == 3){
                if(cycleStartDateSplit[0] == "n"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 1;
                }else if(cycleStartDateSplit[0] == "nn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 2;
                }else if(cycleStartDateSplit[0] == "nnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 3;
                }else if(cycleStartDateSplit[0] == "nnnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 4;
                }else if(cycleStartDateSplit[0] == "nnnnn"){
                    cycleStartYear = jan1.get(Calendar.YEAR) + 5;
                }

                cycleStartMonth = Integer.valueOf(cycleStartDateSplit[1]) - 1;
                cycleStartDay = Integer.valueOf(cycleStartDateSplit[2]);
            }

            //int cycleStartYear = Integer.valueOf(cycleStartDateSplit[0]);
            String[] cycleEndDateSplit = endOfCycleDate.split(",");

            //ending date integers for comparison
            int cycleEndYear = 0;
            int cycleEndMonth = 0;
            int cycleEndDay = 0;

            //Log.d(TAG, "END OF CYCLE DATE STRING : "+ endOfCycleDate);
            //Log.d(TAG, "cycleEndDateSplit .length" + cycleEndDateSplit.length);
            if(cycleEndDateSplit.length == 2){
                //Log.d(TAG, "CYCLE END DATE SPLIT.LENGTH = 2");
                //set the year to the current year
                cycleEndYear = jan1.get(Calendar.YEAR);
                cycleEndMonth = Integer.valueOf(cycleEndDateSplit[0]) - 1;
                cycleEndDay = Integer.valueOf(cycleEndDateSplit[1]);

                //Log.d(TAG, "cycleEndYear : " + cycleEndYear);
            }else if (cycleEndDateSplit.length == 3){
                //Log.d(TAG, "cycleEndYear : " + cycleEndDateSplit[0]); //for test on ln. 608
                if(cycleEndDateSplit[0].equals("n")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 1;
                }else if(cycleEndDateSplit[0].equals("nn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 2;
                }else if(cycleEndDateSplit[0].equals("nnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 3;
                }else if(cycleEndDateSplit[0].equals("nnnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 4;
                }else if(cycleEndDateSplit[0].equals("nnnnn")){
                    cycleEndYear = jan1.get(Calendar.YEAR) + 5;
                }else{
                    /**todo: logic fault here.java.lang.NumberFormatException: For input string: "nn" */
                    cycleEndYear = Integer.valueOf(cycleEndDateSplit[0]);
                }

                cycleEndMonth = Integer.valueOf(cycleEndDateSplit[1]) - 1;
                cycleEndDay = Integer.valueOf(cycleEndDateSplit[2]);
            }

            //Log.d(TAG, "cycleendyear here : " + cycleEndYear);


            Log.d(TAG, "cycle End Year : " + String.valueOf(cycleEndYear) + ", and cycleStartYear : " + String.valueOf(cycleStartYear));
            /**now you can compare the two (beginning and ending) YEAR integers to determine the length of the cycle*/
            int totalCycleYearSpan = cycleEndYear - cycleStartYear;
            /**above is the total length of the cycle**/
            //Log.d(TAG, "totalCycleYearSpan : " + String.valueOf(totalCycleYearSpan));

            Log.d(TAG, "Total cycle year span for the fall cycle:" + String.valueOf(totalCycleYearSpan));
            refChartColors.add(Color.argb(0,255,255,255));

            if(totalCycleYearSpan > 0){

                /**calc the total (fall cycle length)*/
                cycleStartDate.set(cycleStartYear, cycleStartMonth, cycleStartDay);
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);

                long startInMillis = cycleStartDate.getTimeInMillis();
                long endInMillis = cycleEndDate.getTimeInMillis();
                long divisor = 86400000;
                long finalCycleLength = endInMillis - startInMillis;
                long cycleLenDays = finalCycleLength/divisor;
                int finalLengthInt = (int)cycleLenDays;

                /**calc the cycle gap and cycle delay*/
                int newStartYear = cycleStartYear + 1;
                Calendar newComparableStartDate = Calendar.getInstance();
                newComparableStartDate.set(newStartYear, cycleStartMonth, cycleStartDay);
                long comparableInMillis = newComparableStartDate.getTimeInMillis();
                long cycleGapInMillis = comparableInMillis - endInMillis;
                long cycleGapInDays = cycleGapInMillis/divisor;
                int finalCycleGap = (int)cycleGapInDays; //this value will inform a period to be added to the dataEntries array as a new entry with no color added
                //above is the total gap between the ending and beginning of the cycle
                //now all you need is to know how much to rotate the entire pie

                /**set cycle gap*/
                colors.add(Color.argb(0,0,0,0));
                String[] delayEntry = {"cycleGap", "", "", ""};
                pieChartDataEntries.add(new PieEntry(finalCycleGap, "", delayEntry));

                //then inform the degree of rotation
                jan1.set(cycleStartYear, 0, 1);
                long jan1InMillis = jan1.getTimeInMillis();
                long delayMillis = startInMillis - jan1InMillis;
                long delayInDays = delayMillis/divisor;
                int delayDaysInt = (int)delayInDays;


                /**the length of the cycle can still be less than 1 year, but can overlap newYears.*/
                if(totalCycleYearSpan == 1){ //meaning the range from start to finish spans 2 years

                    if(finalLengthInt > 365){
                        //cycleSpan extends into two years, and the total length is more than a year
                        /**todo: set the refChart to 2 years*/

                        for(int i = 0; i < 2; i++){
                            String[] yearEntry = {"Jan1", "","","", ""}; /**updated to include extra brackets for phaseName in template*/
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }


                    }else{
                        //cycleSpan extends into two years, but the total length is actually less than one year
                        //where is the starting point relative to jan1? that difference will simply be dictated by the rotation
                        //what you need is the space between the end and start dates. set the start date to the following year, and then just subtract the end date from the startdate
                        //to determine the actual lengtth of that cycle gap.

                        /*int newStartYear = cycleStartYear + 1;
                        Calendar newComparableStartDate = Calendar.getInstance();
                        newComparableStartDate.set(newStartYear, cycleStartMonth, cycleStartDay);
                        long comparableInMillis = newComparableStartDate.getTimeInMillis();
                        long cycleGapInMillis = comparableInMillis - endInMillis;
                        long cycleGapInDays = cycleGapInMillis/divisor;
                        int finalCycleGap = (int)cycleGapInDays; //this value will inform a period to be added to the dataEntries array as a new entry with no color added
                        //above is the total gap between the ending and beginning of the cycle
                        //now all you need is to know how much to rotate the entire pie
                        colors.add(Color.argb(0,0,0,0));
                        String[] delayEntry = {"cycleGap", "", "", ""};
                        pieChartDataEntries.add(new PieEntry(finalCycleGap, "", delayEntry));
                        //Log.d(TAG, "this is finalCycleGap (between end in millis, and the next cycle start) : " + String.valueOf(finalCycleGap));
                        //Log.d(TAG, "for comparison, here is the final length of the cycle, subtracted from the number of days in a year : " + String.valueOf(test) );

                        //then inform the degree of rotation
                        jan1.set(cycleStartYear, 0, 1);
                        long jan1InMillis = jan1.getTimeInMillis();
                        long delayMillis = startInMillis - jan1InMillis;
                        long delayInDays = delayMillis/divisor;
                        int delayDaysInt = (int)delayInDays;
*/
                        //Log.d(TAG, "delay in Days : " + String.valueOf(delayDaysInt));


                        //then compare the gap with the delay, and if the gap is less than the delay, then the delay would only be that value minus the gap

                        /**configure the refChart (single year)*/
                        String[] yearEntry = {"Jan1", "","","", ""}; /**updated to include extra brackets for phasename in template*/
                        refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));

                        /*configure pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            //then the final rotation is going to be the delay - final gap
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            //Log.d(TAG, "Delay IN NUMBER OF DAYS - FINAL CYCLE GAP : " + String.valueOf(pieRotationInDays));
                            //Log.d(TAG, "pieRotationInDays = " + String.valueOf(pieRotationInDays));

                            double d = 365.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            //double rotationValueAsPercentage = rem * 100;
                            //test
                            //pieChartRotationsValue = (float)rotationValueAsPercentage;
                            pieChartRotationValue = (float)rem;

                            /*****ROTATION VALUE!!!*****/
                            /**
                             pieChartRotationValue = (float)pieRotationInDays; //if we don't make this into a percentage, then what?
                             */
                            //to an integer value, without a decimal point

                        }
                        Log.d(TAG, "fall cycle rotation value : " + String.valueOf(pieChartRotationValue));

                    }
                }else if(totalCycleYearSpan == 2){
                    if(finalLengthInt > 730){
                        for(int i = 0; i < 3; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1095.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 2; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 730.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }


                }else if(totalCycleYearSpan ==3){
                    if(finalLengthInt > 1095){
                        for(int i = 0; i < 4; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1460.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 3; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1095.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }


                }else if(totalCycleYearSpan == 4){
                    if(finalLengthInt > 1460){
                        for(int i = 0; i < 5; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1825.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 4; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1460.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }

                }else if(totalCycleYearSpan == 5){
                    if(finalLengthInt > 1825){
                        for(int i = 0; i < 6; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 2190.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }else{
                        for(int i = 0; i < 5; i++){
                            String[] yearEntry = {"Jan1", "","",""};
                            refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));
                        }
                        /**configure the pieChart rotation value*/
                        if(finalCycleGap <= delayDaysInt){
                            int pieRotationInDays = delayDaysInt - finalCycleGap; //this is now in days, but since you need it as a percentage (<365) convert this integer to %
                            double d = 1825.00/360.00;
                            double rem = (double)pieRotationInDays * d;
                            pieChartRotationValue = (float)rem; //final rotation value
                        }
                    }

                }
            }else{
                //it's just a single year thing, you can therefore set the start and end delays relative to Jan 1st and Dec. 31st, respectively.

                Log.d(TAG, "it's just a single year thing, for the FALL cycle");

                /**set the cycle start delay relative to a January 1st pivot point*/
                jan1.set(cycleStartYear, 0, 1);
                cycleStartDate.set(cycleStartYear, cycleStartMonth, cycleStartDay);

                long jan1InMillis = jan1.getTimeInMillis();
                long beginningDateInMillis = cycleStartDate.getTimeInMillis();

                long cycleStartDelay = beginningDateInMillis - jan1InMillis;
                long divisor = 86400000;
                long finalStartDelay = cycleStartDelay/divisor;

                int finalDelayInt = (int)finalStartDelay;

                //Log.d(TAG, "THIS IS THE FINAL DELAY FOR THE BEGINNING OF THE CYCLE FROM JANUARY 1 : " + finalDelayInt + " days");

                Log.d(TAG, "final (fall) delay: " + String.valueOf(finalDelayInt));
                /**add the delay phase here, but it can't be clickable, and needs to be totally transparent**/
                colors.add(Color.argb(0,0,0,0));
                String[] delayEntry = {"delay", "", "", "", ""}; /**updated to include extra brackets*/
                pieChartDataEntries.add(new PieEntry(finalDelayInt, "", delayEntry));

                /**set the refChart to a single year*/
                refChartColors.add(Color.argb(0,255,255,255));
                String[] yearEntry = {"Jan1", "","","", ""};/**updated to include extra brackets*/
                refChartDataEntries.add(new PieEntry(365, "January 1", yearEntry));

                //int transparent = Color.argb(0, Color.red(0), Color.green(0), Color.blue(0));
                //@ColorInt int transparent = Color.parseColor("#00d9ff04");


                /**you will also have to set the end delays, but maybe create a trigger here, and then migrate this section below to AFTER THE LOOP through allof the cycle's events*/

                /*Calendar dec31 = Calendar.getInstance();
                dec31.set(cycleStartYear, 11, 31 );
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);

                //ok, now you can compare them
                long dec31InMillis = dec31.getTimeInMillis();
                long endDateInMillis = cycleEndDate.getTimeInMillis();

                long yearRemainder = dec31InMillis - endDateInMillis;
                long finalYearRemainder = yearRemainder/divisor;

                int finalRemainderInt = (int)finalYearRemainder; //this value is to set the end remainder phase
                colors.add(Color.argb(0,0,0,0));
                String[] remainderEntry = {"remainder","","",""};
                pieChartDataEntries.add(new PieEntry(finalRemainderInt, "", remainderEntry));
*/



                /***/

            }




            /**end of delay module*/

            Boolean entirePhaseIsInFuture = true;
            Boolean entirePhaseHasPast = true;

            for(String key: varietyPhase2.keySet()){
                //Log.d(TAG, "variety phase 2.keyset (key) : " + key);
                final String phaseName = key;
                final String round2PhaseDetail = varietyPhase2.get(key).get("beginning")[2];

                /**add the values to the linkedHashmap*/
                round2HashMap.put(key, round2PhaseDetail);

                final String beginningDate = varietyPhase2.get(key).get("beginning")[1];
                final String endingDate = varietyPhase2.get(key).get("end")[1];

                //Log.d(TAG, "HERE IS THE BEGINNING OF THE DATE PROCESSING: beginning date string : " + beginningDate);
                //Log.d(TAG, ">>>AND HERE IS THE BEGINNING OF THE END DATE PROCESSING: ending date string : " + endingDate);

                //Log.d(TAG, "BEGINNING DATE AS STRING : " + beginningDate);
                //Log.d(TAG, "END DATE AS STRING : " + endingDate);
                int timeRel = 0;

                //now you can process the dates and get the number of days
                String[] beginningDateSplit = beginningDate.split(",");
                String[] endDateSplit = endingDate.split(",");

                int beginningYear;
                int beginningMonth = 0;
                int beginningDay = 0;

                int endingYear;
                int endingMonth = 0;
                int endingDay = 0;

                Calendar now = Calendar.getInstance();
                beginningYear = now.get(Calendar.YEAR);
                endingYear = now.get(Calendar.YEAR);

                if(beginningDateSplit.length == 2){
                    beginningMonth = Integer.valueOf(beginningDateSplit[0]) -1;
                    beginningDay = Integer.valueOf(beginningDateSplit[1]);
                } else if(beginningDateSplit.length > 2){

                    if(beginningDateSplit[0].equals("n")){
                        beginningYear = beginningYear + 1;
                    }else if(beginningDateSplit[0].equals("nn")){
                        beginningYear = beginningYear + 2;
                    }else if (beginningDateSplit[0].equals("nnn")){
                        beginningYear = beginningYear + 3;
                    }else if (beginningDateSplit[0].equals("nnnn")){
                        beginningYear = beginningYear + 4;
                    }else if(beginningDateSplit[0].equals("nnnnn")){
                        beginningYear = beginningYear + 5;
                    }

                    beginningMonth = Integer.valueOf(beginningDateSplit[1]) -1;
                    beginningDay = Integer.valueOf(beginningDateSplit[2]);
                }

                if(endDateSplit.length == 2){
                    endingMonth = Integer.valueOf(endDateSplit[0]) -1;
                    endingDay = Integer.valueOf(endDateSplit[1]);
                } else if(endDateSplit.length > 2){

                    if(endDateSplit[0].equals("n")){
                        endingYear = endingYear + 1;
                    }else if(endDateSplit[0].equals("nn")){
                        endingYear = endingYear + 2;
                    }else if (endDateSplit[0].equals("nnn")){
                        endingYear = endingYear + 3;
                    }else if (endDateSplit[0].equals("nnnn")){
                        endingYear = endingYear + 4;
                    }else if(endDateSplit[0].equals("nnnnn")){
                        endingYear = endingYear + 5;
                    }

                    endingMonth = Integer.valueOf(endDateSplit[1]) -1;
                    endingDay = Integer.valueOf(endDateSplit[2]);
                }

                //then set the dates and calculate the time between them
                //int durationBetween;
                Calendar beginningCal = Calendar.getInstance();
                beginningCal.set(beginningYear, beginningMonth, beginningDay);

                Calendar endCal = Calendar.getInstance();
                endCal.set(endingYear, endingMonth, endingDay-1); /**added -1 to differentiate between start and end dates for the pie chart*/


                /***convert the dates into date objects to compare them*/ //****NEW*****
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                Date beginD = new Date();
                Date endD = new Date();
                Date nowD = new Date();
                int nowMonth = now.get(Calendar.MONTH)+1;
                int nowYear = now.get(Calendar.YEAR);
                int nowDay = now.get(Calendar.DATE);

                    /**calculate number of days*/
                long beginningCalInMillis = beginningCal.getTimeInMillis();
                long endingCalInMillis = endCal.getTimeInMillis();
                long remainder = endingCalInMillis - beginningCalInMillis;
                long singleDay = 86400000;
                long finalLongDuration = remainder/singleDay;
                int finalDuration = (int)finalLongDuration;

                String phaseState = "";

                Log.d(TAG, "final duration : " + String.valueOf(finalDuration));
/*
                Log.d(TAG, "BeginningCal.compareTo(NOW) < 0 = " + beginningCal.compareTo(now) + " + ENDCAL.compareTo(NOW) < 0 = " + endCal.compareTo(now));
                Log.d(TAG, "\n\n\n\nBEGINNING CALENDAR OBJECT : " + beginningCal.getTime().toString() + "\n\n\n\n");
                Log.d(TAG, "\n\n\n\nENDING CALENDAR OBJECT : " + endCal.getTime().toString() + "\n\n\n\n");
                Log.d(TAG, "\n\n\n\nNOW CALENDAR OBJECT: " + now.getTime().toString());*/
                /**figure the timeRel*/
                if((beginningCal.compareTo(now) <= 0 /*past*/) && (endCal.compareTo(now) < 0) /*past*/){
                    timeRel = -1;

                    //refactor
                    colors.add(Color.argb(255,153,153,153));
                    phaseState = "-1";

                    entirePhaseIsInFuture = false;
                }else if((beginningCal.compareTo(now) <= 0 /*past*/) && (endCal.compareTo(now) >= 0) /*future*/){
                    timeRel = 0;

                    isInPhase2 = true;
                    currentPhaseName2 = phaseName;

                    hasCurrentPhaseCycle2BeginningDate = varietyPhase2.get(key).get("beginning")[1];
                    hasCurrentPhaseCycle2EndingDate = varietyPhase2.get(key).get("end")[1];
                    String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle2BeginningDate).getFinalDateStr();
                    String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle2EndingDate).getFinalDateStr();

                    TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                    beginDate.setText(readableBeginDateStr);
                    beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                    endDate.setText(readableEndDateStr);
                    endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                    TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                    beginDateBeginning.setText(R.string.present_began);
                    beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));
                    endDateBeginning.setText(R.string.present_ends);
                    endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                    String currentTitle = getApplicationContext().getString(R.string.current_phase, currentPhaseName2);
                    currentPhaseTitle.setText(currentTitle);
                    currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpresent));

                    //refactor
                    colors.add(Color.argb(255,255,64,129));
                    phaseState = "0";

                    entirePhaseIsInFuture = false;
                    entirePhaseHasPast = false;
                }else if((beginningCal.compareTo(now) > 0 /*past*/) && (endCal.compareTo(now) >= 0) /*past*/){
                    timeRel = 1;
                    //Log.d(TAG, "PHASE NAME = " + phaseName + ", timerel = " + timeRel);
                    colors.add(Color.argb(255,58,174,52));
                    phaseState = "1";
                    entirePhaseHasPast = false;
                }


                String eventBeginningDate = varietyPhase2.get(key).get("beginning")[1];
                String eventEndingDate = varietyPhase2.get(key).get("end")[1];
                /***instead of passing the entire object, just pass the above two strings*/

                String readableBeginDateStr = new getReadableDate(eventBeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(eventEndingDate).getFinalDateStr(); //THIS CAN BE INSERTED INTO THE "DATA" object for the data entry

                /***this is ALL YOU NEED*/
                /*String[] entryData = {phaseState, round2PhaseDetail, readableBeginDateStr, readableEndDateStr}; this is updated to include the phaseName*/

                String[] entryData = {phaseState, round2PhaseDetail, readableBeginDateStr, readableEndDateStr, phaseName}; /**updated to include phaseName*/
                pieChartDataEntries.add(new PieEntry(finalDuration, phaseName, entryData)); //YES!!!!


                /*Log.d(TAG, "Phase state : " + phaseState + ", round2PhaseDetail : " + round2PhaseDetail
                    + ", readableBeginDateStr : " + readableBeginDateStr + ", readableEndDateStr : " + readableEndDateStr
                );*/
            } //end of the loop that iterates over each of the events in the cycle.


            if(!entirePhaseHasPast && entirePhaseIsInFuture){
                //entire phase is in the future
                //implement the UI to accommodate the Notice accordingly.

                hasCurrentPhaseCycle2BeginningDate = varietyPhase2.get(beginningPhaseName).get("beginning")[1];
                hasCurrentPhaseCycle2EndingDate = varietyPhase2.get(beginningPhaseName).get("end")[1];
                String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle2BeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle2EndingDate).getFinalDateStr();

                TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                beginDate.setText(readableBeginDateStr);
                beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                endDate.setText(readableEndDateStr);
                endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                beginDateBeginning.setText(R.string.future_begins);
                beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));
                endDateBeginning.setText(R.string.future_ends);
                endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                currentPhaseTitle.setText(R.string.growing_cycle_has_not_begun);
                currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartfuture));

                /***todo: there are more UI and text configurations to accommodate having the entire phase be in the future*/


            }else if(entirePhaseHasPast && !entirePhaseIsInFuture){
                //entire phase is in the past
                hasCurrentPhaseCycle2BeginningDate = varietyPhase2.get("Harvesting").get("beginning")[1];
                hasCurrentPhaseCycle2EndingDate = varietyPhase2.get("Harvesting").get("end")[1];
                String readableBeginDateStr = new getReadableDate(hasCurrentPhaseCycle2BeginningDate).getFinalDateStr();
                String readableEndDateStr = new getReadableDate(hasCurrentPhaseCycle2EndingDate).getFinalDateStr();

                TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                beginDate.setText(readableBeginDateStr);
                beginDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                endDate.setText(readableEndDateStr);
                endDate.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                beginDateBeginning.setText(R.string.past_began);
                beginDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));
                endDateBeginning.setText(R.string.past_ended);
                endDateBeginning.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
                currentPhaseTitle.setText(R.string.growing_cycle_has_passed);
                currentPhaseTitle.setTextColor(getApplicationContext().getResources().getColor(R.color.piechartpast));

                /***todo: there are more UI and text configurations to accommodate having the entire phase be in the past*/


            }else if(!entirePhaseHasPast && !entirePhaseIsInFuture){
                //phase must be in progress, and nothing needs to happen because the UI has already been configured above.
            }else if (entirePhaseHasPast && entirePhaseIsInFuture){
                //something is not right.
            }
            /**now implement the end remainder phase for this cycle*/
            if(totalCycleYearSpan > 0){

            }else {
                Calendar dec31 = Calendar.getInstance();
                dec31.set(cycleStartYear, 11, 31 );
                //Log.d(TAG, "CYCLE END YEAR : " + cycleEndYear + " , cycleEndMonth : " + cycleEndMonth + ", cycleEnd Date : " + cycleEndDay);
                cycleEndDate.set(cycleEndYear, cycleEndMonth, cycleEndDay);

                //ok, now you can compare them
                long dec31InMillis = dec31.getTimeInMillis();
                long endDateInMillis = cycleEndDate.getTimeInMillis();
                long divisor = 86400000;

                long yearRemainder = dec31InMillis - endDateInMillis;
                long finalYearRemainder = yearRemainder/divisor;

                //Log.d(TAG, "final Year Remainder : " + finalYearRemainder);

                int finalRemainderInt = (int)finalYearRemainder; //this value is to set the end remainder phase
                //Log.d(TAG, "finalRemainder Int : " + finalRemainderInt);

                colors.add(Color.argb(0,0,0,0));
                String[] remainderEntry = {"remainder","","","", ""};/**updated to include extra brackets*/
                pieChartDataEntries.add(new PieEntry(finalRemainderInt, "", remainderEntry));
            }
        } //END OF IF FALL CYCLE

        /***then format the UI to the specific variety*/
        //String varName = intentBaselineIDStr.replaceAll("[0-9]", "").toLowerCase();
        int varietyImage = new getImage(intentBaselineIDStr.replaceAll("[0-9]", "").toLowerCase()).getImage();
        finalVarietyImage = varietyImage;

        /**set UI background image*/
        ImageView background = findViewById(R.id.activity_events_calendar_background);
        background.setImageResource(varietyImage);


        /**set the eventdescription*/
        TextView eventDescription = findViewById(R.id.mostRecentEvent);
        eventDescription.setText(upcomingEventDetail);
        Button viewCalendarButton = findViewById(R.id.seeEntireCalendarButton);
        String btnTxt = getApplicationContext().getString(R.string.view_entire_cal_button_string, varietyName);
        viewCalendarButton.setTransformationMethod(null);

        /**check to see if the height of the event description exceeds the current view frame, and if it does, then limit the text somehow and reference an expandable fragment.*/
        //first need to retrieve the height of the frame that will dictate the limitation.
        //LinearLayout mainUIFrame = findViewById(R.id.eventCalActivity_mainUIFrame);
        //mainUIFrame.measure(0,0);
        //int mainUIFrameHeight = mainUIFrame.getMeasuredHeight();
        //int mainUIFrameHeight = mainUIFrame.getHeight(); //?

        //RelativeLayout pieUIBlock = findViewById(R.id.pieUIBlock);
        //pieUIBlock.measure(0,0);

        /**get the height of the string (upcomingEventDetail)*/
        /*eventDescription.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                final int height = bottom - top;
                Log.d(TAG, "event description height = "+ String.valueOf(height));
            }
        });*/


        /**check the height of the LinearLayout (R.id.eventActivityAccommodatingTextFrame)*/

        LinearLayout mainTextFrame = findViewById(R.id.eventActivityAccommodatingTextFrame);
        mainTextFrame.measure(0,0);
        int textFrameHeight = mainTextFrame.getMeasuredHeight();
        //Log.d(TAG, "main text Frame height = "+String.valueOf(textFrameHeight));
        //this is consistent across varieties and layouts
        //this can then be the basis of comparison for what's inside of it.

        /**then all of the children for the mainTextFrame, which will have to be
         * added up in order to compare to the parent element*/
        TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);
        currentPhaseTitle.measure(0,0);
        int currentPhaseTitleHeight = currentPhaseTitle.getMeasuredHeight();
        //Log.d(TAG, "current phase title height = "+String.valueOf(currentPhaseTitleHeight));

        LinearLayout startDateStr = findViewById(R.id.startDateStr);
        startDateStr.measure(0,0);
        int startDateStrHeight = startDateStr.getMeasuredHeight();
        //Log.d(TAG, "start date Str height =" + String.valueOf(startDateStrHeight));

        TextView mostRecentEventtxt = findViewById(R.id.mostRecentEvent);
        mostRecentEventtxt.measure(0,0);
        int mostRecentEventHeight = mostRecentEventtxt.getMeasuredHeight();
        //Log.d(TAG, "most recent event height = "+String.valueOf(mostRecentEventHeight));

        eventDescription.measure(0,0);
        int eventDescriptionHeight=eventDescription.getMeasuredHeight();
        //Log.d(TAG, "...vs. eventDescription height measurement = " + String.valueOf(eventDescriptionHeight));

        LinearLayout endDateStr = findViewById(R.id.endDateStr);
        endDateStr.measure(0,0);
        int endDateStrHeight = endDateStr.getMeasuredHeight();
        //Log.d(TAG, "end date str height = "+String.valueOf(endDateStrHeight));

        int totalChildrenHeight = currentPhaseTitleHeight + startDateStrHeight +
                mostRecentEventHeight + endDateStrHeight + 54 + 32/**this is to accommodate the additional padding for the event*/;

        //Log.d(TAG, "Total children height + margins = "+ String.valueOf(totalChildrenHeight));

        /*if(totalChildrenHeight > textFrameHeight){
            Log.d(TAG, "children exceed the text Frame height");
        }*/

        TextView mostRecentEvent = findViewById(R.id.mostRecentEvent);
        LinearLayout eventActivityAccommodatingTextFrame = findViewById(R.id.eventActivityAccommodatingTextFrame);
        ConstraintLayout eventCalActivity_mainUIFrame = findViewById(R.id.eventCalActivity_mainUIFrame);

        //int mostRecentEvent_Height = mostRecentEvent.getMeasuredHeight();
        //int eventActivityAccommodatingTextFrame_Height = eventActivityAccommodatingTextFrame.getMeasuredHeight();
        //int eventCalActivity_mainUIFrame_Height = eventCalActivity_mainUIFrame.getMeasuredHeight();

        /*Log.d(TAG, "Most recent event textView height (onCreate) : " + String.valueOf(mostRecentEvent_Height));
        Log.d(TAG, "event activity accommodating text frame height (onCreate) : " + String.valueOf(eventActivityAccommodatingTextFrame_Height));
        Log.d(TAG, "event cal Main ui frame height (on Create) : " + String.valueOf(eventCalActivity_mainUIFrame_Height));
        */

        viewCalendarButton.setText(btnTxt);

        /**customize the toolbar*/
        TextView actionBarTitle = findViewById(R.id.event_calendar_toolbar_title);
        actionBarTitle.setText(varietyName);
        android.support.v7.widget.Toolbar mTopToolbar = findViewById(R.id.events_calendar_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /**create a new pieDatasetObject, and pass your data to it.*/

        final PieDataSet dataSet = new PieDataSet(pieChartDataEntries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2f);
        dataSet.setDrawValues(false); //this eliminates the drawing of the data values on the piechart
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLinePart1OffsetPercentage(25f);
        //mChart.animateY(3000, Easing.EasingOption.EaseOutBack);
        dataSet.setSelectionShift(0);

        double deg = 360.00/365.00;

        Log.d(TAG, "piechartrotationValue = " + String.valueOf(pieChartRotationValue));
        double degValueOffset = deg * (double)pieChartRotationValue;
        float degValue = (float)degValueOffset - 90f;

        pieChart.spin(0, -90, degValue, null);
        pieChart.setPadding(40,40,40,0);
        //pieChart.animateY(3000, Easing.EasingOption.EaseInOutQuart);
        pieChart.animateXY(2750, 2750, Easing.EaseInOutQuad, Easing.EaseInOutQuad);
        refChart.animateXY(1750,1750, Easing.EaseInOutQuad, Easing.EaseInOutQuad);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            int currentSelection = 0; //this will track the previously selected segment
            boolean currentlySelected = false; //this will track whether a segment is selected

            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Log.d(TAG, "looking into the highlight object: " + h.toString());
                Log.d(TAG, "looking into the e object : " + e.toString());



                int index = dataSet.getEntryIndex(e);
                Log.d(TAG,dataSet.getValues().toString());

                /**todo: determine if the selected value is already selected
                 * if it is, then don't do anything, if it isn't then switch the color hierarchy to the order needed.
                 * */

                //determine if there is a segment currently selected?
                //yes
                if(currentlySelected){
                    //determine if the segment selected is the the one previously selected
                    //yes
                    if(currentSelection == index){
                        //do nothing, the segment is already selected
                    //no
                    }else {
                        // the selection has changed to another segment.
                        pieClick = true;

                        //determine if the segment is actually a highlightable segment
                        Color currentColor = Color.valueOf(colors.get(index));

                        if(currentColor.alpha() > 0){

                            currentlySelected = true;
                            currentSelection = index;

                            for(int g = 0; g < colors.size(); g++){
                                Color color = Color.valueOf(colors.get(g));
                                if(g == index){
                                    //meaning g references to the new segment
                                    // change this segment to alpha: 1
                                    if(color.alpha() > 0){
                                        int newColor = Color.argb(1, color.red(), color.green(), color.blue());
                                        colors.set(g, newColor);
                                    }
                                }else{

                                    if(color.alpha()>0){
                                        /***this works for now*/
                                        int newColor = Color.argb(150, color.red(), color.green(), color.blue());
                                        colors.set(g, newColor);
                                    }

                                }
                            }
                        }else{
                            //reset all pie segment colors
                            currentlySelected = false;
                            for(int g = 0; g < colors.size(); g++){
                                Color color = Color.valueOf(colors.get(g));

                                if(color.alpha() > 0){
                                    int newColor = Color.argb(1, color.red(), color.green(), color.blue());
                                    colors.set(g, newColor);
                                }

                            }

                        }
                    }
                //no
                }else{
                    // there are no segments selected, do all the things!
                    pieClick = true;
                    Color selectedColor = Color.valueOf(colors.get(index));
                    if(selectedColor.alpha()>0){
                        currentlySelected = true;
                        currentSelection = index;
                        for(int g = 0; g < colors.size(); g++){
                            Color color = Color.valueOf(colors.get(g));
                            if(g == index){
                                //do nothing
                            }else{

                                if(color.alpha()>0){
                                    //this works for now
                                    int newColor = Color.argb(150, color.red(), color.green(), color.blue());
                                    colors.set(g, newColor);
                                }

                            }
                        }
                    }else {
                        currentlySelected = false;
                        //
                    }

                }

                TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
                TextView endDate = findViewById(R.id.mostRecentEventEndDate);
                TextView beginDateBeginning = findViewById(R.id.startDateStrBeginning);
                TextView endDateBeginning = findViewById(R.id.endDateStrBeginning);
                TextView currentPhaseTitle = findViewById(R.id.currentPhaseTitle);

                String[] dataObj = (String[])e.getData();

                if(dataObj[0].equals("-1")){

                    String beginningDate = dataObj[2];
                    String endingDate = dataObj[3];
                    beginDate.setText(beginningDate);
                    endDate.setText(endingDate);

                    String detail = dataObj[1];
                    TextView eventDescription = findViewById(R.id.mostRecentEvent);
                    eventDescription.setText("");
                    eventDescription.setText(detail);

                    //should be in the past

                    beginDate.setTextColor(Color.rgb(153,153,153));
                    endDate.setTextColor(Color.rgb(153,153,153));
                    beginDateBeginning.setTextColor(Color.rgb(153,153,153));
                    endDateBeginning.setTextColor(Color.rgb(153,153,153));
                    currentPhaseTitle.setTextColor(Color.rgb(153,153,153));

                    beginDateBeginning.setText(R.string.past_began);
                    endDateBeginning.setText(R.string.past_ended);

                } else if(dataObj[0].equals("0")){

                    String beginningDate = dataObj[2];
                    String endingDate = dataObj[3];
                    beginDate.setText(beginningDate);
                    endDate.setText(endingDate);

                    String detail = dataObj[1];
                    TextView eventDescription = findViewById(R.id.mostRecentEvent);
                    eventDescription.setText("");
                    eventDescription.setText(detail);

                    beginDate.setTextColor(Color.rgb(255,64,129));
                    endDate.setTextColor(Color.rgb(255,64,129));
                    beginDateBeginning.setTextColor(Color.rgb(255,64,129));
                    endDateBeginning.setTextColor(Color.rgb(255,64,129));
                    currentPhaseTitle.setTextColor(Color.rgb(255,64,129));

                    beginDateBeginning.setText(R.string.present_began);
                    endDateBeginning.setText(R.string.present_ends);

                }else if(dataObj[0].equals("1")){

                    String beginningDate = dataObj[2];
                    String endingDate = dataObj[3];
                    beginDate.setText(beginningDate);
                    endDate.setText(endingDate);

                    String detail = dataObj[1];
                    TextView eventDescription = findViewById(R.id.mostRecentEvent);
                    eventDescription.setText("");
                    eventDescription.setText(detail);

                    beginDate.setTextColor(Color.rgb(58,174,52));
                    endDate.setTextColor(Color.rgb(58,174,52));
                    beginDateBeginning.setTextColor(Color.rgb(58,174,52));
                    endDateBeginning.setTextColor(Color.rgb(58,174,52));
                    currentPhaseTitle.setTextColor(Color.rgb(58,174,52));

                    beginDateBeginning.setText(R.string.future_begins);
                    endDateBeginning.setText(R.string.future_ends);

                }else{}

                if(!dataObj[4].equals("")){
                    currentPhaseTitle.setText(dataObj[4]);
                }

                descriptionOfInterest = dataObj[1];

                /**todo: 7/31/19 set the title to the appropriate title of the clicked phase of the cycle.
                 * */


                currentSelection = index;
                currentlySelected = true;
            }//end of onvalueSelected

            @Override
            public void onNothingSelected() {

                /**there needs to be a conditional clause here to determine if something is selected in order to un-select it.*/
                if(currentlySelected){
                    for(int g = 0; g < colors.size(); g++){
                        Color color = Color.valueOf(colors.get(g));
                        if(color.alpha() > 0){
                            int newColor = Color.argb(1, color.red(), color.green(), color.blue());
                            //int newColor = Color.RED;
                            colors.set(g, newColor);
                        }
                        //Log.d(TAG, String.valueOf(colors.get(g)));
                    }
                    pieChart.invalidate(); // refresh*/

                    currentlySelected = false;
                }else{
                    //nothing needs to happen
                }
            }
        });



        //pieChart.invalidate();

        /**..then set the refChart piechart */

        final PieDataSet refDataSet = new PieDataSet(refChartDataEntries, "");
        refDataSet.setColors(refChartColors);
        refDataSet.setSliceSpace(0f);
        refDataSet.setDrawValues(false); //this eliminates the drawing of the data values on the piechart
        refDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //refDataSet.setValueLinePart1OffsetPercentage(200.f);
        //refDataSet.setValueLinePart1Length(0.2f);
        //refDataSet.setValueLinePart2Length(0.4f);
        refDataSet.setValueLinePart1OffsetPercentage(0f);
        refDataSet.setValueLineColor(Color.argb(255,255,255,255));

        refDataSet.setValueLinePart1Length(0.44f);
        refDataSet.setValueLinePart2Length(0.15f);
        //mChart.animateY(3000, Easing.EasingOption.EaseOutBack);
        refDataSet.setSelectionShift(0);
        /*
        double deg = 360.00/365.00;
        double degValueOffset = deg * (double)pieChartRotationValue;
        float degValue = (float)degValueOffset - 90f;
        pieChart.spin(0, -90, degValue, null);
        */
        refChart.spin(0,90,90,null);
        refChart.setPadding(40,40,40,0);
        //pieChart.animateY(3000, Easing.EasingOption.EaseInOutQuart);
        //refChart.animateXY(2000, 1000, Easing.EaseInOutQuad, Easing.EaseInOutQuad);
        refChart.setExtraTopOffset(10f);
        refChart.setExtraBottomOffset(10f);
        pieChart.setExtraTopOffset(10f);
        pieChart.setExtraBottomOffset(10f);

        final PieData refData = new PieData(refDataSet);
        refChart.setData(refData);
        refChart.invalidate();

        final PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

        final Boolean finalCycle = isFallCycle;
        final String eventDetailRestore = upcomingEventDetail;

        /**need to create the list of events for the serializable arryaList*/

        /**trying...*/
        final ArrayList<learnMoreCalendarEvent> varietyCalendarEventList = new makeListOfCalendarEventsPerVariety(getApplicationContext(), varietyID).getVarietyCalendarList();
        final String baselineID = varietyID.toLowerCase().replaceAll("[0-9]","");
        final String botanicalName = new getBotanicalName(baselineID, getApplicationContext()).getFinalName();
        viewCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**needs to include a Bundle with the serializable ArrayList of*/

                Bundle bundle = new Bundle();
                bundle.putSerializable("cal",varietyCalendarEventList );
                Intent intent = new Intent(getApplicationContext(), learnMoreAboutXVariety_Calendar.class);
                intent.putExtra("calendarBundle", bundle);
                intent.putExtra("fromEventsCalendar", true);
                intent.putExtra("varietyName", varietyNameStash);
                intent.putExtra("varietyID", varietyID);
                intent.putExtra("isFallCycle", finalCycle);
                intent.putExtra("upcomingEventDetail", eventDetailRestore);
                intent.putExtra("botanicalName", botanicalName);
                intent.putExtra("baselineID", baselineID);
                intent.putExtra("eventCycle", finalCycle); //I know this is redundant

                if(activityIntent.hasExtra("previousActivity")){
                    intent.putExtra("previousActivity", previousActivity);
                }
                startActivity(intent);
            }
        });
        mostRecentEvent.getViewTreeObserver().addOnGlobalLayoutListener(new MyGlobalListenerClass());
        TextView tv = findViewById(R.id.mostRecentEvent);
        int tvHeight = tv.getHeight();
    } //end of onCreate

    /**trying to implement a fragment listener to inform the activity that the fragment has
     * been destroyed*/
    @Override
    public void onFragmentInteraction(int state) {
        if (state == 0){
            //Log.d(TAG, "fragment has closed... do what you need to do");
            /**I think this works, you can then migrate any changes to the UI here (alpha values)*/
            FrameLayout backgroundFader = findViewById(R.id.events_calendar_background_fader_1);
            int colorFrom = Color.parseColor("#99000000");
            int colorTo = Color.parseColor("#00000000");
        /*if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(5.0f);
        }*/
            ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                    .setDuration(750)
                    .start();
            FrameLayout backgroundFader2 = findViewById(R.id.events_calendar_background_fader_2);
            int colorFrom2 = Color.parseColor("#99000000");
            int colorTo2 = Color.parseColor("#00000000");
        /*if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(10.0f);
        }*/
            ObjectAnimator.ofObject(backgroundFader2, "backgroundColor", new ArgbEvaluator(), colorFrom2, colorTo2)
                    .setDuration(750)
                    .start();

        }

    }

    /*public void setDescriptionUIPhase1(LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase1, LinkedHashMap map, String key, int phaseTemp){
        //  Log.d(TAG, "PHASETEMP = " + phaseTemp);
        String beginningDate = varietyPhase1.get(key).get("beginning")[1];
        String endingDate = varietyPhase1.get(key).get("end")[1];
        //instead of passing the entire object, just pass the above two strings

        String readableBeginDateStr = new getReadableDate(beginningDate).getFinalDateStr();
        String readableEndDateStr = new getReadableDate(endingDate).getFinalDateStr();

        TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
        beginDate.setText(readableBeginDateStr);

        TextView endDate = findViewById(R.id.mostRecentEventEndDate);
        endDate.setText(readableEndDateStr);

        String detail = map.get(key).toString();
        TextView eventDescription = findViewById(R.id.mostRecentEvent);
        eventDescription.setText("");
        eventDescription.setText(detail);
    }*/

    /*
    public void setDescriptionUIPhase2(LinkedHashMap<String, LinkedHashMap<String,String[]>> varietyPhase2, LinkedHashMap map, String key, int phaseTemp){

        //Log.d(TAG, "PHASETEMP = " + phaseTemp);

        String beginningDate = varietyPhase2.get(key).get("beginning")[1];
        String endingDate = varietyPhase2.get(key).get("end")[1];

        String readableBeginDateStr = new getReadableDate(beginningDate).getFinalDateStr();
        String readableEndDateStr = new getReadableDate(endingDate).getFinalDateStr();

        TextView beginDate = findViewById(R.id.mostRecentEventStartDate);
        beginDate.setText(readableBeginDateStr);

        TextView endDate = findViewById(R.id.mostRecentEventEndDate);
        endDate.setText(readableEndDateStr);

        String detail = map.get(key).toString();
        TextView eventDescription = findViewById(R.id.mostRecentEvent);
        eventDescription.setText("");
        eventDescription.setText(detail);
    }*/

    /*public void createDescriptionFragment(){
        Fragment fragment = new Fragment();
        //fragment = findViewById(R.id.test_fragment);
        fragment.setArguments(getIntent().getExtras());

        Log.d(TAG, "create Description fragment called.");
        //fragMan.beginTransaction()
        //        .add(R.id.fragment_container, fragment).commit();
    };*/

    /*@Override
    public void onStart(){
        //see if you can readjust the layout given certain constraints
        super.onStart();
        //measure the UI salient component ** these have to be called AFTER onCreate, hence, they are called here
        mostRecentEvent = findViewById(R.id.mostRecentEvent);
        eventActivityAccommodatingTextFrame = findViewById(R.id.eventActivityAccommodatingTextFrame);
        eventCalActivity_mainUIFrame = findViewById(R.id.eventCalActivity_mainUIFrame);

        //measure
        mostRecentEvent.measure(0,0);
        eventActivityAccommodatingTextFrame.measure(0,0);
        eventCalActivity_mainUIFrame.measure(0,0);

        int mostRecentEventHeight = mostRecentEvent.getMeasuredHeight();
        int eventActivityAccommodatingTextFrameHeight = eventActivityAccommodatingTextFrame.getMeasuredHeight();
        int eventCalActivity_mainUIFrameHeight = eventCalActivity_mainUIFrame.getMeasuredHeight();

        Log.d(TAG, "Most recent event textView height (onStart) : " + String.valueOf(mostRecentEventHeight));
        Log.d(TAG, "event activity accommodating text frame height (onStart) : " + String.valueOf(eventActivityAccommodatingTextFrameHeight));
        Log.d(TAG, "event cal Main ui frame height (on start) : " + String.valueOf(eventCalActivity_mainUIFrameHeight));

        Log.d(TAG, "... and just to see...Display Height = " + String.valueOf(height));


        mostRecentEvent.setTextColor(Color.RED); //just to see how that re-rending looks...


    }*/
    //Declare the layout listener
    class MyGlobalListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {

            /**so, the trick of having this kind of listener is that it runs EVERY time the UI changes.*/

            //Log.d(TAG, "ON GLOBAL LAYOUT called...");
            //Log.d(TAG, "state of UIState: " + String.valueOf(UIStateTracker));

            //get the initial height of the target textView
            TextView target = findViewById(R.id.mostRecentEvent);
            int tvHeight = target.getHeight();

            //Log.d(TAG, "EVENT TEXT (TARGET TEXTVIEW) at the beginning of the onGlobalLayout : " + String.valueOf(tvHeight));
            //Log.d(TAG, "state of UI : " + String.valueOf(UIStateTracker));
            //Log.d(TAG, "PHASE OF USER ACTION (PIE CLICK) : " + String.valueOf(pieClick));

            LinearLayout perimeter = findViewById(R.id.eventActivityAccommodatingTextFrame);
            int perimeterHeight = perimeter.getHeight();

            //Log.d(TAG, "child2Height (LinearLayout)/text block : " + String.valueOf(perimeterHeight));

            /**each of the three measurements above are unchanging, meaning, that perhaps the "child2Height can actually be used as a base-line to compare all of it's children.
             * which also means that the child2height value can be used to set exactly the height of the textView that needs to change. ...and yes, you can effect the height of the
             * textView from here in the initial (or close to it) rendering!!!
             * */

            /**retrieve each of the heights of all of the children of child2*/
            TextView title = findViewById(R.id.currentPhaseTitle);
            LinearLayout ll1 = findViewById(R.id.startDateStr);
            TextView expandPrompt = findViewById(R.id.expandPrompt);
            //LinearLayout ll2 = findViewById(R.id.eventTextFrame); //this is the one with the actual textview that needs to be changed
            //the above linearlayout was changed to a constraintlayout to accommodate the "..more" textview
            ConstraintLayout ll2 = findViewById(R.id.eventTextFrame);
            LinearLayout ll3 = findViewById(R.id.endDateStr);

            /**you need to account for the margins of each of these elements*/
            int titleHeight = title.getHeight() + 16;
            int expandPromptHeight = expandPrompt.getHeight();
            int ll1Height = ll1.getHeight() + 6;
            int ll2Height = ll2.getHeight() + 16 + 24 + 32;
            int ll3Height = ll3.getHeight() + 16;
            /*Log.d(TAG, "titleHeight = " + String.valueOf(titleHeight));
            Log.d(TAG, "ll1 Height = " + String.valueOf(ll1Height));
            Log.d(TAG, "expandPrompt height = " + String.valueOf(expandPromptHeight));
            Log.d(TAG, "ll2 heigth = " + String.valueOf(ll2Height));
            Log.d(TAG, "ll3 height = " + String.valueOf(ll3Height));
            Log.d(TAG, "perimeter height = " + String.valueOf(perimeterHeight));
*/


            int cumulativeHeight = titleHeight + ll2Height + ll1Height + ll1Height;
            //Log.d(TAG, "ll3 Height: " + String.valueOf(ll3Height));


            /***all of the logic of this method, which enables anything to change should only be done
             * either when the activity first loads, or when the user clicks on a pie slice, or when a
             * screen configuration happens.
             * */

            if(pieClick || (UIStateTracker == 0)){

                //Log.d(TAG, "PIE CLICK OR UI STATE TRACKER EQUALS 0 SEQUENCE BEGUN");
                //if the user has either clicked on the piechart, initializing a change in the UI, or if the
                //UI is loading for the first Time.

                //Log.d(TAG, "ll3Height (end) : " + String.valueOf(ll3Height) +  ", ll1Height (beginning) e: " + String.valueOf(ll1Height));
                //if(ll3Height < ll1Height){ //if the height of the start date is larger than the height of the
                /**trying with different trigger*/
                if(cumulativeHeight > perimeterHeight){


                    //end date, implying that the end date is pushed off.

                    //Log.d(TAG, "OVERSIZED LAYOUT REQUIRED");
                    int remainder = perimeterHeight - titleHeight - expandPromptHeight - ll1Height - ll1Height; //(subtract the
                    // ll1Height twice to account for the height of ll3, which is still missing in full)

                    //then, you need to measure each of the components of ll2, so you can subtract all of
                    //the cumulative heights, except the target height, from the initial remainder value,
                    //then that secondary remainder will be the value that you will set the target height to.

                    //Log.d(TAG, "Initial remainder : "+ String.valueOf(remainder));
                    //Log.d(TAG, "eventTextFrame HEight : " + String.valueOf(ll2Height));
                    //Log.d(TAG, "target change height :" + String.valueOf(tvHeight));

                    int sub = ll2Height - tvHeight;
                    int finalTargetHeight = remainder - sub;

                    target.setHeight(finalTargetHeight-24);

                    //set expand text/
                    TextView expand = findViewById(R.id.expandPrompt);
                    expand.setText(R.string.expandString);
                    expand.setVisibility(View.VISIBLE);

                    //you can also set the "maxheight and the ellipsizew property of the textview
                    //tv.setMaxLines(8); //this should be done mathematically
                    float lineHeight = target.getPaint().getFontMetrics().bottom - target.getPaint().getFontMetrics().top;
                    int targetHeight = finalTargetHeight-24;
                    int lines = targetHeight / (int)lineHeight;
                    target.setMaxLines(lines);
                    target.setEllipsize(TextUtils.TruncateAt.END);

                    /**added */
                    target.setPadding(target.getPaddingLeft(),target.getPaddingTop(),target.getPaddingRight(),0);


                    //set textView as clickable
                    ll2.setBackgroundResource(R.drawable.rounded_corner_notification_clickable);
                    ll2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //mute the brightness of the current view
                            FrameLayout backgroundFader = findViewById(R.id.events_calendar_background_fader_1);
                            int colorFrom = Color.parseColor("#00000000");
                            int colorTo = Color.parseColor("#99000000");
                            if(Build.VERSION.SDK_INT > 20){
                                backgroundFader.setElevation(5.0f);
                            }
                            ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                                    .setDuration(750)
                                    .start();
                            FrameLayout backgroundFader2 = findViewById(R.id.events_calendar_background_fader_2);
                            int colorFrom2 = Color.parseColor("#00000000");
                            int colorTo2 = Color.parseColor("#99000000");
                            if(Build.VERSION.SDK_INT > 20){
                                backgroundFader.setElevation(10.0f);
                            }
                            ObjectAnimator.ofObject(backgroundFader2, "backgroundColor", new ArgbEvaluator(), colorFrom2, colorTo2)
                                    .setDuration(750)
                                    .start();

                            //do the same for the toolbar
                        //FrameLayout toolbarBackgroundFader = findViewById(R.id.eventsCalendarToolbarBackgroundFader);
                        int toolbarBackgroundFrom = Color.parseColor("#00000000");
                        int toolbarBackgroundTo = Color.parseColor("#99000000");
                        if(Build.VERSION.SDK_INT > 20){
                            backgroundFader.setElevation(5.0f);
                        }
                        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), toolbarBackgroundFrom, toolbarBackgroundTo)
                                .setDuration(750)
                                .start();
                        //
                            //create new fragment and pass it the text needed
                            descriptionExpansionFragment fragment = new descriptionExpansionFragment();
                            Bundle fragmentBundle = new Bundle();

                            fragmentBundle.putString("text", descriptionText);
                            fragmentBundle.putInt("background_image", finalVarietyImage);
                            fragment.setArguments(fragmentBundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.your_placeholder, fragment);
                            ft.commit();
                        }
                    });
                    UIStateTracker = 1;
                } else {

                    UIStateTracker = -1;

                    //reset the UI rules for the normal layout, not the oversized rules.
                    //tv.setHeight();

                    ll2.setBackgroundResource(R.drawable.rounded_corner_notification_detail);
                    ll2.setOnClickListener(null);
                    //Log.d(TAG, "NORMAL LAYOUT");
                    //Log.d(TAG, "ll3Height and ll1Height are the same");
                    //tv.setHeight(wrap content);
                    TextView expand = findViewById(R.id.expandPrompt);
                    expand.setVisibility(View.GONE);
                    //int tvPaddingB = 32;
                    int tvPaddingL = target.getPaddingLeft();
                    int tvPaddingR = target.getPaddingRight();
                    int tvPaddingT = target.getPaddingTop(); //use this for top and bottom so it matches
                    //int tvPaddingBottom = target.getPaddingBottom();


                    //Log.d(TAG, " target textview padding top = " +String.valueOf(tvPaddingT));
                    //Log.d(TAG, "target textView padding bottom = " + String.valueOf(tvPaddingBottom));

                    //TODO: test this padding reassignment /
                    //this morning, you were testing this and didn't finish. something about
                    //something...


                    target.setPadding(tvPaddingL,tvPaddingT,tvPaddingR, tvPaddingT);
                    target.setHeight(0); //I don't know why...but this works for some reason

                    //ViewGroup.LayoutParams targetHeightReassign = (ViewGroup.LayoutParams) target.getLayoutParams();
                    //targetHeightReassign.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    //target.setLayoutParams(targetHeightReassign);


                    target.setEllipsize(null);
                    target.setMaxLines(Integer.MAX_VALUE);

                }

                //reset the click tracker
                if(pieClick){
                    pieClick = false; //reset the click tracker to false to be able to validate a user's click again.
                }
            } else {
                //Log.d(TAG, "nothing happened this time. UI has not changed");
            }

            //Log.d(TAG, "THIS IS THE HEIGHT OF THE ll3 : " + String.valueOf(ll3Height));
            //Log.d(TAG, "THIS IS THE HEIGHT OF THE TARGET TEXTVIEW : " + String.valueOf(target.getHeight()));



            /***note that this works to reset the height, but then you will also need to set a conditional in the onClick listener for the pie
             * slices that will adjust the height again, otherwise, the height of the textView will remain fixed in that state.
             *
             * */

            //question: do the heights include padding and margins?
            //they do. The overall height of a view element considers the margins into
            //the measurement of the dims, which means that you don't have to worry about
            //considering the margins yourself.

            //ie: height = 45dp = 45dp, not 45dp + marginX.

            //note that this would probably be different if you were considering 'padding'.




            //THIS IS THE ORIGINAL VERSION
            /*if(ll3Height < ll1Height){ //if the height of the start date is larger than the height of the
                //end date, implying that the end date is pushed off.
                Log.d(TAG, "OVERSIZED LOAD");

                //Log.d(TAG, "ll3Height is less than ll1Height");
                int remainder = perimeterHeight - tv1Height - ll1Height - ll1Height; //(subtract the
                // ll1Height twice to account for the height of ll3, which is still missing in full)

                //then, you need to measure each of the components of ll2, so you can subtract all of
                //the cumulative heights, except the target height, from the initial remainder value,
                //then that secondary remainder will be the value that you will set the target height to.

                //Log.d(TAG, "Initial remainder : "+ String.valueOf(remainder));
                //Log.d(TAG, "eventTextFrame HEight : " + String.valueOf(ll2Height));
                //Log.d(TAG, "target change height :" + String.valueOf(tvHeight));

                int sub = ll2Height - tvHeight;
                int finalTargetHeight = remainder - sub;

                tv.setHeight(finalTargetHeight-24);

                //set expand text
                TextView expand = findViewById(R.id.expandPrompt);
                expand.setText(R.string.expandString);
                //expand.setVisibility(View.VISIBLE);

                //you can also set the "maxheight and the ellipsize property of the textview
                //tv.setMaxLines(8); //this should be done mathematically
                float lineHeight = tv.getPaint().getFontMetrics().bottom - tv.getPaint().getFontMetrics().top;
                int targetHeight = finalTargetHeight-24;
                int lines = targetHeight / (int)lineHeight;
                tv.setMaxLines(lines);
                tv.setEllipsize(TextUtils.TruncateAt.END);

                //set textView as clickable
                ll2.setBackgroundResource(R.drawable.rounded_corner_button);
                ll2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mute the brightness of the current view
                        FrameLayout backgroundFader = findViewById(R.id.events_calendar_background_fader_1);
                        int colorFrom = Color.parseColor("#00000000");
                        int colorTo = Color.parseColor("#99000000");
                        if(Build.VERSION.SDK_INT > 20){
                            backgroundFader.setElevation(5.0f);
                        }
                        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                                .setDuration(750)
                                .start();
                        FrameLayout backgroundFader2 = findViewById(R.id.events_calendar_background_fader_2);
                        int colorFrom2 = Color.parseColor("#00000000");
                        int colorTo2 = Color.parseColor("#99000000");
                        if(Build.VERSION.SDK_INT > 20){
                            backgroundFader.setElevation(10.0f);
                        }
                        ObjectAnimator.ofObject(backgroundFader2, "backgroundColor", new ArgbEvaluator(), colorFrom2, colorTo2)
                                .setDuration(750)
                                .start();
                        //create new fragment and pass it the text needed
                        descriptionExpansionFragment fragment = new descriptionExpansionFragment();
                        Bundle fragmentBundle = new Bundle();

                        //THIS FRAGMENT NEEDS THE IMMEDIATELY ACCESSED EVENT DESCRIPTION, NOT THE DESCRIPTION TEXT PASSED FROM THE
                        //PREVIOUS ACTIVITY!!!
                        //TODO: GET THE RIGHT TEXT TO PASS TO THE FRAGMENT
                        //fragmentBundle.putString("text", descriptionText);
                        fragmentBundle.putString("text", descriptionOfInterest);
                        fragmentBundle.putInt("background_image", finalVarietyImage);
                        fragment.setArguments(fragmentBundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.your_placeholder, fragment);
                        ft.commit();
                    }
                });
            } else  {

                Log.d(TAG, "NORMAL LAYOUT");
                //Log.d(TAG, "ll3Height and ll1Height are the same");
                //tv.setHeight(wrap content);
                TextView expand = findViewById(R.id.expandPrompt);
                expand.setVisibility(View.GONE);
                //int tvPaddingB = 32;
                int tvPaddingL = tv.getPaddingLeft();
                int tvPaddingR = tv.getPaddingRight();
                int tvPaddingT = tv.getPaddingTop(); //use this for top and bottom so it matches

                tv.setPadding(tvPaddingL,tvPaddingT,tvPaddingR, tvPaddingT);
                //tv.setPadding(32,16, 32,16);
                //ll2.setBackgroundResource(R.drawable.rounded_corner_notification_detail);
                //ll2.setOnClickListener(null);
            }*/

            //TODO: then you need to unregister the onGlobal layout so it doesn't go infinite.
        }//end of ongloballayout
    }

    public void closedFragment(){


        FrameLayout backgroundFader = findViewById(R.id.events_calendar_background_fader_1);
        int colorFrom = Color.parseColor("#99000000");
        int colorTo = Color.parseColor("#00000000");
        /*if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(5.0f);
        }*/
        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(750)
                .start();
        FrameLayout backgroundFader2 = findViewById(R.id.events_calendar_background_fader_2);
        int colorFrom2 = Color.parseColor("#99000000");
        int colorTo2 = Color.parseColor("#00000000");
        /*if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(10.0f);
        }*/
        ObjectAnimator.ofObject(backgroundFader2, "backgroundColor", new ArgbEvaluator(), colorFrom2, colorTo2)
                .setDuration(750)
                .start();
    }
}
