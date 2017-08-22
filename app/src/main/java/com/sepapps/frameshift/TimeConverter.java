package com.sepapps.frameshift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ragebunny on 8/13/17.
 */

public class TimeConverter {

    public static String getTimeFromLong(long time) {
        Calendar cal = Calendar.getInstance(MainActivity.locale);
        cal.setTimeInMillis(time);
        int theHour = cal.get(Calendar.HOUR_OF_DAY);
        int theMinute = cal.get(Calendar.MINUTE);
        String formattedStartHour = String.format("%02d", theHour);
        String formattedStartMinute = String.format("%02d", theMinute);
        String theTime = formattedStartHour + ":" + formattedStartMinute;
        return theTime;
    }

    public static String getDateFromLong(long time) {
        Calendar cal = Calendar.getInstance(MainActivity.locale);
        cal.setTimeInMillis(time);
        String date = "" + cal.get(Calendar.DAY_OF_MONTH) + " "
                + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, MainActivity.locale) + " " + cal.get(Calendar.YEAR);
        return date;
    }

    /**
     * The date will be in the format Tue, 22 August 2017
     *
     * @param date
     * @param time
     * @return
     */
    public static long getLongFromDateAndTime(String date, String time) {
        String dateAndTime = date + " " + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMMM yyyy HH:mm", Locale.UK);
        try {
            Date convertedDate = dateFormat.parse(dateAndTime);
            Calendar cal = Calendar.getInstance(MainActivity.locale);
            cal.setTime(convertedDate);
            long result = cal.getTimeInMillis();
            return result;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
