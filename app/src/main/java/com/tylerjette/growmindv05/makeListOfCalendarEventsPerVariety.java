package com.tylerjette.growmindv05;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class makeListOfCalendarEventsPerVariety {

    private String baselineID;
    private String varietyID;
    private String userZone;
    private String varietyName;

    private ArrayList<learnMoreCalendarEvent> varietyCalendarEventList = new ArrayList<>();
    private JSONObject fileByZone;
    private JSONArray zoneFileArray;
    Context context;

    public makeListOfCalendarEventsPerVariety(Context context, String varietyID){
        this.varietyID = varietyID;
        this.context = context;
        this.baselineID = varietyID.replaceAll("[0-9]", "").toLowerCase();
        this.varietyName = new getReadableName(baselineID, context).getName();
        this.userZone = varietyID.toLowerCase().replaceAll("[a-z]", "");

        String finalVarietyDescription = "";
        try {
            fileByZone = new JSONObject(loadbyZone(userZone).trim());
            zoneFileArray = fileByZone.getJSONArray("zone" + userZone);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        try{
            for(int i = 0; i < zoneFileArray.length(); i++){
                JSONObject var = (JSONObject)zoneFileArray.get(i);
                String varID = var.getString("id").toLowerCase();
                if(varID.equals(varietyID)){

                    String baselineVarID = varietyID.replaceAll("[0-9]", "");
                    JSONObject toDosFile = new JSONObject(loadToDos().trim());
                    JSONObject varietyTodos = (JSONObject)toDosFile.get(baselineVarID);
                    if(var.has("round2")){
                        JSONObject round1Obj = var.getJSONObject("round1");
                        JSONObject round2Obj = var.getJSONObject("round2");

                        finalVarietyDescription = new buildDescription(varietyName, var, round1Obj, round2Obj, varietyTodos).getLocaleSpec();

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
                            varietyCalendarEventList.add(event);
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
                            varietyCalendarEventList.add(event);
                        }
                    }else if (!var.has("round2")){
                        finalVarietyDescription = new buildDescription(varietyName, var, null, null, varietyTodos).getLocaleSpec();

                        Iterator<?> keys = var.keys();
                        while(keys.hasNext()){

                            String key = (String)keys.next();
                            //Log.d(TAG, "from single round variety: " + keys);
                            if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season"))){
                                //run each of the keys with the corresponding todos from the todos file
                                //capturing based on the baselineVarietyID
                                String eventTitle = keys.toString();
                                //String key = (String)keys.next();
                                String dateObj = var.getString(key);
                                //String eventDetail = (String)varietyTodos.getString(key);
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

                                varietyCalendarEventList.add(event);
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
    }
    //getter method
    public ArrayList<learnMoreCalendarEvent> getVarietyCalendarList(){
        return varietyCalendarEventList;
    }

    public String loadbyZone(String zone){ //loads the zonefile when the first try/catch statement is successful
        String json = null;
        try{
            InputStream zoneFile = context.getAssets().open("zone" + zone + ".json");
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
            InputStream todoFile = context.getAssets().open("todos.json");
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
}
