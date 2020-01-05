package com.example.daysmonthyearingivendate;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class HoursTest {
    @Test
    public void hours() throws ParseException {
        Hours days = new Hours();
        String datePre = "2019-12-24";
        String datePost = "2019-12-23";

        Date dateEarly = new SimpleDateFormat("yyyy-MM-dd").parse(datePre);
        Date dateLate = new SimpleDateFormat("yyyy-MM-dd").parse(datePost);

//        days.getDays(dateEarly);

        assertEquals("Today", days.getHours(dateEarly));
    }
}
