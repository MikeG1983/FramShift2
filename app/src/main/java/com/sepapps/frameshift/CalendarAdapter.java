package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/17/17.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    final Calendar calendar;
    private final Locale locale;
    private final CalendarDay today;
    //    private final Shift selected;
    private final LayoutInflater inflater;
    private CalendarDay[] calendarDays;
    private final SimpleDateFormat dayFormat;
//    private Shift[] shifts;

    public CalendarAdapter(Context context, Calendar weekCalendar) {
        calendar = weekCalendar;
        long theTime = calendar.getTimeInMillis();
//        locale = Locale.getDefault();
        locale = Locale.UK;
        dayFormat = new SimpleDateFormat("E", Locale.UK);
        // get the variables to construct the today object
        Calendar todayCalendar = Calendar.getInstance();
        int date = todayCalendar.get(Calendar.DAY_OF_MONTH);
        String theDayName = dayFormat.format(todayCalendar.getTime());
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        todayCalendar.add(Calendar.HOUR, CalendarView.gmtOffset);
        //create the today object
        today = new CalendarDay(date, theDayName, todayCalendar.getTimeInMillis());
        //set the calendar to the first day of the week
         theTime = calendar.getTimeInMillis();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return calendarDays[position];
    }

    @Override
    public long getItemId(int position) {
        final CalendarDay item = calendarDays[position];
        if (item != null) {
            return calendarDays[position].id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.day_item, null);
        }
        final TextView dayView = (TextView) view.findViewById(R.id.date);
        final CalendarDay currentDay = calendarDays[position];

        if (currentDay == null) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
            view.setBackgroundDrawable(null);
            dayView.setText(null);
        } else {
            if (currentDay.equals(today)) {
                view.setBackgroundResource(R.drawable.today_background);
            } else {
                view.setBackgroundResource(R.drawable.normal_background);
            }
            dayView.setText(currentDay.text);
        }

        return view;
    }

    public final void refreshDays() {

        final CalendarDay[] days;
        Calendar calCopy = (Calendar) (calendar.clone());
                // Set the day to the first day of the week
        calCopy.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calCopy.getTime();
        days = new CalendarDay[7];
        for (int i = 0; i < 7; i++) {
            int theDate = calCopy.get(Calendar.DAY_OF_MONTH);
            String theDayName = dayFormat.format(calCopy.getTime());
            calCopy.set(Calendar.HOUR_OF_DAY, 0);
            calCopy.set(Calendar.MINUTE, 0);
            calCopy.set(Calendar.SECOND, 0);
            calCopy.set(Calendar.MILLISECOND, 0);
            calCopy.add(Calendar.HOUR, CalendarView.gmtOffset);
            days[i] = new CalendarDay(theDate, theDayName, calCopy.getTimeInMillis());
            calCopy.add(Calendar.DATE, 1);
        }
        this.calendarDays = days;

        notifyDataSetChanged();
    }

    private static class CalendarDay {
        public int date;
        public String day;
        public String text;
        public Long id;
        public long theDayLong;


        public CalendarDay(int date, String day, long daylong) {
            this.date = date;
            this.day = day;
            this.text = day + System.getProperty("line.separator") + date;
            this.id = CalendarView.createID();
            this.theDayLong = daylong;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof CalendarDay) {
                final CalendarDay item = (CalendarDay) o;
                Boolean rtn = item.theDayLong == theDayLong;
                return rtn;
            }
            return false;
        }


    }
}
