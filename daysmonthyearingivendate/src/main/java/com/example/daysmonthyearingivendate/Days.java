package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Days {
    private Date presentDate;


    public Days() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }

    public String getDays(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long days = getDaysInNumber(startDate);
        if(days == 0){
            return String.valueOf("Today");
        }else if(days == 1){
            return String.valueOf("Yesterday");
        }else {
            return String.valueOf(days) + " Days ago";
        }
    }
    public String getDays(Date startDate, Date presentDate) {
        long days = getDaysInNumber(startDate, presentDate);
        if(days == 0){
            return "Today";
        }else if(days == 1){
            return "Yesterday";
        }else {
            return String.valueOf(days);
        }
    }
    public long getDaysInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long days = (endInMillis - startInMillis ) / (24 * 60 * 60 * 1000);
        return days;
    }
    public long getDaysInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long days = (endInMillis - startInMillis ) / (24 * 60 * 60 * 1000);
        return days;
    }


}
