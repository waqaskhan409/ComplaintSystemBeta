package com.example.daysmonthyearingivendate;

import java.util.Date;

public class Minutes {
    private Date presentDate;


    public Minutes() {
        Date currentDate = new Date();
        this.presentDate = currentDate;
    }

    public String getMinutes(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = getMinutesInNumber(startDate);

        return String.valueOf(minutes) + " minutes ago";

    }
    public String getMinutes(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = getMinutesInNumber(startDate, presentDate);

        return String.valueOf(minutes) + " minutes ago";

    }
    public long getMinutesInNumber(Date startDate, Date presentDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = (endInMillis - startInMillis ) / (60 * 1000);
        return minutes;
    }
    public long getMinutesInNumber(Date startDate) {
        long startInMillis = startDate.getTime();
        long endInMillis = presentDate.getTime();
        long minutes = (endInMillis - startInMillis ) / (60 * 1000 );
        return minutes;
    }
}
