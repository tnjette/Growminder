package com.tylerjette.growmindv05;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class firstRoundImmediateEventList {
    private List<String[]> listOfVarietyEvents = new ArrayList<String[]>();
    public firstRoundImmediateEventList(/**adding readableNamesFile*/ JSONObject readableNamesFile,/**adding the todoJSONObj*/JSONObject todoJSONObj, JSONObject originalVarietyObject, JSONObject varietyObj, String varID, String yearStr, int year){
        //Log.d(TAG, "AT the beginning of the firstRoundImmediateEventList, this is the String yearStr : " + yearStr +" , and this is the Integer year : " + String.valueOf(year));
        if(varietyObj.has("startInside")){
            String insideStr = "startInside";
            //Boolean startInsideDateCheck = checkDate(insideStr, varietyObj, yearStr, year);Refactored
            Boolean startInsideDateCheck = new checkDate(insideStr, varietyObj, yearStr, year).getIsDateValid();
            if(startInsideDateCheck){
                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 1, 1, 1, 0,1).getFirstRoundList();
            }else if(!startInsideDateCheck) { //if startInside is EXPIRED
                if (varietyObj.has("startHardenOff")) {
                    String hardenStr = "startHardenOff";
                    //Boolean startHardenOffDateCheck = checkDate(hardenStr, varietyObj, yearStr, year);Refactored
                    Boolean startHardenOffDateCheck = new checkDate(hardenStr, varietyObj, yearStr, year).getIsDateValid();
                    if (startHardenOffDateCheck) { //startInside has EXPIRED, startHardenOff is still good
                        this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 1, 1, 0, 1).getFirstRoundList();
                    } else if (!startHardenOffDateCheck) { // startInside and startHardenOff have expired...
                        Boolean startOutsideDateCheck = false;
                        if ((varietyObj.has("startOutside")) || (varietyObj.has("startOutsideCovered"))) {
                            String startOutsideStr = "";
                            if (varietyObj.has("startOutside")) {
                                startOutsideStr = "startOutside";
                            } else if (varietyObj.has("startOutsideCovered")) {
                                startOutsideStr = "startOutsideCovered";
                            }
                            startOutsideDateCheck = new checkDate(startOutsideStr, varietyObj, yearStr, year).getIsDateValid();
                        }
                        if (startOutsideDateCheck) {
                            this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, -1, 1, 0, 1).getFirstRoundList();
                        } else if (!startOutsideDateCheck) {
                            String stopStr = "stopOutside";
                            Boolean stopOutsideDateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                            if (stopOutsideDateCheck) {
                                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject,varietyObj, varID, yearStr, year, -1, -1, -1, 0, 1).getFirstRoundList();
                            } else if (!stopOutsideDateCheck) {
                                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj,originalVarietyObject, varietyObj, varID, yearStr, year, -1, -1, -1, -0, -1).getFirstRoundList();
                            }
                        }
                    }
                } else if (!varietyObj.has("startHardenOff")) {
                    if ((varietyObj.has("startOutside")) || (varietyObj.has("startOutsideCovered"))) {
                        String outsideStr = "";
                        if (varietyObj.has("startOutside")) {
                            outsideStr = "startOutside";
                        } else if (varietyObj.has("startOutsideCovered")) {
                            outsideStr = "startOutsideCovered";
                        }
                        Boolean startOutsideDateCheck = new checkDate(outsideStr, varietyObj, yearStr, year).getIsDateValid();
                        if (startOutsideDateCheck) {
                            this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, 1, 0, 1).getFirstRoundList();
                        } else if (!startOutsideDateCheck) {
                            String stopStr = "stopOutside";
                            Boolean stopOutsideDateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                            if (stopOutsideDateCheck) {
                                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, -1, 0, 1).getFirstRoundList();
                            } else if (!stopOutsideDateCheck) {
                                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, -1, 0, -1, -0, -1).getFirstRoundList();
                            }
                        }
                    }
                }
            }
        }
        if((varietyObj.has("directOutside")) || (varietyObj.has("directOutsideCovered"))){
            String directStr = "";
            if(varietyObj.has("directOutside")){
                directStr = "directOutside";
            }else if(varietyObj.has("directOutsideCovered")){
                directStr = "directOutsideCovered";
            }
            Boolean directOutsideDateCheck = new checkDate(directStr, varietyObj, yearStr, year).getIsDateValid();
            if(directOutsideDateCheck){
                this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,1,1).getFirstRoundList();
            }else if(!directOutsideDateCheck){
                String stopStr = "stopOutside";
                Boolean stopOutsideDateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
                if(stopOutsideDateCheck){
                    this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,-1,1).getFirstRoundList();
                }else if(!stopOutsideDateCheck){
                    this.listOfVarietyEvents = new makeFirstRoundList(readableNamesFile, todoJSONObj, originalVarietyObject, varietyObj, varID, yearStr, year, 0,0,0,-1, -1).getFirstRoundList();
                }
            }
        }//end of single round
        //return listOfVarietyEvents;
        //Log.d(TAG, "THIS is the firstRoundImmediateList : " + listOfVarietyEvents.size());
    }

    public List<String[]> getFirstRoundImmediateList(){
        return listOfVarietyEvents;
    }
}
