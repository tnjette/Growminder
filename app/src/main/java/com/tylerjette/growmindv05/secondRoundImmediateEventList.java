package com.tylerjette.growmindv05;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class secondRoundImmediateEventList {

    private List<String[]> listOfVarietyEvents = new ArrayList<String[]>();
    //private static String TAG = secondRoundImmediateEventList.class.getSimpleName();

    public secondRoundImmediateEventList(JSONObject readableNamesFile, JSONObject todoJSONObj, JSONObject originalVarietyObject, JSONObject varietyObj, String varID, String yearStr, int year){
        //Log.d(TAG, "AT the beginning of the secondRoundImmediateEventList, this is the String yearStr : " + yearStr +" , and this is the Integer year : " + String.valueOf(year));

        if(varietyObj.has("startInside2")){
            //Log.d(TAG, "variety has startInside2");
            String inside2Str = "startInside2";
            Boolean startInside2DateCheck = false;
            startInside2DateCheck = new checkDate(inside2Str, varietyObj, yearStr, year).getIsDateValid();
            if(startInside2DateCheck){
                //Log.d(TAG, "variety has startInside2 and it is GOOD");
                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 1, 1, 1, 0,1).getSecondRoundListOfEvents();
            }else if(!startInside2DateCheck) { //if startInside is EXPIRED
                //Log.d(TAG, "variety has startInside2 but it HAS EXPIRED");
                if (varietyObj.has("startHardenOff2")) {
                    //Log.d(TAG, "variety has startHardenOff");
                    Boolean startHardenOff2DateCheck = false;
                    String harden2Str = "startHardenOff2";
                    startHardenOff2DateCheck = new checkDate(harden2Str, varietyObj, yearStr, year).getIsDateValid();

                    if (startHardenOff2DateCheck) { //startInside has EXPIRED, startHardenOff is still good
                        this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 1, 1, 0, 1).getSecondRoundListOfEvents();
                    } else if (!startHardenOff2DateCheck) { // startInside and startHardenOff have expired...
                        Boolean startOutside2DateCheck = false;
                        if ((varietyObj.has("startOutside2")) || (varietyObj.has("startOutside2Covered"))) {
                            String startOutside2Str = "";
                            if (varietyObj.has("startOutside2")) {
                                startOutside2Str = "startOutside2";
                            } else if (varietyObj.has("startOutside2Covered")) {
                                startOutside2Str = "startOutside2Covered";
                            }
                            startOutside2DateCheck = new checkDate(startOutside2Str, varietyObj, yearStr, year).getIsDateValid();
                        }
                        if (startOutside2DateCheck) { //startInside and startHardenOff have expired, but startOutside is good
                            this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, -1, 1, 0, 1).getSecondRoundListOfEvents();
                        } else if (!startOutside2DateCheck) {//startInside and startHardenOff and startOutside have EXPIRED
                            String stopStr = "stopOutside2";
                            Boolean stopOutside2DateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                            if (stopOutside2DateCheck) {//startInside, startHardenOff, startOutside have expired, but stopOutside is good.
                                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, -1, -1, 0, 1).getSecondRoundListOfEvents();
                            } else if (!stopOutside2DateCheck) { //all expired
                                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, -1, -1, -0, -1).getSecondRoundListOfEvents();
                            }
                        }
                    }
                } else if (!varietyObj.has("startHardenOff2")) {
                    if ((varietyObj.has("startOutside2")) || (varietyObj.has("startOutside2Covered"))) {
                        //hasStartOutside2 = true;
                        String outside2Str = "";
                        if (varietyObj.has("startOutside2")) {
                            outside2Str = "startOutside2";
                        } else if (varietyObj.has("startOutside2Covered")) {
                            outside2Str = "startOutside2Covered";
                        }
                        Boolean startOutside2DateCheck = new checkDate(outside2Str, varietyObj, yearStr, year).getIsDateValid();
                        if (startOutside2DateCheck) { //startInside and startHardenOff have expired, but startOutside is good
                            this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, 1, 0, 1).getSecondRoundListOfEvents();
                        } else if (!startOutside2DateCheck) {//startInside and startHardenOff and startOutside have EXPIRED
                            String stopStr = "stopOutside2";
                            Boolean stopOutside2DateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                            if (stopOutside2DateCheck) {//startInside, startHardenOff, startOutside have expired, but stopOutside is good.
                                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, -1, 0, 1).getSecondRoundListOfEvents();
                            } else if (!stopOutside2DateCheck) { //all expired
                                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, -1, -0, -1).getSecondRoundListOfEvents();
                            }
                        }
                    }
                }
            }
        } // end of if(variety.has("startInside");
        if((varietyObj.has("directOutside2")) || (varietyObj.has("directOutside2Covered"))){
            String direct2Str = "";
            if(varietyObj.has("directOutside2")){
                direct2Str = "directOutside2";
            }else if(varietyObj.has("directOutside2Covered")){
                direct2Str = "directOutside2Covered";
            }
            Boolean directOutside2DateCheck = new checkDate(direct2Str, varietyObj, yearStr, year).getIsDateValid();
            if(directOutside2DateCheck){
                this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,1,1).getSecondRoundListOfEvents();
            }else if(!directOutside2DateCheck){
                String stopStr = "stopOutside2";
                Boolean stopOutside2DateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                if(stopOutside2DateCheck){ //directOutside is expired, but stopOutside is still good
                    this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,-1,1).getSecondRoundListOfEvents();
                }else if(!stopOutside2DateCheck){
                    this.listOfVarietyEvents = new makeSecondRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,-1, -1).getSecondRoundListOfEvents();
                }
            }
        }//end of single round
        //Log.d(TAG, "LIST OF VARIETYEVENTS FROM THE SECOND ROUND MUTHA FUCKA! : "+listOfVarietyEvents.toString());
        //return listOfVarietyEvents;
        //Log.d(TAG, "Second Round of IMMEDIATE EVENTS : " + listOfVarietyEvents.size());
    }

    public List<String[]> getListOfVarietyEvents(){
        return listOfVarietyEvents;
    }
}
