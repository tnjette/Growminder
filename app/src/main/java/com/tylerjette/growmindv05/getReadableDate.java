package com.tylerjette.growmindv05;

import android.util.Log;

import java.util.Calendar;

public class getReadableDate {

    //public static String TAG = getReadableDate.class.getSimpleName();

    private String finalDateStr;
    private String dateStr;
    private String[] dateSplit;
    private String monthStr;
    private String yearStr;

    public getReadableDate(String dateStr) {

        this.dateStr = dateStr;
        this.dateSplit = dateStr.split(",");

        //Log.d(TAG, "THIS IS THE DATE OBJECT : " + dateStr);

        String monthTemp = "";
        String yearTemp = "";
        if(dateSplit.length == 2){
            monthTemp = dateSplit[0];
        }else if(dateSplit.length == 3){
            monthTemp = dateSplit[1];
            yearTemp = dateSplit[0];
        }

        switch(monthTemp){
            case "01":
                this.monthStr = "January";
                break;
            case "02" :
                this.monthStr = "February";
                break;
            case "03" :
                this.monthStr = "March";
                break;
            case "04" :
                this.monthStr = "April";
                break;
            case "05" :
                this.monthStr = "May";
                break;
            case "06" :
                this.monthStr ="June";
                break;
            case "07" :
                this.monthStr = "July";
                break;
            case "08" :
                this.monthStr = "August";
                break;
            case "09" :
                this.monthStr = "September";
                break;
            case "1":
                this.monthStr = "January";
                break;
            case "2" :
                this.monthStr = "February";
                break;
            case "3" :
                this.monthStr = "March";
                break;
            case "4" :
                this.monthStr = "April";
                break;
            case "5" :
                this.monthStr = "May";
                break;
            case "6" :
                this.monthStr ="June";
                break;
            case "7" :
                this.monthStr = "July";
                break;
            case "8" :
                this.monthStr = "August";
                break;
            case "9" :
                this.monthStr = "September";
                break;
            case "10" :
                this.monthStr = "October";
                break;
            case "11" :
                this.monthStr = "November";
                break;
            case "12" :
                this.monthStr = "December";
                break;
            default : this.monthStr = "";
                break;
        }

        if(!yearTemp.equals("")){
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            //config the year string
            switch(yearTemp){
                case "n" :
                    yearStr = String.valueOf(currentYear + 1);
                    break;
                case "nn":
                    yearStr = String.valueOf(currentYear + 2);
                    break;
                case "nnn" :
                    yearStr = String.valueOf(currentYear + 3);
                    break;
                case "nnnn" :
                    yearStr = String.valueOf(currentYear + 4);
                    break;
                case "nnnnn" :
                    yearStr = String.valueOf(currentYear + 5);
                    break;
                default : yearStr = yearTemp;
            }
        }

        if(dateSplit.length == 3){
            if((Calendar.getInstance().get(Calendar.YEAR)) == Integer.valueOf(yearStr)){
                this.finalDateStr = monthStr + " " + dateSplit[2];
            }else {
                this.finalDateStr = monthStr + " " + dateSplit[2] + ", " + yearStr;
            }
        }else if (dateSplit.length == 2){
            this.finalDateStr = monthStr + " " + dateSplit[1];
        }
    }
    public String getFinalDateStr(){return finalDateStr;}
}
