package com.tylerjette.growmindv05;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class buildDescription {

    private String localeSpec;

    public buildDescription(String varietyName, JSONObject obj, JSONObject round1Obj, JSONObject round2Obj, JSONObject varietyTodos){

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

            String endingMonthRead = new readableMonth(endingMonthName).getMonthRead();

            /**then we put it together in a readable format*/
            /***round1 sentence*/
            if(obj.has("startInside")){


                String test = "";
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

                startingMonthRead = new readableMonth(startingMonth).getMonthRead();

                String length = new cycleLength(startingMonth, endingMonth).getLengthStr();

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
                startingMonthRead = new readableMonth(startingMonth).getMonthRead();

                String length = new cycleLength(startingMonth, endingMonth).getLengthStr();

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

                    endingMonthR1 = String.valueOf(endVal);

                } else {
                    endingMonthR1 = endingMonthR1split[0];
                    r1EndMonth = endingMonthR1;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            String endingMonthR1Read = new readableMonth(r1EndMonth).getMonthRead();

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

            String endingMonthR2Read = new readableMonth(r2EndMonth).getMonthRead();

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

                startingMonthR1Read = new readableMonth(startingMonthR1).getMonthRead();

                String length = new cycleLength(startingMonthR1, endingMonthR1).getLengthStr();

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
                startingMonthR1Read = new readableMonth(startingMonthR1).getMonthRead();

                String length = new cycleLength(startingMonthR1, endingMonthR1).getLengthStr();

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

                startingMonthR2Read = new readableMonth(startingMonthR2).getMonthRead();

                String length = new cycleLength(startingMonthR2, endingMonthR2).getLengthStr();

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
                startingMonthR2Read = new readableMonth(startingMonthR2).getMonthRead();

                String length = new cycleLength(startingMonthR2, endingMonthR2).getLengthStr();

                localeSpec = localeSpec + "while the second is a " + length + " - month cycle that begins in " + startingMonthR2Read +
                        " and is ready to harvest in " + endingMonthR2Read + ". ";
            }
        }

        /**then retreive the variety-specific details from the todos list*/
        String sun = "";
        String soil = "";
        String water = "";
        String fertilize = "";
        String mulch = "";
        String spacing = "";

        try{
            sun = varietyTodos.getString("sun");
            soil = varietyTodos.getString("soil");
            water = varietyTodos.getString("water");
            fertilize = varietyTodos.getString("fertilize");
            mulch = varietyTodos.getString("mulch");
            spacing = varietyTodos.getString("spacing");
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        localeSpec = localeSpec + varietyName + " requires " + sun + soil + water + fertilize + mulch + spacing + ".";

        this.localeSpec = localeSpec;
    }

    //getter
    public String getLocaleSpec(){
        return this.localeSpec;
    }
}
