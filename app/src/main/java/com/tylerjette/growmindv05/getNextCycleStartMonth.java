package com.tylerjette.growmindv05;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class getNextCycleStartMonth {

    private String nextCycleStartMonth;
    public getNextCycleStartMonth(Calendar immediateDate, JSONObject varietyObj){

        int year = immediateDate.get(Calendar.YEAR);

        if (varietyObj.has("round2")){
            String start1Str = "";
            String start2Str = "";
            JSONObject round1;
            JSONObject round2;
            String month1 = "";
            String month2 = "";
            String day1 = "";
            String day2 = "";

            String finalMonth = "";
            try {
                round1 = varietyObj.getJSONObject("round1");
                round2 = varietyObj.getJSONObject("round2");
                if(round1.has("startInside")){
                    try{
                        start1Str = round1.getString("startInside");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else if(round1.has("directOutside")){
                    try{
                        start1Str = round1.getString("directOutside");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else if(round1.has("directOutsideCovered")){
                    try{
                        start1Str = round1.getString("directOutsideCovered");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }

                if(round2.has("startInside2")){
                    try{
                        start2Str = round2.getString("startInside2");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else if(round2.has("directOutside2")){
                    try{
                        start2Str = round2.getString("directOutside2");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else if(round2.has("directOutside2Covered")){
                    try{
                        start2Str = round2.getString("directOutside2Covered");
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }

                String[] start1Split = start1Str.split(",");
                String[] start2Split = start2Str.split(",");

                if(start1Split.length == 2){
                    //meaning there is only mm/dd format for the date
                    month1 = start1Split[0];
                    day1 = start1Split[1];
                } else if (start1Split.length == 3){
                    month1 = start1Split[1];
                    day1 = start1Split[2];
                }

                if(start2Split.length == 2){
                    month2 = start2Split[0];
                    day2 = start2Split[1];
                }else if (start2Split.length == 3){
                    month2 = start2Split[1];
                    day2 = start2Split[2];
                }

                Calendar calRound1 = Calendar.getInstance();
                Calendar calRound2 = Calendar.getInstance();

                calRound1.set(year, Integer.valueOf(month1) - 1, Integer.valueOf(day1));
                calRound2.set(year, Integer.valueOf(month2) - 1, Integer.valueOf(day2));

                if(((calRound1.compareTo(immediateDate) > 0) && (calRound2.compareTo(immediateDate) > 0)) ||
                        ((calRound1.compareTo(immediateDate) == 0) && (calRound2.compareTo(immediateDate) > 0)) ||
                        ((calRound1.compareTo(immediateDate) == 0) && (calRound2.compareTo(immediateDate) == 0))
                        ){

                    finalMonth = month1;

                }
                 else if (((calRound1.compareTo(immediateDate) < 0) && (calRound2.compareTo(immediateDate) > 0))||
                        (calRound1.compareTo(immediateDate) < 0) && (calRound2.compareTo(immediateDate) == 0)
                        ){
                    finalMonth = month2;
                } else if((calRound1.compareTo(immediateDate) < 0) && (calRound2.compareTo(immediateDate) < 0)){
                    finalMonth = month1;
                }else {
                    finalMonth = month2;
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            switch(finalMonth){
                case "1" :
                    this.nextCycleStartMonth = "January";
                    break;
                case "2" :
                    this.nextCycleStartMonth = "February";
                    break;
                case "3" :
                    this.nextCycleStartMonth = "March";
                    break;
                case "4" :
                    this.nextCycleStartMonth = "April";
                    break;
                case "5" :
                    this.nextCycleStartMonth = "May";
                    break;
                case "6" :
                    this.nextCycleStartMonth = "June";
                    break;
                case "7" :
                    this.nextCycleStartMonth = "July";
                    break;
                case "8" :
                    this.nextCycleStartMonth = "August";
                    break;
                case "9" :
                    this.nextCycleStartMonth = "September";
                    break;
                case "10" :
                    this.nextCycleStartMonth = "October";
                    break;
                case "11" :
                    this.nextCycleStartMonth = "November";
                    break;
                case "12" :
                    this.nextCycleStartMonth = "December";
                    break;
            }
        }else {

            String startStr = "";
            if(varietyObj.has("startInside")){
                try{
                    startStr = varietyObj.getString("startInside");
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else if(varietyObj.has("directOutside")){
                try{
                    startStr = varietyObj.getString("directOutside");
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else if(varietyObj.has("directOutsideCovered")){
                try{
                    startStr = varietyObj.getString("directOutsideCovered");
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
            String[] startSplit = startStr.split(",");
            String month = "";
            if(startSplit.length == 2){
                //meaning there is only mm/dd format for the date
                month = startSplit[0];
            } else if (startSplit.length == 3){
                month = startSplit[1];
            }

            switch(month){
                case "1" :
                    this.nextCycleStartMonth = "January";
                    break;
                case "2" :
                    this.nextCycleStartMonth = "February";
                    break;
                case "3" :
                    this.nextCycleStartMonth = "March";
                    break;
                case "4" :
                    this.nextCycleStartMonth = "April";
                    break;
                case "5" :
                    this.nextCycleStartMonth = "May";
                    break;
                case "6" :
                    this.nextCycleStartMonth = "June";
                    break;
                case "7" :
                    this.nextCycleStartMonth = "July";
                    break;
                case "8" :
                    this.nextCycleStartMonth = "August";
                    break;
                case "9" :
                    this.nextCycleStartMonth = "September";
                    break;
                case "10" :
                    this.nextCycleStartMonth = "October";
                    break;
                case "11" :
                    this.nextCycleStartMonth = "November";
                    break;
                case "12" :
                    this.nextCycleStartMonth = "December";
                    break;
            }
        }
    }
    public String getMonthStr(){return nextCycleStartMonth;}
}
