package com.tylerjette.growmindv05;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class getReadableName {
    Context context;
    private String baselineID;
    private String finalName;
    private JSONObject readableNamesFile;

    public getReadableName(String baselineID,Context context){
        this.context = context;
        this.baselineID = baselineID;

        try{
            readableNamesFile = getReadableNameFile();
            this.finalName = readableNamesFile.getJSONObject("readableNames").getString(this.baselineID.toLowerCase());

        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public String getName(){
        return finalName;
    }

    public JSONObject getReadableNameFile(){
        String json = null;
        JSONObject readableNamesfile = new JSONObject();
        try{
            InputStream botanicalNamesFile = context.getAssets().open("readableNamesByVariety.json");
            int size = botanicalNamesFile.available();
            byte[] buffer = new byte[size];
            botanicalNamesFile.read(buffer);
            botanicalNamesFile.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        try{
            readableNamesfile = new JSONObject(json);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return readableNamesfile;
    }
}
