package com.tylerjette.growmindv05;

public class readableMonth {

    private String monthRead;

    public readableMonth(String monthNum){
        String monthRead = "";
        switch(monthNum){
            case("1"):
                monthRead = "January";
                break;
            case("2"):
                monthRead = "February";
                break;
            case("3"):
                monthRead = "March";
                break;
            case("4"):
                monthRead = "April";
                break;
            case("5"):
                monthRead = "May";
                break;
            case("6"):
                monthRead = "June";
                break;
            case("7"):
                monthRead = "July";
                break;
            case("8"):
                monthRead = "August";
                break;
            case("9"):
                monthRead = "September";
                break;
            case("10"):
                monthRead = "October";
                break;
            case("11"):
                monthRead = "November";
                break;
            case("12"):
                monthRead = "December";
                break;
            default :
                monthRead = "";
        }
        this.monthRead = monthRead;
    }
    public String getMonthRead(){
        return this.monthRead;
    }
}
