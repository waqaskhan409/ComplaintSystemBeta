package com.example.daysmonthyearingivendate;

import android.util.Log;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class YearTest {
    private static final String TAG = "YearTest";
    @Test
    public void year() throws ParseException {
        Year year = new Year();
        String datePre = "2018-12-25";
        String datePost = "2019-12-23";

        Date dateEarly = new SimpleDateFormat("yyyy-MM-dd").parse(datePre);
        Date dateLate = new SimpleDateFormat("yyyy-MM-dd").parse(datePost);

//        year.getYears(dateEarly);
//        Log.d(TAG, "year: " + datePre);
//        Log.d(TAG, "year: " + datePost);

        assertEquals("1 year ago", year.getYears(dateEarly, dateLate));
    }
}
