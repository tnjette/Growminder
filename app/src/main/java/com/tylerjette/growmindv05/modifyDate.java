package com.tylerjette.growmindv05;

public class modifyDate {
    private String finalDate;
    public modifyDate(String yearStr, int year, String relevantDate){
        StringBuilder eventDateBuild = new StringBuilder();
        //String relevantDate = varietyObj.getString(key);
        //String eventNote = ""; //this will be a special note for the user in the event that a window is about to close.

        //Log.d(TAG, "relevant date that is getting modified: " + relevantDate);
        String[] eventDateSplit = relevantDate.split(",");

        if(eventDateSplit.length == 2){ //meaning there is no year, implying the date is within the current year
            String monthStr = eventDateSplit[0];
            String dayStr = eventDateSplit[1];
            if (eventDateSplit[0].length() == 1) { //changed from [1] //meaning the date is in the format of 4,5
                monthStr = "0" + eventDateSplit[0];
                //Log.d(TAG, "month build in the modify date method, should be '04', for example. = " +monthStr);
            } else {
                //do nothing, month str is fine
            }
            if (eventDateSplit[1].length() == 1) {
                dayStr = "0" + eventDateSplit[1]; //changed from [2]
                //Log.d(TAG, "day build in the modify date method, should be '04' for example = " + dayStr);
            } else {
                //do nothing, day str is fine
            }
            eventDateBuild.append(yearStr);
            eventDateBuild.append(",");
            eventDateBuild.append(monthStr);
            eventDateBuild.append(",");
            eventDateBuild.append(dayStr);

        } else if(eventDateSplit.length == 3){
            //first check the month and day
            if (eventDateSplit[1].length() == 1) { //changed from [1] //meaning the date is in the format of 4,5
                String modifyMonth = "0" + eventDateSplit[1];
                eventDateSplit[1]= modifyMonth;
                //Log.d(TAG, "month build in the modify date method, should be '04', for example. = " +eventDateSplit[1]);
            } else {
                //do nothing, month str is fine
            }
            if (eventDateSplit[2].length() == 1) {
                String modifyDay = "0" + eventDateSplit[2];
                eventDateSplit[2] = modifyDay;
                //Log.d(TAG, "day build in the modify date method, should be '04' for example = " + eventDateSplit[2]);
            } else {
                //do nothing, day str is fine
            }

            if (eventDateSplit[0].equals("n")) {
                int yearAdjustOne = year + 1;
                String thisYear = String.valueOf(yearAdjustOne);
                eventDateBuild.append(thisYear);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[1]);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[2]); //should now be "2019,XX,XX"
            } else if (eventDateSplit[0].equals("nn")) {
                int yearAdjustOne = year + 2;
                String thisYear = String.valueOf(yearAdjustOne);
                eventDateBuild.append(thisYear);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[1]);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[2]); //should now be "2020,XX,XX"
            } else if (eventDateSplit[0].equals("nnn")) {
                int yearAdjustOne = year + 3;
                String thisYear = String.valueOf(yearAdjustOne);
                eventDateBuild.append(thisYear);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[1]);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[2]); //should now be "2021,XX,XX"
            } else if (eventDateSplit[0].equals("nnnn")) {
                int yearAdjustOne = year + 4;
                String thisYear = String.valueOf(yearAdjustOne);
                eventDateBuild.append(thisYear);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[1]);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[2]); //should now be "2022,XX,XX"
            } else if (eventDateSplit[0].equals("nnnnn")) {
                int yearAdjustOne = year + 2;
                String thisYear = String.valueOf(yearAdjustOne);
                eventDateBuild.append(thisYear);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[1]);
                eventDateBuild.append(",");
                eventDateBuild.append(eventDateSplit[2]); //should now be "2023,XX,XX"
            }
        }

        //Log.d(TAG, "first, from the modifydate method, here is the final eventDateBuild string : " + eventDateBuild.toString());
        this.finalDate = eventDateBuild.toString();
    }//end of modify date method

    public String getModifiedDate(){ return finalDate; }
}
