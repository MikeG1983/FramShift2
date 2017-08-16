package com.sepapps.frameshift;

import java.util.Calendar;

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
}
