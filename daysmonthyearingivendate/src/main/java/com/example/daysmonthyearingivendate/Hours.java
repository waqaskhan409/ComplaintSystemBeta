package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Hours {

    private Date presentDate;


    public Hours() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }

    public String getHours(Date startDate) {
        long hours = getHoursInNumber(startDate);
        return String.valueOf(hours) + "Hours ago";
    }
    public String getHours(Date startDate, Date presentDate) {
        long hours = getHoursInNumber(startDate, presentDate);
        return String.valueOf(hours) + "Hours ago";

    }


    public long getHoursInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long hours = (endInMillis - startInMillis ) / (60 * 60 * 1000);
        return hours;
    }
    public long getHoursInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long hours = (endInMillis - startInMillis ) / (60 * 60 * 1000);
        return hours;
    }

}
