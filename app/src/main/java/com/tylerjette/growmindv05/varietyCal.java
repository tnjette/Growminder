package com.tylerjette.growmindv05;

/**
 * Created by tylerjette on 3/14/18.
 */
public class varietyCal {
    private String baselineID;
    private String varietyName;
    private int varietyImage;
    private String eventTitle;
    private String calendarEvent;
    private Boolean isFallCycle;

    //private List<VarietyStep> steps;

    public varietyCal(String baselineID, String varietyName, int varietyImage, String eventTitle, Boolean isFallCycle, String calendarEvent){ //this will probably need to be something else,
        // like a "next event date" or "upcoming event date"
        this.baselineID = baselineID;
        this.varietyName = varietyName;
        this.varietyImage = varietyImage;
        this.calendarEvent = calendarEvent;
        this.eventTitle = eventTitle;
        this.isFallCycle = isFallCycle;
    }

    public String getName(){
        return varietyName;
    }
    public String getbaselineID(){return baselineID;}
    public int getVarietyImage(){return varietyImage;}
    public String getCalendarEvent(){return calendarEvent;}
    public String getEventTitle(){return eventTitle;}
    public Boolean getIsFallCycle(){return isFallCycle;}

}
