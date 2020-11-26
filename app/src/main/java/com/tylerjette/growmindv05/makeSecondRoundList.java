package com.tylerjette.growmindv05;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class makeSecondRoundList {

    private List<String[]> listOfVarietyEvents = new ArrayList<>();
    //private static String TAG = makeSecondRoundList.class.getSimpleName();

    public makeSecondRoundList (JSONObject readableNamesFile, JSONObject toDoJSONObj, JSONObject originalVarietyObject, JSONObject varietyObj, String varID, String yearStr, int year, int startInside,
                                               int startHardenOff, int startOutside, int directOutside, int stopOutside){

        //List<String[]> listOfVarietyEvents = new ArrayList<>();
        Iterator<?> keys = varietyObj.keys();
        String regex = "\\d+.*";
        String baselineID = varID.replaceAll(regex, "");
        //String readableVarietyName = getReadableName(baselineID);
        String readableVarietyName = "";
        try{
            readableVarietyName = readableNamesFile.getJSONObject("readableNames").getString(baselineID);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        JSONObject varietyTodos;

        /**this method will only be called when there are dates that have passed.*/
        if(((startInside == -1) && (startHardenOff == 1) && (startOutside == 1) && (directOutside == 0) &&
                (stopOutside == 1)) ||
                ((startInside == -1) && (startHardenOff == 0) && (startOutside == 1) && (directOutside == 0) && (stopOutside == 1)))
        {
            Calendar immediateDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
            String formatted = sdf.format(immediateDate.getTime());
            String immediateNotificationBody = "The window for starting your fall " + readableVarietyName + " seeds indoors has already begun. If there " +
                    "is not enough time to start your fall " + readableVarietyName + " seedlings indoors, it is advisable to simply purchase " + readableVarietyName + " seedlings from " +
                    "your local Nursery in order to not miss the window to begin transplanting them into your garden, which is approaching.";

            String immediateNotificationTitle = "startInside2";
            String[] immediateNotice = {varID,/* varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody}; /**changed to include eventTitle*/

            listOfVarietyEvents.add(immediateNotice);
        } else if((startInside == -1) && (startHardenOff == -1) && (startOutside == 1) && (directOutside == 0) && (stopOutside  == 1)){
            /**triggered by expired startInside and startHardenOff in single round*, thus activated during the startHardenOff window*/

            //then add content for immediate notification relating to the fact that the window for the startInside is closing
            Calendar immediateDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
            String formatted = sdf.format(immediateDate.getTime());
            String immediateNotificationBody = "The window for starting your fall " + readableVarietyName + " seeds indoors has already closed, and the window to begin hardening your " +
                    readableVarietyName + " fall seedlings in your garden has already begun. It is therefore advisable to simply purchase " + readableVarietyName+ " seedlings from " +
                    "your local Nursery, if you don't have them growing already, in order to not miss the window to begin hardening them off in your garden, which will close soon." +
                    " To harden them off in your garden, place the seedlings outside during daylight hours, and bring them back inside at night. This will prepare them " +
                    " for permanent transplanting outside which will be happening soon.";

            String immediateNotificationTitle = "startHardenOff2";

            String[] immediateNotice = {varID, /*varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody};
            listOfVarietyEvents.add(immediateNotice);
            //}catch(JSONException ex){
            //    ex.printStackTrace();
            //}
        }else if(((startInside == -1) && (startHardenOff == -1) && (startOutside == -1) && (directOutside == 0) && (stopOutside == 1)) ||
                ((startInside == -1) && (startHardenOff == 0) && (startOutside == -1) && (directOutside == 0) && (stopOutside == 1)))
        {
            /**triggered by expired startInside, startHardenOff, and startOutside, thus activated within the startOutside window, doesn't include directOutside because the
             * immediate notifications are relevant to starting indoors and transplanting.
             * */
            try{
                varietyTodos = toDoJSONObj.getJSONObject(baselineID);
                String begin = "";
                String mainBody = "";
                String remainingBoth = "";
                String correspondingEventDescription = "";

                if (varietyObj.has("startOutside2")){
                    begin = varietyTodos.getJSONObject("startOutside2").getString("begin");
                    mainBody = varietyTodos.getJSONObject("startOutside2").getString("uncovered");
                    remainingBoth = varietyTodos.getJSONObject("startOutside2").getString("remainingBoth");
                    correspondingEventDescription = begin + mainBody + remainingBoth;
                } else if (varietyObj.has("startOutside2Covered")){
                    begin = varietyTodos.getJSONObject("startOutside2").getString("begin");
                    mainBody = varietyTodos.getJSONObject("startOutside2").getString("covered");
                    remainingBoth = varietyTodos.getJSONObject("startOutside2").getString("remainingBoth");
                    correspondingEventDescription = begin + mainBody + remainingBoth;
                }
                Calendar immediateDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                String formatted = sdf.format(immediateDate.getTime());
                String immediateNotificationBody = "The windows for starting your fall " + readableVarietyName + " seeds indoors and for hardening them off in your garden have closed." +
                        " It is therefore advisable to simply purchase " + readableVarietyName + " seedlings from your local nursery and transplant them into your garden " +
                        "as soon as possible, before the window to do so closes. Transplant as follows. " + correspondingEventDescription;

                String immediateNotificationTitle = "startOutside2";
                String[] immediateNotice = {varID, /*varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody}; /**includes eventtitle*/
                listOfVarietyEvents.add(immediateNotice);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }else if((startInside == 0) && (startHardenOff == 0) && (startOutside == 0) && (directOutside == -1) && (stopOutside == 1)){
            /**triggered by expired directOutside, startHardenOff, and startOutside, thus activated within the startOutside window, doesn't include directOutside because the
             * immediate notifications are relevant to starting indoors and transplanting.
             * */
            try{
                varietyTodos = toDoJSONObj.getJSONObject(baselineID);
                String begin = "";
                String mainBody = "";
                String remainingBoth = "";
                String correspondingEventDescription = "";

                if (varietyObj.has("directOutside2")){
                    begin = varietyTodos.getJSONObject("directOutside2").getString("begin");
                    mainBody = varietyTodos.getJSONObject("directOutside2").getString("uncovered");
                    remainingBoth = varietyTodos.getJSONObject("directOutside2").getString("remainingBoth");
                    correspondingEventDescription = begin + mainBody + remainingBoth;
                } else if (varietyObj.has("directOutside2Covered")){
                    begin = varietyTodos.getJSONObject("directOutside2").getString("begin");
                    mainBody = varietyTodos.getJSONObject("directOutside2").getString("covered");
                    remainingBoth = varietyTodos.getJSONObject("directOutside2").getString("remainingBoth");
                    correspondingEventDescription = begin + mainBody + remainingBoth;
                }
                Calendar immediateDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                String formatted = sdf.format(immediateDate.getTime());

                String immediateNotificationBody = "The window for planting your fall " + readableVarietyName + " seeds directly in your garden has already begun." +
                        " It is therefore advisable to plant as soon as possible so as to not miss the window in your zone. Plant your " + readableVarietyName +
                        " as follows. " + correspondingEventDescription;

                String immediateNotificationTitle = "directOutside2";

                String[] immediateNotice = {varID, /*varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody};
                listOfVarietyEvents.add(immediateNotice);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }else if(((startInside == -1) && (startHardenOff == -1) && (startOutside == -1) && (directOutside == 0) && (stopOutside == -1)) ||
                ((startInside == -1) && (startHardenOff == 0) && (startOutside == -1) && (directOutside == 0) && (stopOutside == -1)) ||
                ((startInside == 0) && (startHardenOff == 0) && (startOutside == 0) && (directOutside == -1) && (stopOutside == -1))
                ){
            Calendar immediateDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
            String formatted = sdf.format(immediateDate.getTime());

            /**TODO: retrieve the starting date for the next seasonal cycle, which in this case is going to be the next spring cycle*/
            //String nextCycle = getNextCycleStartMonth(immediateDate, originalVarietyObject); REFACTORED
            String nextCycle = new getNextCycleStartMonth(immediateDate, originalVarietyObject).getMonthStr();

            String immediateNotificationBody = "Unfortunately the optimal window for planting fall " + readableVarietyName + " in your zone has already passed. " +
                    "You will have to wait until the next seasonal cycle, which starts in " + nextCycle + ".";

            String immediateNotificationTitle = "EXPIRED";

            String[] immediateNotice = {varID, /*varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody}; /**includes eventTitle*/
            listOfVarietyEvents.add(immediateNotice);
        }
        else if(((startInside == 1) && (startHardenOff == 1) && (startOutside == 1) && (directOutside == 0) && (stopOutside == 1))
                || ((startInside == 1) && (startHardenOff == 0) && (startOutside == 1) && (directOutside == 0) && (stopOutside == 1))
                || ((startInside == 0) && (startHardenOff == 0) && (startOutside == 0) && (directOutside == 1) && (stopOutside == 1))){

            /***this needs to be reintroduced in order to accommodate situations in which every event for a particular cycle is in the future
             * this was originally foregone because it seemed illogical to have notifications when there was really nothing to notify the user about,
             * but it turns out the system involved in the dashboard activity needs a least some list of events to hold onto in order to avoid crashing,
             * and realistically, it is actually logical to give the user a notification to not get their panties in a bunch and wait a little longer.
             *
             * */
            Calendar immediateDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
            String formatted = sdf.format(immediateDate.getTime());

            //String nextCycle = getNextCycleStartMonth(immediateDate, originalVarietyObject);REFACTORED
            String nextCycle = new getNextCycleStartMonth(immediateDate,originalVarietyObject).getMonthStr();

            int currentMonth = immediateDate.get(Calendar.MONTH);
            Boolean isSameMonth = new checkMonth(currentMonth, nextCycle).getAnswer();

            /**check if it's the same month as is currently.
             * */
            String immediateNotificationBody = "";

            if(isSameMonth){
                immediateNotificationBody = "The planting cycle for fall "+readableVarietyName+" has not yet begun in your zone. It will begin later this month." ;
            }else {
                immediateNotificationBody = "The planting cycle for fall " + readableVarietyName + " has not yet begun in your zone. It will begin in " + nextCycle + ".";
            }
            String immediateNotificationTitle = "HAS NOT BEGUN"; //todo keep in mind this will have to be accommodated later on in the system when the title is
            //reviewed by the

            String[] immediateNotice = {varID, /*varTrueName,*/ formatted, immediateNotificationTitle, immediateNotificationBody}; /**includes eventTitle*/
            this.listOfVarietyEvents.add(immediateNotice);

        }

        //Log.d(TAG, "This is the makeSecondRoundList : " + listOfVarietyEvents.size());

    } //end of make List of variety events method

    public List<String[]> getSecondRoundListOfEvents(){
        return listOfVarietyEvents;
    }
}
