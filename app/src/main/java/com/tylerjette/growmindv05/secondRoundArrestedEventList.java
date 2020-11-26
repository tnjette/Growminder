package com.tylerjette.growmindv05;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class secondRoundArrestedEventList {
    private List<String[]> listOfVarietyEvents = new ArrayList<String[]>();
    //private static String TAG = secondRoundArrestedEventList.class.getSimpleName();
    public secondRoundArrestedEventList(JSONObject varietyObj, String varID, String yearStr, int year){
        //Log.d(TAG, "AT the beginning of the secondRoundArrestedEventList, this is the String yearStr : " + yearStr +" , and this is the Integer year : " + String.valueOf(year));

        Boolean startInside2DateCheck = false;
        Boolean startHardenOff2DateCheck = false;
        Boolean startOutside2DateCheck = false;
        Boolean directOutside2DateCheck= false;

        String stopStr = "stopOutside2";
        Boolean stopOutside2DateCheck = new checkDate(stopStr, varietyObj, yearStr, year).getIsDateValid();

        if(varietyObj.has("startInside2")){
            String inside2Str = "startInside2";
            startInside2DateCheck = new checkDate(inside2Str, varietyObj, yearStr, year).getIsDateValid();
            if(startInside2DateCheck){
            }else if(!startInside2DateCheck){ //if startInside is EXPIRED
                if(varietyObj.has("startHardenOff2")){
                    String harden2Str = "startHardenOff2";
                    startHardenOff2DateCheck = new checkDate(harden2Str, varietyObj, yearStr, year).getIsDateValid();
                    if(startHardenOff2DateCheck){ //startInside has EXPIRED, startHardenOff is still good
                    }else if(!startHardenOff2DateCheck){ // startInside and startHardenOff have expired...
                        if((varietyObj.has("startOutside2")) || (varietyObj.has("startOutside2Covered"))){
                            String startOutside2Str = "";
                            if(varietyObj.has("startOutside2")){
                                startOutside2Str = "startOutside2";
                            }else if(varietyObj.has("startOutside2Covered")){
                                startOutside2Str = "startOutside2Covered";
                            }
                            startOutside2DateCheck = new checkDate(startOutside2Str, varietyObj, yearStr, year).getIsDateValid();
                        }
                        if(startOutside2DateCheck){ //startInside and startHardenOff have expired, but startOutside is good
                        } else if (!startOutside2DateCheck){//startInside and startHardenOff and startOutside have EXPIRED
                            if(stopOutside2DateCheck){//startInside, startHardenOff, startOutside have expired, but stopOutside is good.
                            }else if(!stopOutside2DateCheck){ //all expired

                                /***CHECK TO SEE IF THERE ARE ANY KEYS LEFT IN THE FUTURE RELATED TO THIS CYCLE*/
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
                                            this.listOfVarietyEvents.add(arrestedEvent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if(!varietyObj.has("startHardenOff2")){
                    if((varietyObj.has("startOutside2")) || (varietyObj.has("startOutside2Covered"))){
                        String outside2Str = "";
                        if(varietyObj.has("startOutside2")){
                            outside2Str = "startOutside2";
                        }else if(varietyObj.has("startOutside2Covered")){
                            outside2Str = "startOutside2Covered";
                        }
                        startOutside2DateCheck = new checkDate(outside2Str, varietyObj, yearStr, year).getIsDateValid();
                    }
                    if(startOutside2DateCheck){
                    } else if (!startOutside2DateCheck){//startInside and startHardenOff and startOutside have EXPIRED
                        if(stopOutside2DateCheck){//startInside, startHardenOff, startOutside have expired, but stopOutside is good.
                            //listOfVarietyEvents = makeSingleRoundList(varietyObj, varID, yearStr, year, -1,0,-1,0,1);
                        }else if(!stopOutside2DateCheck){ //all expired

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
                                        this.listOfVarietyEvents.add(arrestedEvent);
                                    }
                                }
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
            //directOutside2DateCheck = checkDate(direct2Str, varietyObj, yearStr, year);
            directOutside2DateCheck = new checkDate(direct2Str, varietyObj, yearStr, year).getIsDateValid();
            if(directOutside2DateCheck){
                //listOfVarietyEvents = makeSingleRoundList(varietyObj, varID, yearStr, year, 0,0,0,1,1);
            }else if(!directOutside2DateCheck){
                if(stopOutside2DateCheck){ //directOutside is expired, but stopOutside is still good
                    //listOfVarietyEvents = makeSingleRoundList(varietyObj, varID, yearStr, year, 0,0,0,-1,1);
                }else if(!stopOutside2DateCheck){
                    //listOfVarietyEvents = makeSingleRoundList(varietyObj, varID, yearStr, year, 0,0,0,-1, -1);

                    /***CHECK TO SEE IF THERE ARE ANY KEYS LEFT IN THE FUTURE RELATED TO THIS CYCLE*/
                    Iterator<?> firstKeys = varietyObj.keys();
                    while(firstKeys.hasNext()){
                        String key = (String)firstKeys.next();

                        if((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))){

                            //Boolean stillValidFutureDate = checkDate(key, varietyObj,yearStr, year);
                            Boolean stillValidFutureDate = new checkDate(key, varietyObj,yearStr, year).getIsDateValid();
                            if(stillValidFutureDate){
                                //put this item into the listOfVarietyEvents
                                String value = "";
                                try{
                                    value = varietyObj.getString(key);
                                }catch(JSONException ex){
                                    ex.printStackTrace();
                                }
                                //String[] arrestedEvent = {varID, value, key};
                                //String modDate = modifyDate(yearStr,year,value);REFACTORED
                                String modDate = new modifyDate(yearStr,year,value).getModifiedDate();

                                String[] arrestedEvent = {varID, modDate, key};
                                this.listOfVarietyEvents.add(arrestedEvent);
                            }
                        }
                    }
                }
            }
        }//end of single round
        //Log.d(TAG, "LIST OF VARIETYEVENTS FROM THE SECOND ROUND MUTHA FUCKA! : "+listOfVarietyEvents.toString());
        //return listOfVarietyEvents;
        //Log.d(TAG, "Second round of arrestedEvent List : " + listOfVarietyEvents.size());
    }
    public List<String[]> getListOfVarietyEvents(){
        return listOfVarietyEvents;
    }
}
