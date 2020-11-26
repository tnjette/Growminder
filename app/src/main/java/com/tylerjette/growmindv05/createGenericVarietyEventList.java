package com.tylerjette.growmindv05;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class createGenericVarietyEventList {

    private List<String[]> genericVarietyEventList;

    public createGenericVarietyEventList(String varietyID, JSONObject firstRoundKeys, JSONObject secondRoundKeys, JSONObject varietyTodoObj){
        //List<String[]> genericVarietyEventList = new ArrayList<>();
        this.genericVarietyEventList = new ArrayList<>();

        if(secondRoundKeys == null){

            String stopDate = "";
            try{
                stopDate = firstRoundKeys.getString("stopOutside");
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            Calendar todayDate = Calendar.getInstance();
            Calendar terminationDate = Calendar.getInstance();
            String[] stopDateSplit = stopDate.split(",");

            int currentYear = todayDate.get(Calendar.YEAR);
            int workingYear = currentYear;
            int validYear = currentYear;
            String validationYear = "";

            if(stopDateSplit.length == 3){
                if(stopDateSplit[0].equals("n")){
                    workingYear = workingYear + 1;
                }else if (stopDateSplit[0].equals("nn")){
                    workingYear = workingYear + 2;
                }else if (stopDateSplit[0].equals("nnn")){
                    workingYear = workingYear + 3;
                }else if (stopDateSplit[0].equals("nnnn")){
                    workingYear = workingYear + 4;
                }else if (stopDateSplit[0].equals("nnnnn")){
                    workingYear = workingYear + 5;
                }

                terminationDate.set(workingYear, Integer.valueOf(stopDateSplit[1]) - 1,Integer.valueOf(stopDateSplit[2]));
            }else {
                //workingYear will remain set to "currentYear"
                terminationDate.set(workingYear, Integer.valueOf(stopDateSplit[0]) - 1,Integer.valueOf(stopDateSplit[1]));
            }

            if (todayDate.compareTo(terminationDate) < 0 ){
                //if less than 0, means that today is earlier
                //this means that the cycle is still okay to proceed, and every instance of a working year is still valid in it's current state
            }else {
                validYear = validYear + 1;
            }

            Iterator<?> keys = firstRoundKeys.keys();

            while(keys.hasNext()){
                String key = (String)keys.next();
                if ((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))) {
                    String toDoKeyStr = "";
                    try {
                        toDoKeyStr = firstRoundKeys.getString(key);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    String correspondingEvent = new checkCovered(varietyTodoObj, key).getDescription();

                    String[] dateStrSplit = toDoKeyStr.split(",");
                    if(dateStrSplit.length == 3){
                        if(dateStrSplit[0].equals("n")){
                            validationYear = String.valueOf(validYear + 1);
                        }else if(dateStrSplit[0].equals("nn")){
                            validationYear = String.valueOf(validYear + 2);
                        }else if(dateStrSplit[0].equals("nnn")){
                            validationYear = String.valueOf(validYear + 3);
                        }else if(dateStrSplit[0].equals("nnnn")){
                            validationYear = String.valueOf(validYear + 4);
                        }else if(dateStrSplit[0].equals("nnnnn")){
                            validationYear = String.valueOf(validYear + 5);
                        }
                    }else{
                        validationYear = "";
                    }

                    String[] singleVarietyEvent = {varietyID, toDoKeyStr, validationYear, key, correspondingEvent};
                    genericVarietyEventList.add(singleVarietyEvent);
                }
            }
        }else{
            // run for double round

            /**the point of all of the following is the determine whether the cycle of events is valid for the following year
             * in which case, any instance of an event having an "n" value for the year, will have a corresponding year against which it
             * is validated.
             * */
            String stopDateRound1 = "";
            try{
                stopDateRound1 = firstRoundKeys.getString("stopOutside");
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            String stopDateRound2 = "";
            try{
                stopDateRound2 = secondRoundKeys.getString("stopOutside2");
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            Calendar todayDate = Calendar.getInstance();
            Calendar terminationDateRound1 = Calendar.getInstance();
            Calendar terminationDateRound2 = Calendar.getInstance();

            String[] stopDateRound1Split = stopDateRound1.split(",");
            String[] stopDateRound2Split = stopDateRound2.split(",");

            int currentYear = todayDate.get(Calendar.YEAR);

            int workingYearRound1 = currentYear;
            int validYearRound1 = currentYear;
            String validationYearRound1 = "";

            int workingYearRound2 = currentYear;
            int validYearRound2 = currentYear;
            String validationYearRound2 = "";

            //check that the current cycle (round1 and round2) is still valid, otherwise the validation year will have to be pushed one more year into the future.

            /***round1 **/
            if(stopDateRound1Split.length == 3){
                if(stopDateRound1Split[0].equals("n")){
                    workingYearRound1 = workingYearRound1 + 1;
                }else if (stopDateRound1Split[0].equals("nn")){
                    workingYearRound1 = workingYearRound1 + 2;
                }else if (stopDateRound1Split[0].equals("nnn")){
                    workingYearRound1 = workingYearRound1 + 3;
                }else if (stopDateRound1Split[0].equals("nnnn")){
                    workingYearRound1 = workingYearRound1 + 4;
                }else if (stopDateRound1Split[0].equals("nnnnn")){
                    workingYearRound1 = workingYearRound1 + 5;
                }

                terminationDateRound1.set(workingYearRound1, Integer.valueOf(stopDateRound1Split[1]) - 1,Integer.valueOf(stopDateRound1Split[2]));
            }else {
                //nada
                //workingYear will remain set to "currentYear"
                terminationDateRound1.set(workingYearRound1, Integer.valueOf(stopDateRound1Split[0]) - 1,Integer.valueOf(stopDateRound1Split[1]));
            }
            if (todayDate.compareTo(terminationDateRound1) < 0 ){
                //if less than 0, means that today is earlier
                //this means that the cycle is still okay to proceed, and every instance of a working year is still valid in it's current state
            }else {
                validYearRound1 = validYearRound1 + 1;
            }

            /**round2*/
            if(stopDateRound2Split.length == 3){
                if(stopDateRound2Split[0].equals("n")){
                    workingYearRound2 = workingYearRound2 + 1;
                }else if (stopDateRound2Split[0].equals("nn")){
                    workingYearRound2 = workingYearRound2 + 2;
                }else if (stopDateRound2Split[0].equals("nnn")){
                    workingYearRound2 = workingYearRound2 + 3;
                }else if (stopDateRound2Split[0].equals("nnnn")){
                    workingYearRound2 = workingYearRound2 + 4;
                }else if (stopDateRound2Split[0].equals("nnnnn")){
                    workingYearRound2 = workingYearRound2 + 5;
                }

                terminationDateRound2.set(workingYearRound2, Integer.valueOf(stopDateRound2Split[1]) - 1,Integer.valueOf(stopDateRound2Split[2]));
            }else {
                terminationDateRound2.set(workingYearRound2, Integer.valueOf(stopDateRound2Split[0]) - 1,Integer.valueOf(stopDateRound2Split[1]));
            }

            if (todayDate.compareTo(terminationDateRound2) < 0 ){
                //if less than 0, means that today is earlier
                //this means that the cycle is still okay to proceed, and every instance of a working year is still valid in it's current state
            }else {
                validYearRound2 = validYearRound2 + 1;
            }

            Iterator<?> firstKeys = firstRoundKeys.keys();
            Iterator<?> secondKeys = secondRoundKeys.keys();
            while(firstKeys.hasNext()){
                String key = (String)firstKeys.next();
                if ((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))) {
                    String toDoKeyStr = "";
                    try {
                        toDoKeyStr = firstRoundKeys.getString(key);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    String correspondingEvent = new checkCovered(varietyTodoObj, key).getDescription();

                    //Log.d(TAG, "key = " + key + ", todoKeyStr = " + toDoKeyStr + " correspondingEvent = " + correspondingEvent);

                    String[] dateStrSplit = toDoKeyStr.split(",");
                    if(dateStrSplit.length == 3){
                        if(dateStrSplit[0].equals("n")){
                            validationYearRound1 = String.valueOf(validYearRound1 + 1);
                        }else if(dateStrSplit[0].equals("nn")){
                            validationYearRound1 = String.valueOf(validYearRound1 + 2);
                        }else if(dateStrSplit[0].equals("nnn")){
                            validationYearRound1 = String.valueOf(validYearRound1 + 3);
                        }else if(dateStrSplit[0].equals("nnnn")){
                            validationYearRound1 = String.valueOf(validYearRound1 + 4);
                        }else if(dateStrSplit[0].equals("nnnnn")){
                            validationYearRound1 = String.valueOf(validYearRound1 + 5);
                        }
                    }else {
                        validationYearRound1 = "";
                    }

                    //String[] singleVarietyEvent = {varietyID, toDoKeyStr, validationYear, key, correspondingEvent};
                    String[] singleVarietyEvent = {varietyID,toDoKeyStr, validationYearRound1, key, correspondingEvent};
                    /**above should be (varietyID, a dateString (key), the eventTitle (toDoKeyString), eventDetail String)*/
                    genericVarietyEventList.add(singleVarietyEvent);
                }
            }

            while(secondKeys.hasNext()){
                String key = (String)secondKeys.next();
                if ((!key.equals("name")) && (!key.equals("id")) && (!key.equals("season")) && (!key.equals("notes"))) {
                    String toDoKeyStr = "";
                    try {
                        toDoKeyStr = secondRoundKeys.getString(key);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    String correspondingEvent = new checkCovered(varietyTodoObj, key).getDescription();

                    String[] dateStrSplit = toDoKeyStr.split(",");
                    if(dateStrSplit.length == 3){
                        if(dateStrSplit[0].equals("n")){
                            validationYearRound2 = String.valueOf(validYearRound2 + 1);
                        }else if(dateStrSplit[0].equals("nn")){
                            validationYearRound2 = String.valueOf(validYearRound2 + 2);
                        }else if(dateStrSplit[0].equals("nnn")){
                            validationYearRound2 = String.valueOf(validYearRound2 + 3);
                        }else if(dateStrSplit[0].equals("nnnn")){
                            validationYearRound2 = String.valueOf(validYearRound2 + 4);
                        }else if(dateStrSplit[0].equals("nnnnn")){
                            validationYearRound2 = String.valueOf(validYearRound2 + 5);
                        }
                    }else {
                        validationYearRound2 = "";
                    }

                    String[] singleVarietyEvent = {varietyID,toDoKeyStr, validationYearRound2, key, correspondingEvent};

                    genericVarietyEventList.add(singleVarietyEvent);
                }
            }
        }
    }

    public List<String[]> getGenericVarietyEventList(){
        return genericVarietyEventList;
    }
}
