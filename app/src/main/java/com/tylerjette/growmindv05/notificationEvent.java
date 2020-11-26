package com.tylerjette.growmindv05;

/**
 * Created by tylerjette on 2/28/18.
 */

public class notificationEvent {
    private String baselineID;
    private String varName;
    private String varietyEvent;
    private String eventTitle;
    private int varietyImage;
    private Boolean isFallCycle;

    public notificationEvent(String baselineZoneVarID, String varName, String eventTitle, String varietyEvent, int varietyImage, Boolean isFallCycle){
        this.baselineID = baselineZoneVarID;
        this.varName = varName;
        this.eventTitle = eventTitle;
        this.varietyEvent = varietyEvent;
        this.varietyImage = varietyImage;
        this.isFallCycle = isFallCycle;
    }

    public String getBaselineID(){return baselineID;}
    public String getName(){return varName;}
    public String getVarietyEvent(){return varietyEvent;}
    public String getEventTitle(){return eventTitle;}
    public int getVarietyImage(){return varietyImage;}
    public Boolean getIsFallCycle(){return isFallCycle;}
}
