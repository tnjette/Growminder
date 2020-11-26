package com.tylerjette.growmindv05;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by tylerjette on 4/8/18.
 */

public class learnMoreCalendarEvent implements Serializable{
    //private static final String TAG = learnMoreCalendarEvent.class.getSimpleName();

    private String eventDate;
    private String eventTitle;
    private String eventDescription_expand;

    public  learnMoreCalendarEvent(String eventDate, String eventTitle, String eventDescription_expand){
        this.eventDate = eventDate;
        this.eventTitle = eventTitle;
        this.eventDescription_expand = eventDescription_expand;
    }

    public String getEventDate(){
        String readableDate = new getReadableDate(eventDate).getFinalDateStr();
        return readableDate;
    }
    public String getEventTitle(){
        //Log.d(TAG, " FROM THE LEARN MORE CALENDAR EVENT, this is the event title " + eventTitle);
        return eventTitle;
    }
    public String getEventDescription_expand(){
        return eventDescription_expand;
    }
}
