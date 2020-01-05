package com.example.daysmonthyearingivendate;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MonthTest {


    @Test
    public void months() throws ParseException {
        Months months = new Months();
        String datePre = "2019-10-26";
        String datePost = "2020-1-24";
        Date dateEarly = new SimpleDateFormat("yyyy-MM-dd").parse(datePre);
        Date dateLate = new SimpleDateFormat("yyyy-MM-dd").parse(datePost);
        assertEquals("4 Month ago", months.getMonths(dateEarly, dateLate));
    }
}
