package com.tylerjette.growmindv05;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**this class will ultimately have to do the JSON reading of the Botanical Names JSON file in order to return the desired variety Name*/
public class getBotanicalName {

    private String finalBotanicalNameStr;
    private String baselineID;
    private JSONObject botanicalNamesFile;
    Context context;

    public getBotanicalName(String baselineID, Context context) {

        this.baselineID = baselineID;
        this.context = context;

        /**read the file, get the name, return it to the calling class*/
        try{
            botanicalNamesFile = new JSONObject(loadBotanicalNames().trim());
            this.finalBotanicalNameStr = botanicalNamesFile.getJSONObject("botanicalNames").getString(baselineID.toLowerCase());
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public String getFinalName(){return finalBotanicalNameStr;}

    public String loadBotanicalNames(){
        String json = null;
        try{
            InputStream botanicalNamesFile = context.getAssets().open("botanicalNames.json");
            int size = botanicalNamesFile.available();
            byte[] buffer = new byte[size];
            botanicalNamesFile.read(buffer);
            botanicalNamesFile.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
