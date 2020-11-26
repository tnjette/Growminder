package com.tylerjette.growmindv05;

import android.util.Log;

import java.util.List;

/**
 * Created by tylerjette on 11/16/17.
 */

public class variety {

    //private static final String TAG = variety.class.getSimpleName();
    private String name;
    private String botanicalName;
    private String description;
    private String zoneVarID;
    private int varietyImage;
    private Boolean state;

    //private List<VarietyStep> steps;

    public variety(String name, String zoneVarID, String botanicalName, int varietyImage, Boolean state){
        this.name = name;
        this.zoneVarID = zoneVarID;
        this.botanicalName = botanicalName;
        this.varietyImage = varietyImage;
        this.state = state;

        String testIMG = String.valueOf(varietyImage);

        //Log.d(TAG, "FROM VARIETY ( NAME : " + name + ", ZONEVARID : " + zoneVarID + ", BOTANICAL NAME : " + botanicalName +
           //     ", VARIETYIMAGE : " + varietyImage + ", VALUEOF_VAR_IMAGE : " + testIMG);

    }

    public String getName(){
        return name;
    }
    public String getBotanicalName(){return botanicalName;}
    //public String getDescription(){
    //    return description;
    //}
    public String getZoneVarID(){
        return zoneVarID;
    }
    public int getVarietyImage(){return varietyImage;}
    //public Boolean getState(){
    //    return state;
    //}
    //public void setState(Boolean state){
    //    this.state = state;
    //}
}
