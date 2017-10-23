package com.encore.piano.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kamran on 10/14/2017.
 */

public class DateTimeUtility {

    static final SimpleDateFormat formatDateTimeToShow = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
    static final SimpleDateFormat formatTimeToShow = new SimpleDateFormat("hh:mm a");
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final SimpleDateFormat formatToSend = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Date toDateTime(String dateTime) {

        Date date = null;
        if (dateTime != null) {
            try {
                date = format.parse(dateTime);
            } catch (ParseException e) {
                date = null;
            }
        }
        return date;
    }

    public static String formatDateTime(Date dateTime) {
        return format.format(dateTime);
    }
    public static String formatDateTimeToShow(Date dateTime) {
        return formatDateTimeToShow.format(dateTime);
    }

    public static String formatTimeToShow(Date dateTime) {
        return formatTimeToShow.format(dateTime);
    }

    public static String formatTimeStampToSend(Date dateTime) {
        return formatToSend.format(dateTime);
    }

    public static String getCurrentTimeStamp()
    {
        return format.format(new Date());
    }

    public static String getCurrentTimeStampLongString()
    {
        return String.valueOf(getCurrentTimeStampLong());
    }

    public static long getCurrentTimeStampLong()
    {
        return new Date().getTime();
    }

    public static long getMilis24Hours()
    {
        return getCurrentTimeStampLong() + 86400000; // now in milli + 24h in miliseconds.
    }

    public static long GetMilisOneMinute()
    {
        return getCurrentTimeStampLong() + 60000; // now in milli + 1m in miliseconds
    }
}
