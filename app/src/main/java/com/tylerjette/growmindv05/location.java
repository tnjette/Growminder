package com.tylerjette.growmindv05;

/**
 * Created by tylerjette on 11/8/17.
 */

public class location {
    private String zipCode;
    private String zone;
    private String placeName;

    public location (String zipCode, String zone, String placeName){
        this.zipCode = zipCode;
        this.zone = zone;
        this.placeName = placeName;
    }

    public String getZipCode(){
        return zipCode;
    }
    public String getZone(){
        return zone;
    }
    public String getPlaceName(){
        return placeName;
    }
}
