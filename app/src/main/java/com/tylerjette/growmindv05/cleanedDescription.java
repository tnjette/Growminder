package com.tylerjette.growmindv05;

import android.util.Log;

public class cleanedDescription {
    private static String TAG = cleanedDescription.class.getSimpleName();
    private String roughStr = "";
    private String cleanedStr = "";

    public cleanedDescription(String roughStr) {
        Log.d(TAG, "When cleaned Description is called, here is the rough Str : " + roughStr);
        //do stuff
        //String output = descSplit[i].substring(0, 1).toUpperCase() + descSplit[i].substring(1);
        String workingStr = "";
        this.roughStr = roughStr;
        String[] roughSplit = this.roughStr.split(" ");
        if(roughSplit[0].equals(",")){
            if(roughSplit[1].equals("and")){
                String firstWord = roughSplit[2].substring(0,1).toUpperCase() + roughSplit[2].substring(1);
                workingStr = firstWord + " ";
                for(int i = 3; i < roughSplit.length; i++){
                    workingStr = workingStr + roughSplit[i] + " ";
                }
            }else {
                /**char[] wordSplit = descSplit[i].toCharArray();
                 if(Character.isDigit(wordSplit[0])){
                 //is a digit
                 }else if (Character.isLetter(wordSplit[0])){
                 //is a letter

                 }*/
                char[] firstCharArr = roughSplit[1].toCharArray();
                if(Character.isLetter(firstCharArr[0])){
                    //
                    String firstWord = roughSplit[1].substring(0,1).toUpperCase() + roughSplit[1].substring(1);
                    workingStr = firstWord + " ";
                    for(int i = 2; i < roughSplit.length; i++){
                        workingStr = workingStr + roughSplit[i] + " ";
                    }
                }else {
                    for(int i = 1; i < roughSplit.length; i++){
                        workingStr = workingStr + roughSplit[i] + " ";
                    }
                }
            }
        } else{
            if(roughSplit[0].equals("and")){
                String firstWord = roughSplit[1].substring(0,1).toUpperCase() + roughSplit[1].substring(1);
                workingStr = firstWord + " ";
                for(int i = 2; i < roughSplit.length; i++){
                    workingStr = workingStr + roughSplit[i] + " ";
                }
            }else {
                /**char[] wordSplit = descSplit[i].toCharArray();
                 if(Character.isDigit(wordSplit[0])){
                 //is a digit
                 }else if (Character.isLetter(wordSplit[0])){
                 //is a letter

                 }*/
                char[] firstCharArr;
                Boolean first = false;
                if(roughSplit[0].equals("") || roughSplit[0].equals(" ")){
                    firstCharArr = roughSplit[1].toCharArray();
                }else{
                    firstCharArr = roughSplit[0].toCharArray();
                    first = true;
                }

                //Log.d(TAG, "rough split [0] : " + roughSplit[0]);
                //Log.d(TAG, "rough split [1] : " + roughSplit[1]);
                //Log.d(TAG, "RoughSplit : " + String.valueOf(roughSplit));
                //Log.d(TAG, "FIRST CHAR ARR Length: " + firstCharArr.length);
                if(Character.isLetter(firstCharArr[0])){
                    //
                    String firstWord = "";
                    if(first){
                        firstWord = roughSplit[0].substring(0,1).toUpperCase() + roughSplit[0].substring(1);
                    }else{
                        firstWord = roughSplit[1].substring(0,1).toUpperCase() + roughSplit[1].substring(1);

                    }

                    workingStr = firstWord + " ";
                    for(int i = 1; i < roughSplit.length; i++){
                        workingStr = workingStr + roughSplit[i] + " ";
                    }
                }else if (Character.isDigit(firstCharArr[0])){
                    for(int i = 0; i < roughSplit.length; i++){
                        workingStr = workingStr + roughSplit[i] + " ";
                    }
                }
            }
        }
        Log.d(TAG, "THIS IS THE CLEANED STRING : " +  workingStr);
        this.cleanedStr= workingStr;
    }

    public String getCleanedStr(){
        return this.cleanedStr;
    }
}
