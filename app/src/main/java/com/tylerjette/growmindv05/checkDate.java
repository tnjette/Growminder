package com.tylerjette.growmindv05;

import org.json.JSONException;
import org.json.JSONObject;

public class checkDate {

    private Boolean isDateValid;

    public checkDate(String dateToCheck, JSONObject varietyObj, String yearStr, int year){
        String dateToCheckStr = "";
        try {
            dateToCheckStr = varietyObj.getString(dateToCheck);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        //String finalDateStr = modifyDate(yearStr, year, dateToCheckStr);
        String finalDateStr = new modifyDate(yearStr, year, dateToCheckStr).getModifiedDate();
        //Log.d(TAG, "then, from the checkDate method, here is the final date str again... see if it has changed: " + finalDateStr);
        //Boolean isDateValid = valiDate(finalDateStr);
        this.isDateValid = new validDate(finalDateStr).getValidDate();
    }
    public Boolean getIsDateValid(){
        return isDateValid;
    }
}
