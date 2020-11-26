package com.tylerjette.growmindv05;

import org.json.JSONObject;

import java.util.List;

public class createImmediateVarietyEventList {

    private List<String[]> immediateVarietyEventList;

    public createImmediateVarietyEventList(JSONObject readableNamesFile, JSONObject todoJSONObj, JSONObject originalVarietyObj, String varID, JSONObject firstRoundKeys, JSONObject secondRoundKeys, JSONObject varietyTodoObj, int year, String yearStr){
        //List<String[]> immediateVarietyEventList;

        if(secondRoundKeys == null){
            //run for only first round
            this.immediateVarietyEventList = new firstRoundImmediateEventList(readableNamesFile,todoJSONObj, originalVarietyObj, firstRoundKeys, varID, yearStr, year).getFirstRoundImmediateList();

        }else{
            List<String[]> firstRoundImmediateEventList = new firstRoundImmediateEventList(readableNamesFile,todoJSONObj, originalVarietyObj, firstRoundKeys, varID, yearStr, year).getFirstRoundImmediateList();
            List<String[]> secondRoundImmediateEventList = new secondRoundImmediateEventList(readableNamesFile,todoJSONObj, originalVarietyObj, secondRoundKeys, varID, yearStr, year).getListOfVarietyEvents();

            this.immediateVarietyEventList = firstRoundImmediateEventList;

            for(int k = 0; k<secondRoundImmediateEventList.size(); k++){
                this.immediateVarietyEventList.add(secondRoundImmediateEventList.get(k));
            }
        }
        //return immediateVarietyEventList;
    }

    public List<String[]> getImmediateVarietyEventList(){
        return immediateVarietyEventList;
    }
}
