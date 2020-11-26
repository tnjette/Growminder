package com.tylerjette.growmindv05;

import android.util.Log;

public class checkMonth {
    private Boolean answer;
    private static String TAG = checkMonth.class.getSimpleName();
    public checkMonth(int currentMonth, String nextCycle){

        Log.d(TAG, "THIS IS THE CURRENTMONTH INT : " + currentMonth + " and THIS IS THE NEXT CYCLE : " + nextCycle);
        if((currentMonth == 0 && nextCycle.equals("January")) || ((currentMonth == 1 && nextCycle.equals("February")))
                || ((currentMonth == 2 && nextCycle.equals("March"))) || ((currentMonth == 3 && nextCycle.equals("April")))
                || ((currentMonth == 4 && nextCycle.equals("May"))) || ((currentMonth == 5 && nextCycle.equals("June"))) || ((currentMonth == 6 && nextCycle.equals("July")))
                || ((currentMonth == 7 && nextCycle.equals("August"))) || ((currentMonth == 8 && nextCycle.equals("September"))) || ((currentMonth == 9 && nextCycle.equals("October")))
                || ((currentMonth == 10 && nextCycle.equals("November"))) || ((currentMonth == 11 && nextCycle.equals("December")))){
            this.answer = true;
        } else {
            this.answer = false;
        }
    }
    public boolean getAnswer(){
        return this.answer;
    }
}
