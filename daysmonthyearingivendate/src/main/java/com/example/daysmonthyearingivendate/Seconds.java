package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Seconds {
    private Date presentDate;


    public Seconds() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }

    public String getSeconds(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long seconds = getSecondsInNumber(startDate);

        return String.valueOf(seconds) + " seconds ago";

    }
    public String getSeconds(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long seconds = getSecondsInNumber(startDate, presentDate);

        return String.valueOf(seconds) + " seconds ago";

    }
    public long getSecondsInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = (endInMillis - startInMillis ) / (60 * 1000);
        return minutes;
    }
    public long getSecondsInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = (endInMillis - startInMillis ) / (60 * 1000 );
        return minutes;
    }
}
