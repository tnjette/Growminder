package com.tylerjette.growmindv05;

public class cycleLength {
    private String lengthStr;
    public cycleLength(String startMonth, String endMonth){

        String lengthStr = "";
        int startVal = Integer.valueOf(startMonth);
        int endVal = Integer.valueOf(endMonth);

        int lengthVal = endVal - startVal;
        lengthStr = String.valueOf(lengthVal);

        this.lengthStr = lengthStr;
    }
    public String getLengthStr(){
        return this.lengthStr;
    }
}
