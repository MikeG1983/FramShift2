package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/17/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShiftAdapter extends BaseAdapter {

    private final Shift selected;
    private final LayoutInflater inflater;

    private final SimpleDateFormat dayFormat;

    private Shift[] shifts;

    public ShiftAdapter(Context context, Shift[] theShifts) {
        this.shifts = theShifts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shifts.length;
    }

    @Override
    public Object getItem(int position) {
        return shifts[position];
    }

    @Override
    public long getItemId(int position) {
        final Shift item = shifts[position];
        if (item != null) {
            return shifts[position].id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.shift_item, null);
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

        final int year = calendar.get(Calendar.YEAR);
        final int week = calendar.get(Calendar.WEEK_OF_YEAR);
        final CalendarDay[] days;
        Calendar calCopy = (Calendar) (calendar.clone());
        // Set the day to the first day of the week
        calCopy.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        days = new CalendarDay[7];
        for (int i = 0; i < 7; i++) {
            int theDate = calCopy.get(Calendar.DAY_OF_MONTH);
            String theDayName = dayFormat.format(calCopy.getTime());
            days[i] = new CalendarDay(theDate, theDayName);
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


        public CalendarDay(int date, String day) {
            this.date = date;
            this.day = day;
            this.text = day + System.getProperty("line.separator") + date;
            this.id = CalendarView.createID();

        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof CalendarDay) {
                final CalendarDay item = (CalendarDay) o;
                Boolean rtn = item.date == date && item.day.equals(day);
                return rtn;
            }
            return false;
        }


    }
}
