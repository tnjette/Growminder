package com.tylerjette.growmindv05;

public class varietyCalendarEvent {
    private String eventTitle;
    private String eventDetail;
    private String eventDate;

    public varietyCalendarEvent(String eventTitle, String eventDetail, String eventDate){
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.eventDate = eventDate;
    }

    public String getEventTitle(){
        return eventTitle;
    }
    public String getEventDetail(){
        return eventDetail;
    }
    public String getEventDate(){
        return eventDate;
    }
}
