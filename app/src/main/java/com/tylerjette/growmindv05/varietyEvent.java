package com.tylerjette.growmindv05;

/**
 * Created by tylerjette on 3/10/18.
 */

public class varietyEvent {
    private String varietyName;
    private String varietyEvent;
    private int varietyImage;

    public varietyEvent(String varietyName, String varietyEvent, int varietyImage){
        this.varietyName = varietyName;
        this.varietyEvent = varietyEvent;
        this.varietyImage = varietyImage;
    }

    public String getVarietyName(){
        return varietyName;
    }
    public String getVarietyEvent(){
        return varietyEvent;
    }
    public int getVarietyEventImage(){
        return varietyImage;
    }
}
