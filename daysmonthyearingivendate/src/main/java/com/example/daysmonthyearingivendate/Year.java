package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Year {
    private Date presentDate;


    public Year() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }

    public String getYears(Date startDate) {
        long years = getYearsInNumber(startDate);
        if(years == 0){
            return "This year";
        }else if(years == 1){
            return "1 year ago";
        }else {
            return String.valueOf(years) + " years ago" ;
        }
    }
    public String getYears(Date startDate, Date presentDate) {
        long years = getYearsInNumber(startDate, presentDate);
        if(years == 0){
            return "This year";
        }else if(years == 1){
            return "1 year ago";
        }else {
            return String.valueOf(years) + " years ago" ;
        }
    }
    public long getYearsInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long hours = ( endInMillis - startInMillis) / (12 * 30 * 24 * 60 * 60 * 1000);
        return hours;
    }
    public long getYearsInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long hours = ( endInMillis - startInMillis) / (12 * 30 * 24 * 60 * 60 * 1000);
        return hours;
    }

}
