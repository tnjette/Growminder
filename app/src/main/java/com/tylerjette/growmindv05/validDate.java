package com.tylerjette.growmindv05;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class validDate {

    private Boolean validate;

    public validDate(String dateStr){ //this is in the form of yyyy,mm,dd

        this.validate = false;

        Calendar nowCheck = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        String pattern = "yyyy,MM,dd"; //date template
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //Boolean validate = false;
        /***here there needs to be a conditional method of determining whether today's date is the same date as the date in the database */

        //nowCheck.add(Calendar.DATE, 1);
        String formattedNow = sdf.format(nowCheck.getTime()); //this should now be the current date in the yyyy,mm,dd format

        //now you can compare the formatted now with the dateStr. if they match, then the validate boolean is false
        if (formattedNow.equals(dateStr)){
            validate = false;
            //Log.d(TAG, "DATES MATCH" );
        }else{
            //Log.d(TAG, "DATES DO NOT MATCH >>>>COMPARING DATE WITH CURRENT DATE : formatted now = " + formattedNow + ", dateStr to be compared = "+ dateStr);
            try {
                Date dateToCompare = sdf.parse(dateStr);
                Calendar calObjToCompare = Calendar.getInstance();
                calObjToCompare.setTime(dateToCompare);
                if (calObjToCompare.compareTo(now) < 0){
                    //date is bad, has passed
                    //goodDate boolean remains false;
                } else if(calObjToCompare.compareTo(now) > 0){
                    //date is still relevant, has not passed
                    validate = true;
                } else if (calObjToCompare.compareTo(now) == 0){
                    //date is the same
                    validate = false;
                }
            }catch(ParseException ex){
                ex.printStackTrace();
                //Log.d(TAG, "sorry, couldn't parse a date String from the database"); //this can act as a test of the quality of the json data
            }
        }

    }

    public Boolean getValidDate(){
        return validate;
    }


}
