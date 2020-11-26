package com.tylerjette.growmindv05;

import org.json.JSONException;
import org.json.JSONObject;

public class checkCovered {

    private String correspondingEventDescription;

    public checkCovered(JSONObject varietyTodos, String key){
        //String correspondingEventDescription = "";
        try {
            if (key.equals("startOutsideCovered")) {
                String begin = varietyTodos.getJSONObject("startOutside").getString("begin");
                String mainBody = varietyTodos.getJSONObject("startOutside").getString("covered");
                String remainingBoth = varietyTodos.getJSONObject("startOutside").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("startOutside")) {
                String begin = varietyTodos.getJSONObject("startOutside").getString("begin");
                String mainBody = varietyTodos.getJSONObject("startOutside").getString("uncovered");
                String remainingBoth = varietyTodos.getJSONObject("startOutside").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("directOutsideCovered")) {
                String begin = varietyTodos.getJSONObject("directOutside").getString("begin");
                String mainBody = varietyTodos.getJSONObject("directOutside").getString("covered");
                String remainingBoth = varietyTodos.getJSONObject("directOutside").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("directOutside")) {
                String begin = varietyTodos.getJSONObject("directOutside").getString("begin");
                String mainBody = varietyTodos.getJSONObject("directOutside").getString("uncovered");
                String remainingBoth = varietyTodos.getJSONObject("directOutside").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("startOutside2Covered")) {
                String begin = varietyTodos.getJSONObject("startOutside2").getString("begin");
                String mainBody = varietyTodos.getJSONObject("startOutside2").getString("covered");
                String remainingBoth = varietyTodos.getJSONObject("startOutside2").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("startOutside2")) {
                String begin = varietyTodos.getJSONObject("startOutside2").getString("begin");
                String mainBody = varietyTodos.getJSONObject("startOutside2").getString("uncovered");
                String remainingBoth = varietyTodos.getJSONObject("startOutside2").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("directOutside2Covered")) {
                String begin = varietyTodos.getJSONObject("directOutside2").getString("begin");
                String mainBody = varietyTodos.getJSONObject("directOutside2").getString("covered");
                String remainingBoth = varietyTodos.getJSONObject("directOutside2").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            } else if (key.equals("directOutside2")) {
                String begin = varietyTodos.getJSONObject("directOutside2").getString("begin");
                String mainBody = varietyTodos.getJSONObject("directOutside2").getString("uncovered");
                String remainingBoth = varietyTodos.getJSONObject("directOutside2").getString("remainingBoth");
                this.correspondingEventDescription = begin + mainBody + remainingBoth;
            }else {
                this.correspondingEventDescription = varietyTodos.getString(key);
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public String getDescription(){
        return correspondingEventDescription;
    }
}
