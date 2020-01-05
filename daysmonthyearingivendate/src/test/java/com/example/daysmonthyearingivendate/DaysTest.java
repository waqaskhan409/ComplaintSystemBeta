package com.example.daysmonthyearingivendate;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DaysTest {

    @Test
    public void days() throws ParseException {
        Days days = new Days();
        String datePre = "2019-12-24";

        Date dateEarly = new SimpleDateFormat("yyyy-MM-dd").parse(datePre);
        Date dateLate = new SimpleDateFormat("yyyy-MM-dd").parse(datePre);

        days.getDays(dateEarly);

        assertEquals("Today", days.getDays(dateEarly, dateLate));
    }
}
