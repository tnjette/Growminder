package com.tylerjette.growmindv05;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class createArrestedVarietyEventList {

    private List<String[]> arrestedVarietyEvents;

    public createArrestedVarietyEventList(String varId, JSONObject firstRoundKeys, JSONObject secondRoundKeys, String yearStr, int year){

        //this method will look to see which events are obsolete based on where in the calendar year the cycle falls.
        //if the cycle is deemed obsolete, then every event in the cycle that comes after the
        //threshold point ((stopplanting) or whatever it's called)will need to be added to the list, since
        //those events have not passed yet, but will still be triggered otherwise.

        this.arrestedVarietyEvents = new ArrayList<>();

        if(secondRoundKeys == null){
            //only one cycle
            this.arrestedVarietyEvents = new firstRoundArrestedEventList(firstRoundKeys,varId, yearStr, year).getListOfVarietyEvents();
        }else{
            //both cycles
            this.arrestedVarietyEvents = new firstRoundArrestedEventList(firstRoundKeys, varId,yearStr,year).getListOfVarietyEvents();
            List<String[]> secondround = new secondRoundArrestedEventList(secondRoundKeys, varId,yearStr,year).getListOfVarietyEvents();
            for(int t = 0; t<secondround.size(); t++){
                this.arrestedVarietyEvents.add(secondround.get(t));
            }
        }
        //return arrestedVarietyEvents;
    }
    public List<String[]> getArrestedVarietyEventList(){
        return arrestedVarietyEvents;
    }
}
