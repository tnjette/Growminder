package com.tylerjette.growmindv05;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class firstRoundArrestedEventList {
    private List<String[]> listOfVarietyEvents;
    public firstRoundArrestedEventList(JSONObject varietyObj, String varID, String yearStr, int year){
        this.listOfVarietyEvents = new ArrayList<String[]>();
        Boolean startInsideDateCheck;
        Boolean startHardenOffDateCheck;
        Boolean startOutsideDateCheck = false;
        Boolean directOutsideDateCheck;
        String stopStr = "stopOutside";
        Boolean stopOutsideDateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();
        if(varietyObj.has("startInside")){
            String insideStr = "startInside";
            startInsideDateCheck = new checkDate(insideStr, varietyObj, yearStr, year).getIsDateValid();
            if(startInsideDateCheck){
            }else if(!startInsideDateCheck){
                if(varietyObj.has("startHardenOff")){
                    String hardenStr = "startHardenOff";
                    startHardenOffDateCheck = new checkDate(hardenStr, varietyObj, yearStr, year).getIsDateValid();
                    if(startHardenOffDateCheck){
                    }else if(!startHardenOffDateCheck){
                        if((varietyObj.has("startOutside")) || (varietyObj.has("startOutsideCovered"))){
                            String startOutsideStr = "";
                            if(varietyObj.has("startOutside")){
                                startOutsideStr = "startOutside";
                            }else if(varietyObj.has("startOutsideCovered")){
                                startOutsideStr = "startOutsideCovered";
                            }
                            startOutsideDateCheck = new checkDate(startOutsideStr, varietyObj, yearStr, year).getIsDateValid();
                        }
                        if(startOutsideDateCheck){
                        } else if (!startOutsideDateCheck){
                            if(stopOutsideDateCheck){
                            }else if(!stopOutsideDateCheck){
                                Iterator<?> firstKeys = varietyObj.keys();
                                while(firstKeys.hasNext()){
                                    String key = (String)firstKeys.next();
                                    if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))){
                                        Boolean stillValidFutureDate = new checkDate(key, varietyObj,yearStr, year).getIsDateValid();
                                        if(stillValidFutureDate){
                                            String value = "";
                                            try{
                                                value = varietyObj.getString(key);
                                            }catch(JSONException ex){
                                                ex.printStackTrace();
                                            }
                                            String modDate = new modifyDate(yearStr,year,value).getModifiedDate();
                                            String[] arrestedEvent = {varID, modDate, key};
                                            listOfVarietyEvents.add(arrestedEvent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if(!varietyObj.has("startHardenOff")){
                    if((varietyObj.has("startOutside")) || (varietyObj.has("startOutsideCovered"))){
                        String outsideStr = "";
                        if(varietyObj.has("startOutside")){
                            outsideStr = "startOutside";
                        }else if(varietyObj.has("startOutsideCovered")){
                            outsideStr = "startOutsideCovered";
                        }
                        startOutsideDateCheck = new checkDate(outsideStr, varietyObj, yearStr, year).getIsDateValid();
                    }
                    if(startOutsideDateCheck){
                    } else if (!startOutsideDateCheck){
                        if(stopOutsideDateCheck){
                        }else if(!stopOutsideDateCheck){
                            Iterator<?> firstKeys = varietyObj.keys();
                            while(firstKeys.hasNext()){
                                String key = (String)firstKeys.next();
                                if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))){
                                    Boolean stillValidFutureDate = new checkDate(key, varietyObj,yearStr, year).getIsDateValid();
                                    if(stillValidFutureDate){
                                        String value = "";
                                        try{
                                            value = varietyObj.getString(key);
                                        }catch(JSONException ex){
                                            ex.printStackTrace();
                                        }
                                        String modDate = new modifyDate(yearStr,year,value).getModifiedDate();
                                        String[] arrestedEvent = {varID, modDate, key};
                                        listOfVarietyEvents.add(arrestedEvent);
                                    }
                                }
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
            directOutsideDateCheck = new checkDate(directStr, varietyObj, yearStr, year).getIsDateValid();
            if(directOutsideDateCheck){
            }else if(!directOutsideDateCheck){
                if(stopOutsideDateCheck){
                }else if(!stopOutsideDateCheck){
                    Iterator<?> firstKeys = varietyObj.keys();
                    while(firstKeys.hasNext()){
                        String key = (String)firstKeys.next();
                        if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))){
                            Boolean stillValidFutureDate = new checkDate(key, varietyObj,yearStr, year).getIsDateValid();
                            if(stillValidFutureDate){
                                String value = "";
                                try{
                                    value = varietyObj.getString(key);
                                }catch(JSONException ex){
                                    ex.printStackTrace();
                                }
                                String modDate = new modifyDate(yearStr,year,value).getModifiedDate();

                                String[] arrestedEvent = {varID, modDate, key};
                                listOfVarietyEvents.add(arrestedEvent);
                            }
                        }
                    }
                }
            }
        }//end of single round
        //return listOfVarietyEvents;
        //Log.d(TAG, "THIS IS THE FIRST ROUND OF ARRESTED EVENT LIST : " + listOfVarietyEvents.size());
    }

    public List<String[]> getListOfVarietyEvents(){
        return listOfVarietyEvents;
    }
}
