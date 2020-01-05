package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Months {
    private Date presentDate;

    public Months() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }
    public String getMonths(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long months = getMonthsInNumber(startDate);
        if(months == 0){
            return "This month";
        }
        return String.valueOf(months) + " Month ago";
    }
    public String getMonths(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long months = getMonthsInNumber(startDate, presentDate);
        if(months == 0){
            return "This month";
        }
        return String.valueOf(months) + " Month ago";
    }
    public long getMonthsInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long months = (startInMillis - endInMillis ) / (30 * 24 * 60 * 60 * 1000);
        return months;
    }
    public long getMonthsInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long months = (startInMillis - endInMillis ) / (30 * 24 * 60 * 60 * 1000);
        return months;
    }



}
