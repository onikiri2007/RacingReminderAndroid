package com.bluechilli.racingreminders.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by monishi on 13/01/15.
 */
public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date getUTCDateTimeAsDate() {
        try
        {
            return stringDateToDate(getUTCDateTimeAsString(new Date(), DATE_FORMAT));
        }
        catch(ParseException e) {
            return null;
        }
    }


    public static String getUTCDateTimeAsString(Date date, String format) {

        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = formatter.format(date);
        return utcTime;
    }

    public static Date stringDateToDate(String date) throws ParseException {

       SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

       return format.parse(date);

    }
}
