package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
//import android.widget.Toolbar;
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tylerjette on 3/24/18.
 */

public class learnMoreAboutXVariety extends AppCompatActivity implements Serializable{

    //private static final String TAG = learnMoreAboutXVariety.class.getSimpleName();
    //private RecyclerView recyclerView;
    //private learnMoreActivityCalendarAdapter calendarAdapter;
    private JSONObject fileByZone;
    private JSONArray zoneFileArray;
    private String previousActivity = "";
    ArrayList<String> transferChecklist = new ArrayList<>();

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_more_activity);

        android.support.v7.widget.Toolbar activityToolbar = findViewById(R.id.learnMoreActivityToolbar);
        setSupportActionBar(activityToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            //landscape...

            //LinearLayout mainUIBlock = findViewById(R.id.learnMoreDescriptionAndDetailsBlock);
            //mainUIBlock.setOrientation(LinearLayout.HORIZONTAL);
            //ConstraintLayout.LayoutParams mainUILP = new ConstraintLayout.LayoutParams(mainUIBlock.getLayoutParams());


            //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.learnMoreActivityToolbar);
            //mainUILP.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            //mainUIBlock.setLayoutParams(mainUILP);
            //mainUIBlock.setWeightSum(2.0f);

            //ConstraintLayout cl = findViewById(R.id.learnMoreActivityCL);
            //ConstraintSet mainConstraints = new ConstraintSet();
            //mainConstraints.connect(mainUIBlock.getId(),ConstraintSet.TOP, toolbar.getId(), ConstraintSet.TOP, 0 );

            //mainConstraints.applyTo(cl);


            //change width
            /*TextView tv = findViewById(R.id.learnMoreVarietyDescription);
            LinearLayout.LayoutParams vlp= new LinearLayout.LayoutParams(tv.getLayoutParams());
            vlp.width = 0;
            tv.setLayoutParams(vlp);

            RecyclerView rv = findViewById(R.id.learnMoreVarietyCalendarOfEventsRecyclerView);
            LinearLayout.LayoutParams rvlp = new LinearLayout.LayoutParams(rv.getLayoutParams());
            rvlp.width = 0;
            rv.setLayoutParams(rvlp);*/

            //nope

            /*LinearLayout descLayout = findViewById(R.id.learnMoreDescriptionFrame);
            descLayout.setWeightSum(1.0f);
            ViewGroup.LayoutParams descLayoutParams = descLayout.getLayoutParams();
            descLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            descLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            descLayout.setLayoutParams(descLayoutParams);*/

            //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.learnMoreActivityToolbar);



            //int toolbarHeight = toolbar.getHeight();
            //int toolbarHeight = getToolbarHeight();

            //int toolbarHeight = Integer.valueOf(R.attr.actionBarSize);

            //int toolbarHeight = getActionBar().getHeight();
            //int toolbarHeight = getToolbarHeight();

            //Log.d(TAG, "*******************this is the toolbar height: " + toolbarHeight);

            //TextView tv = findViewById(R.id.learnMoreVarietyDescription);
            //LinearLayout lin =findViewById(R.id.learnMoreDescriptionAndDetailsBlock);
            //ConstraintLayout.LayoutParams tvlp = new ConstraintLayout.LayoutParams(lin.getLayoutParams());
            //tvlp.weight = 2.0f;
            //tvlp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            //tvlp.setMargins(0,toolbarHeight,0,0);
            //tvlp.height= ConstraintLayout.LayoutParams.MATCH_PARENT;
            //lin.setLayoutParams(tvlp);

            //LinearLayout.MarginLayoutParams tvMargins = new LinearLayout.MarginLayoutParams(tv.getLayoutParams());
            //tvMargins.setMargins(0,100,0,0);
            //tv.setLayoutParams(tvMargins);

            //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.learnMoreActivityToolbar);
            //int toolbarHeight = toolbar.getHeight();

            /***since this recyclerView is being moved to the extension activity, this is obsolete
             *
             *
            RecyclerView rv = findViewById(R.id.learnMoreVarietyCalendarOfEventsRecyclerView);
            LinearLayout.LayoutParams rvlp = new LinearLayout.LayoutParams(rv.getLayoutParams());
            rvlp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            rvlp.weight = 1.0f;
            //rvlp.topMargin  = toolbarHeight;//??
            rvlp.setMargins(0,toolbarHeight,0,0);
            rv.setLayoutParams(rvlp);

             */

            //LinearLayout.MarginLayoutParams recyclerViewMarginParams = new LinearLayout.LayoutParams(rv.getLayoutParams());
            //recyclerViewMarginParams.topMargin = toolbarHeight;
            //rv.setLayoutParams(recyclerViewMarginParams);

            /*LinearLayout.MarginLayoutParams rvmlp = new LinearLayout.LayoutParams(rv.getLayoutParams());
            rvmlp.setMargins(0,0,0,0);
            rv.setLayoutParams(rvmlp);*/

        }

        final Intent activityIntent = getIntent();
        final String varietyName = activityIntent.getStringExtra("varietyName");
        final String varietyID = activityIntent.getStringExtra("varietyID").toLowerCase();

        if(activityIntent.hasExtra("previousActivity")){
            previousActivity = activityIntent.getStringExtra("previousActivity");
        }
        /**use the varietyID here to access the json file with the correct variety id, and then create a list obj to hold the information*/
        String userZone = varietyID.replaceAll("[a-z]", "");

        /**use the baselineID to access the load*/
        final String baselineID = varietyID.replaceAll("[0-9]", "").toLowerCase();
        final String varietyBotanicalName = loadBotanicalName(baselineID);

        final ArrayList<learnMoreCalendarEvent> learnMoreCalendarEventList = new ArrayList<>();

        String finalVarietyDescription = "";
        try {
            fileByZone = new JSONObject(loadbyZone(userZone).trim());
            zoneFileArray = fileByZone.getJSONArray("zone" + userZone);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        String sun_desc_str = "";
        String water_desc_str = "";
        String soil_desc_str = "";
        String spacing_desc_str = "";
        String mulch_desc_str = "";
        String fertilizer_desc_str = "";

        try{
            for(int i = 0; i < zoneFileArray.length(); i++){
                JSONObject var = (JSONObject)zoneFileArray.get(i);
                String varID = var.getString("id").toLowerCase();
                if(varID.equals(varietyID)){

                    /**TODO: read the events within that particular object.*/

                    String baselineVarID = varietyID.replaceAll("[0-9]", "");
                    JSONObject toDosFile = new JSONObject(loadToDos().trim());

                    JSONObject varietyTodos = (JSONObject)toDosFile.get(baselineVarID);
                    sun_desc_str = ((String)varietyTodos.get("sun"));
                    water_desc_str = ((String)varietyTodos.get("water"));
                    soil_desc_str = ((String)varietyTodos.get("soil"));
                    spacing_desc_str = ((String)varietyTodos.get("spacing"));
                    mulch_desc_str = ((String)varietyTodos.get("mulch"));
                    fertilizer_desc_str = ((String)varietyTodos.get("fertilize"));

                    if(var.has("round2")){
                        JSONObject round1Obj = var.getJSONObject("round1");
                        JSONObject round2Obj = var.getJSONObject("round2");

                        //build description
                        finalVarietyDescription = buildDescription(varietyName, var, round1Obj, round2Obj, varietyTodos);

                        Iterator<?> round1key = round1Obj.keys();
                        while(round1key.hasNext()){
                            /**retrieve the corresponding to-do event*/
                            String eventTitle = round1key.toString();
                            String key = (String)round1key.next(); // ie: 'startInside'
                            String dateObj = round1Obj.getString(key); // ie: '3/5'
                            String eventDetail = "";
                            if((key.equals("startOutside")) || (key.equals("directOutside")) || (key.equals("startOutsideCovered")) || (key.equals("directOutsideCovered"))) {

                                if (key.equals("startOutside")) {
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                } else if(key.equals("startOutsideCovered")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                }else if(key.equals("directOutside")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                } else if(key.equals("directOutsideCovered")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                }
                            } else {
                                eventDetail = (String)varietyTodos.getString(key);
                            }

                            getReadableEventTitle readableKey = new getReadableEventTitle(key);
                            String readableKeyStr = readableKey.getFinalTitleStr();
                            learnMoreCalendarEvent event = new learnMoreCalendarEvent(dateObj, readableKeyStr, eventDetail);
                            learnMoreCalendarEventList.add(event);
                        }
                        Iterator<?> round2key = round2Obj.keys();
                        while(round2key.hasNext()){
                            String eventTitle = round2key.toString();
                            String key = (String)round2key.next();
                            String dateObj = round2Obj.getString(key);
                            String eventDetail = "";
                            if((key.equals("startOutside2")) || (key.equals("directOutside2")) || (key.equals("startOutside2Covered")) || (key.equals("directOutside2Covered"))) {

                                if(key.equals("startOutside2")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside2");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                } else if(key.equals("startOutside2Covered")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside2");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                } else if(key.equals("directOutside2")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside2");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                }  else if(key.equals("directOutside2Covered")){
                                    JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside2");
                                    String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                    String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                    String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                    eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                }
                            } else {
                                eventDetail = (String)varietyTodos.getString(key);
                            }
                            //Log.d(TAG, "dateObj : " + dateObj);
                            //Log.d(TAG, "key : " + key);
                            getReadableEventTitle readableKey = new getReadableEventTitle(key);
                            String readableKeyStr = readableKey.getFinalTitleStr();
                            learnMoreCalendarEvent event = new learnMoreCalendarEvent(dateObj, readableKeyStr, eventDetail);
                            learnMoreCalendarEventList.add(event);
                        }
                    }else if (!var.has("round2")){
                        finalVarietyDescription = buildDescription(varietyName, var, null, null, varietyTodos);

                        Iterator<?> keys = var.keys();
                        while(keys.hasNext()){

                            String key = (String)keys.next();
                            //Log.d(TAG, "from single round variety: " + keys);
                            if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season"))){
                                String eventTitle = keys.toString();
                                String dateObj = var.getString(key);
                                String eventDetail = "";

                                //Log.d(TAG, "KEY : " + key);
                                if((key.equals("startOutside")) || (key.equals("directOutside")) || (key.equals("startOutsideCovered")) || (key.equals("directOutsideCovered"))) {

                                    if (key.equals("startOutside")) {
                                        JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside");
                                        String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                        String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                        String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                        eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                    } else if(key.equals("startOutsideCovered")){
                                        JSONObject startOutsideObj = varietyTodos.getJSONObject("startOutside");
                                        String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                        String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                        String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                        eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                    }else if(key.equals("directOutside")){
                                        JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside");
                                        String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                        String startOutsideUncovered = (String) startOutsideObj.get("uncovered");
                                        String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                        eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                    } else if(key.equals("directOutsideCovered")){
                                        JSONObject startOutsideObj = varietyTodos.getJSONObject("directOutside");
                                        String startOutsideBeginning = (String) startOutsideObj.get("begin");
                                        String startOutsideUncovered = (String) startOutsideObj.get("covered");
                                        String startOutsideEnd = (String) startOutsideObj.get("remainingBoth");
                                        eventDetail = startOutsideBeginning + startOutsideUncovered + startOutsideEnd;
                                    }
                                } else {
                                    eventDetail = (String)varietyTodos.getString(key);
                                }
                                //Log.d(TAG, "event title : " + key);
                                //Log.d(TAG, "event key (date) : " + dateObj);
                                //Log.d(TAG, "eventDetail : " + eventDetail);
                                getReadableEventTitle readableKey = new getReadableEventTitle(key);
                                String readableKeyStr = readableKey.getFinalTitleStr();
                                learnMoreCalendarEvent event = new learnMoreCalendarEvent(dateObj, readableKeyStr, eventDetail);
                                learnMoreCalendarEventList.add(event);
                            }
                        }
                    }
                }// end of if var has varID
                else{
                    //do nothing, the var doesn't match the varID
                }
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        TextView sun_desc = findViewById(R.id.sunDetail_desc);
        TextView water_desc = findViewById(R.id.waterDetail_desc);
        TextView soil_desc = findViewById(R.id.soilDetail_desc);
        TextView spacing_desc = findViewById(R.id.spacingDetail_desc);
        TextView mulch_desc = findViewById(R.id.mulchDetail_desc);
        TextView fertilizer_desc = findViewById(R.id.fertilizerDetail_desc);

        if(!sun_desc_str.equals("")){

            String cleanedSunStr = new cleanedDescription(sun_desc_str).getCleanedStr();
            sun_desc.setText(cleanedSunStr);
            ImageView sunIcon = findViewById(R.id.sunIcon);
            sunIcon.setImageResource(R.mipmap.sun_icon);
        }else{
            LinearLayout sunFrame = findViewById(R.id.sunDetailFrame);
            sunFrame.setVisibility(View.GONE);
        }

        if(!water_desc_str.equals("")){
            String cleanedWaterStr = new cleanedDescription(water_desc_str).getCleanedStr();
            water_desc.setText(cleanedWaterStr);
            ImageView waterIcon = findViewById(R.id.waterIcon);
            waterIcon.setImageResource(R.mipmap.water_icon);
        }else{
            LinearLayout waterFrame = findViewById(R.id.waterDetailFrame);
            waterFrame.setVisibility(View.GONE);
        }

        if(!soil_desc_str.equals("")){
            String cleanedSoilStr = new cleanedDescription(soil_desc_str).getCleanedStr();
            soil_desc.setText(cleanedSoilStr);
            ImageView soilIcon = findViewById(R.id.soilIcon);
            soilIcon.setImageResource(R.mipmap.soil_icon);
        }else{
            LinearLayout soilFrame = findViewById(R.id.soilDetailFrame);
            soilFrame.setVisibility(View.GONE);
        }

        if(!spacing_desc_str.equals("")){
            String cleanedSpacingStr = new cleanedDescription(spacing_desc_str).getCleanedStr();
            spacing_desc.setText(cleanedSpacingStr);
            ImageView spacingIcon = findViewById(R.id.spacingIcon);
            spacingIcon.setImageResource(R.mipmap.spacing1_icon);
        }else{
            LinearLayout spacingFrame = findViewById(R.id.spacingDetailFrame);
            spacingFrame.setVisibility(View.GONE);
        }

        if(!mulch_desc_str.equals("")){
            String cleanedMulchStr = new cleanedDescription(mulch_desc_str).getCleanedStr();
            mulch_desc.setText(cleanedMulchStr);
            ImageView mulchIcon = findViewById(R.id.mulchIcon);
            mulchIcon.setImageResource(R.mipmap.mulch_icon);
        }else{
            LinearLayout mulchFrame = findViewById(R.id.mulchDetailFrame);
            mulchFrame.setVisibility(View.GONE);
        }

        if(!fertilizer_desc_str.equals("")){
            String cleanedFertilizerStr = new cleanedDescription(fertilizer_desc_str).getCleanedStr();
            fertilizer_desc.setText(cleanedFertilizerStr);
            ImageView fertilizerIcon = findViewById(R.id.fertilizerIcon);
            fertilizerIcon.setImageResource(R.mipmap.roots_icon);
        }else{
            LinearLayout fertilizerFrame = findViewById(R.id.fertilizerDetailFrame);
            fertilizerFrame.setVisibility(View.GONE);
        }
        //Log.d(TAG, "learnmorecalendareventlist: " + learnMoreCalendarEventList.toString());
        if(activityIntent.hasExtra("transferChecklist")){
            transferChecklist = activityIntent.getStringArrayListExtra("transferChecklist");
        }

        TextView varietyNameTitle = findViewById(R.id.learnMoreVarietytitle);
        varietyNameTitle.setText(varietyName);

        TextView varBotanicalName = findViewById(R.id.learnMoreVarietyBotanicalName);
        varBotanicalName.setText(varietyBotanicalName);

        int varietyImage = new getImage(baselineID).getImage();

        ImageView mainImg = findViewById(R.id.learnMore_mainImg);
        mainImg.setImageResource(varietyImage);

        TextView desc = findViewById(R.id.learnMoreVarietyDescription);
        desc.setText(finalVarietyDescription);

        final TextView goToCalendarExtBtn = findViewById(R.id.goToLearnMoreVarietyCalendarBtn);
        String calBtnTxt = getApplicationContext().getString(R.string.view_entire_cal_button_string, varietyName);
        goToCalendarExtBtn.setText(calBtnTxt);
        goToCalendarExtBtn.setOnClickListener(new ViewGroup.OnClickListener(){
            @Override
            public void onClick(View v) {

                Bundle calendarBundle = new Bundle();
                calendarBundle.putSerializable("cal", learnMoreCalendarEventList);

                Intent goToCalendarIntent = new Intent(context, learnMoreAboutXVariety_Calendar.class);
                goToCalendarIntent.putExtra("calendarBundle", calendarBundle);
                goToCalendarIntent.putExtra("varietyName", varietyName);
                goToCalendarIntent.putExtra("baselineID", baselineID);
                goToCalendarIntent.putExtra("varietyID", varietyID);
                goToCalendarIntent.putExtra("botanicalName", varietyBotanicalName);
                goToCalendarIntent.putExtra("previousActivity", previousActivity);
                if(activityIntent.hasExtra("transferChecklist")){
                    //this is the checklist from the addVarietiesActivity, when user navigates away from that activity, with an active checklist, but wants to see the calendar as well
                    goToCalendarIntent.putStringArrayListExtra("transferChecklist_ext", transferChecklist);
                }
                startActivity(goToCalendarIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("fromEventsCalendar")){

                    Boolean isFallCycle = getIntent().getBooleanExtra("isFallCycle", false);
                    Intent intent = new Intent(context, eventsCalendarActivity.class);
                    intent.putExtra("eventCycle", isFallCycle);
                    String varietyID = getIntent().getStringExtra("varietyID");
                    intent.putExtra("varietySpec", varietyID);
                    String varietyName = getIntent().getStringExtra("varietyName");
                    intent.putExtra("varietyName", varietyName);
                    String upcomingEventRestore = getIntent().getStringExtra("upcomingEventDetail");
                    intent.putExtra("eventDetail", upcomingEventRestore);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if(getIntent().hasExtra("previousActivity")){
                    String previousActivityStr = getIntent().getStringExtra("previousActivity");
                    if(previousActivityStr.equals("currentVarieties")){
                        Intent intent = new Intent(context, currentVarieties.class);
                        startActivity(intent);
                    }else if (previousActivityStr.equals("addVarieties")){
                        Intent intent = new Intent(context, addVarietiesActivity.class);
                        intent.putStringArrayListExtra("transferChecklist", transferChecklist);
                        startActivity(intent);
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        //return true;
    }

    public String loadBotanicalName(String baselineID){
        String json = null;
        String botanicalName = "";
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
        try{
            JSONObject file = new JSONObject(json.trim());
            botanicalName = file.getJSONObject("botanicalNames").getString(baselineID);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return botanicalName;
    }

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

    public int getToolbarHeight(){
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public String buildDescription(String varietyName, JSONObject obj, JSONObject round1Obj, JSONObject round2Obj, JSONObject varietyTodos){

        String localeSpec = "";
        String season = "";
        try{
            season = obj.getString("season");
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        String[] nameSplit = varietyName.split("");
        if(nameSplit[nameSplit.length-1].equals("s")){
            //firstSentence = varietyName + " are " + season + " plants ";
            localeSpec = varietyName + " are " + season + " plants ";
        }else{
            //firstSentence = varietyName + " is a " + season + " plant ";
            localeSpec = varietyName + " is a " + season + " plant ";
        }

        if ((round1Obj == null) && (round2Obj == null)){
            /**meaning there is no round 1 or round2, only a single round (no differentiation)*/

            /**starting month for var*/
            String startingMonth = "";
            String startingMonthRead = "";

            /**ending month for round 1*/
            String endingMonth = "";
            String endingMonthName = "";

            /***get the length of the cycle, since there is only one cycle, then this will only involve */
            try{
                String endingMonthsplit[] = obj.getString("startHarvest").split(",");
                //Log.d(TAG, "startHarvest : " + obj.getString("startHarvest"));
                if((endingMonthsplit[0].equals("n") || (endingMonthsplit[0].equals("nn"))
                        || (endingMonthsplit[0].equals("nnn")) || (endingMonthsplit[0].equals("nnnn"))
                        || (endingMonthsplit[0].equals("nnnnn")))){

                    String yearVal = endingMonthsplit[0];
                    int endVal = Integer.valueOf(endingMonthsplit[1]);

                    switch (yearVal){
                        case "n" :
                            endVal = endVal + 12;
                            break;
                        case "nn" :
                            endVal = endVal + 24;
                            break;
                        case "nnn" :
                            endVal = endVal + 36;
                            break;
                        case "nnnn" :
                            endVal = endVal + 48;
                            break;
                        case "nnnnn" :
                            endVal = endVal + 60;
                            break;
                    }

                    endingMonth = String.valueOf(endVal);
                    endingMonthName = endingMonthsplit[1];

                    //endingMonth = endingMonthsplit[1];
                } else {
                    endingMonth = endingMonthsplit[0];
                    endingMonthName = endingMonth;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
            //Log.d(TAG, "endingMonth : " + endingMonth);
            String endingMonthRead = getReadableMonth(endingMonthName);

            /**then we put it together in a readable format*/
            /***round1 sentence*/
            if(obj.has("startInside")){
                try{
                    String dateStr = obj.getString("startInside");
                    String dateStrSplit[] = dateStr.split(",");

                    if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn"))
                            || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                        startingMonth = dateStrSplit[1];
                    } else {
                        startingMonth = dateStrSplit[0];
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }

                startingMonthRead = getReadableMonth(startingMonth);

                String length = getCycleLength(startingMonth, endingMonth);

                localeSpec = localeSpec + " with a " + length + " - month growing cycle that begins indoors in " + startingMonthRead +
                        " and is ready to harvest in " + endingMonthRead + ". ";

            } else {
                String dateStr = "";
                if(obj.has("directOutside")){
                    try{
                        dateStr = obj.getString("directOutside");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                } else if(obj.has("directOutsideCovered")){
                    try{
                        dateStr = obj.getString("directOutsideCovered");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
                String dateStrSplit[] = dateStr.split(",");
                if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn")) || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                    startingMonth = dateStrSplit[1];
                } else {
                    startingMonth = dateStrSplit[0];
                }
                startingMonthRead = getReadableMonth(startingMonth);

                String length = getCycleLength(startingMonth, endingMonth);

                localeSpec = localeSpec + " with a " + length + " - month growing cycle that begins in " + startingMonthRead +
                        " and is ready to harvest in " + endingMonthRead + ". ";
            }

        } else {

            /***meaning that there are two rounds to consider*/

            localeSpec = localeSpec + "with 2 possible growing cycles throughout the year in your zone.";

            /**starting month for round 1*/
            String startingMonthR1 = "";
            String startingMonthR1Read = "";

            /**ending month for round 1*/
            String endingMonthR1 = "";
            String r1EndMonth = "";
            try{
                String endingMonthR1split[] = round1Obj.getString("startHarvest").split(",");

                if((endingMonthR1split[0].equals("n") || (endingMonthR1split[0].equals("nn"))
                        || (endingMonthR1split[0].equals("nnn")) || (endingMonthR1split[0].equals("nnnn"))
                        || (endingMonthR1split[0].equals("nnnnn")))){

                    //endingMonthR1 = endingMonthR1split[1];
                    r1EndMonth = endingMonthR1split[1];

                    /***added*/
                    String yearVal = endingMonthR1split[0];
                    int endVal = Integer.valueOf(endingMonthR1split[1]);
                    //Log.d(TAG, "YEAR VAL = " + yearVal);
                    switch (yearVal){
                        case "n" :
                            endVal = endVal + 12;
                            break;
                        case "nn" :
                            endVal = endVal + 24;
                            break;
                        case "nnn" :
                            endVal = endVal + 36;
                            break;
                        case "nnnn" :
                            endVal = endVal + 48;
                            break;
                        case "nnnnn" :
                            endVal = endVal + 60;
                            break;
                    }
                    //Log.d(TAG, "ENDVAL = " + endVal);
                    endingMonthR1 = String.valueOf(endVal);
                } else {
                    endingMonthR1 = endingMonthR1split[0];
                    r1EndMonth = endingMonthR1;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            String endingMonthR1Read = getReadableMonth(r1EndMonth);

            /**starting month for round 2*/
            String startingMonthR2 = "";
            String startingMonthR2Read = "";

            /**ending month for round 2*/
            String endingMonthR2 = "";
            String r2EndMonth = "";
            try{
                String endingMonthR2split[]= round2Obj.getString("startHarvest2").split(",");

                if((endingMonthR2split[0].equals("n") || (endingMonthR2split[0].equals("nn"))
                        || (endingMonthR2split[0].equals("nnn")) || (endingMonthR2split[0].equals("nnnn"))
                        || (endingMonthR2split[0].equals("nnnnn")))){

                    r2EndMonth = endingMonthR2split[1];
                    String yearVal = endingMonthR2split[0];
                    int endVal = Integer.valueOf(endingMonthR2split[1]);

                    switch (yearVal){
                        case "n" :
                            endVal = endVal + 12;
                            break;
                        case "nn" :
                            endVal = endVal + 24;
                            break;
                        case "nnn" :
                            endVal = endVal + 36;
                            break;
                        case "nnnn" :
                            endVal = endVal + 48;
                            break;
                        case "nnnnn" :
                            endVal = endVal + 60;
                            break;
                    }

                    endingMonthR2 = String.valueOf(endVal);

                    //endingMonthR2 = endingMonthR2split[1];
                } else {

                    endingMonthR2 = endingMonthR2split[0];

                    r2EndMonth = endingMonthR2;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            String endingMonthR2Read = getReadableMonth(r2EndMonth);

            /***round1 sentence*/
            if(round1Obj.has("startInside")){

                try{
                    String dateStr = round1Obj.getString("startInside");
                    String dateStrSplit[] = dateStr.split(",");

                    if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn"))
                            || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                        startingMonthR1 = dateStrSplit[1];
                    } else {
                        startingMonthR1 = dateStrSplit[0];
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }

                startingMonthR1Read = getReadableMonth(startingMonthR1);

                String length = getCycleLength(startingMonthR1, endingMonthR1);

                localeSpec = localeSpec + " The first is a " + length + " - month cycle that begins indoors in " + startingMonthR1Read +
                        " and is ready to harvest in " + endingMonthR1Read + ", ";

            } else {
                String dateStr = "";
                if(round1Obj.has("directOutside")){
                    try{
                        dateStr = round1Obj.getString("directOutside");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                } else if(round1Obj.has("directOutsideCovered")){
                    try{
                        dateStr = round1Obj.getString("directOutsideCovered");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
                String dateStrSplit[] = dateStr.split(",");
                if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn")) || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                    startingMonthR1 = dateStrSplit[1];
                } else {
                    startingMonthR1 = dateStrSplit[0];
                }
                startingMonthR1Read = getReadableMonth(startingMonthR1);

                String length = getCycleLength(startingMonthR1, endingMonthR1);

                localeSpec = localeSpec + " The first is a " + length + " - month cycle that begins in " + startingMonthR1Read +
                        " and is ready to harvest in " + endingMonthR1Read + ", ";
            }

            /**round 2 sentence*/
            if(round2Obj.has("startInside2")){

                try{
                    String dateStr = round2Obj.getString("startInside2");

                    String dateStrSplit[] = dateStr.split(",");

                    if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn"))
                            || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                        startingMonthR2 = dateStrSplit[1];
                    } else {
                        startingMonthR2 = dateStrSplit[0];
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }

                startingMonthR2Read = getReadableMonth(startingMonthR2);

                String length = getCycleLength(startingMonthR2, endingMonthR2);

                localeSpec = localeSpec + "while the second is a " + length + " - month cycle that begins indoors in " + startingMonthR2Read +
                        " and is ready to harvest in " + endingMonthR2Read + ".";

            } else {
                String dateStr = "";
                try{
                    if(round2Obj.has("directOutside2")){
                        dateStr = round2Obj.getString("directOutside2");
                    } else if(round2Obj.has("directOutside2Covered")){
                        dateStr = round2Obj.getString("directOutside2Covered");
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
                String dateStrSplit[] = dateStr.split(",");
                if((dateStrSplit[0].equals("n") || (dateStrSplit[0].equals("nn")) || (dateStrSplit[0].equals("nnn")) || (dateStrSplit[0].equals("nnnn")) || (dateStrSplit[0].equals("nnnnn")))){
                    startingMonthR2 = dateStrSplit[1];
                } else {
                    startingMonthR2 = dateStrSplit[0];
                }
                startingMonthR2Read = getReadableMonth(startingMonthR2);

                String length = getCycleLength(startingMonthR2, endingMonthR2);

                localeSpec = localeSpec + "while the second is a " + length + " - month cycle that begins in " + startingMonthR2Read +
                        " and is ready to harvest in " + endingMonthR2Read + ". ";
            }
        }
        return localeSpec;
    }

    public String getCycleLength(String startMonth, String endMonth){
        String lengthStr = "";
        int startVal = Integer.valueOf(startMonth);
        int endVal = Integer.valueOf(endMonth);

        int lengthVal = endVal - startVal;
        lengthStr = String.valueOf(lengthVal);
        return lengthStr;
    }
    public String getReadableMonth(String monthNum){
        String monthRead = "";
        switch(monthNum){
            case("1"):
                monthRead = "January";
                break;
            case("2"):
                monthRead = "February";
                break;
            case("3"):
                monthRead = "March";
                break;
            case("4"):
                monthRead = "April";
                break;
            case("5"):
                monthRead = "May";
                break;
            case("6"):
                monthRead = "June";
                break;
            case("7"):
                monthRead = "July";
                break;
            case("8"):
                monthRead = "August";
                break;
            case("9"):
                monthRead = "September";
                break;
            case("10"):
                monthRead = "October";
                break;
            case("11"):
                monthRead = "November";
                break;
            case("12"):
                monthRead = "December";
                break;
            default :
                monthRead = "";
        }
        return monthRead;
    }
}