package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/17/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShiftAdapter extends BaseAdapter {

    //    private final Shift selected;
    private final LayoutInflater inflater;
    //    private final SimpleDateFormat dayFormat;
    private final Calendar calendar;
    private Shift[] shifts;
    private ArrayList[] shiftDays;

    public ShiftAdapter(Context context, Calendar weekCalendar) {
        calendar = weekCalendar;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return shiftDays[position];
    }

    @Override
    public long getItemId(int position) {
        final ArrayList item = shiftDays[position];
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
        final LinearLayout day_cell = (LinearLayout) view.findViewById(R.id.day_cell);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        startTime.setText("yeehaa!");
        day_cell.addView(startTime);

        return view;
    }

    public final void refreshDays() {
        Calendar calCopy = (Calendar) (calendar.clone());
        // Set the day to the first day of the week
        //set the millisecond times for the start of week, and end of each day
        calCopy.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calCopy.set(Calendar.HOUR_OF_DAY, 0);
        calCopy.set(Calendar.MINUTE, 0);
        //create an array to hold the start of every day plus one extra
        long[] dayStarts = new long[8];
        dayStarts[0] = calCopy.getTimeInMillis();
        for (int i = 1; i < dayStarts.length; i++) {
            calCopy.add(Calendar.DATE, 1);
            dayStarts[i] = calCopy.getTimeInMillis();
        }
        //reset the calendar copy
        calCopy = (Calendar) (calendar.clone());
        //pretend to get the shifts from the database
        //there were 4 shifts retrieved, so set the size to 4
        Shift[] theShifts = new Shift[4];
        //set the dummy times
        calCopy.set(Calendar.HOUR_OF_DAY, 14);
        calCopy.set(Calendar.MINUTE, 0);
        long shiftStart = calCopy.getTimeInMillis();
        calCopy.set(Calendar.HOUR_OF_DAY, 18);
        long shiftEnd = calCopy.getTimeInMillis();
        //create 4 dummy shifts
        for (int i = 0; i < theShifts.length; i++) {
            theShifts[i] = new Shift(shiftStart, shiftEnd);
            calCopy.add(Calendar.DATE, 1);
            calCopy.set(Calendar.HOUR_OF_DAY, 14);
            calCopy.set(Calendar.MINUTE, 0);
            shiftStart = calCopy.getTimeInMillis();
            calCopy.set(Calendar.HOUR_OF_DAY, 18);
            shiftEnd = calCopy.getTimeInMillis();
        }
        this.shifts = theShifts;
        //end of getting shifts from database

        //create an array to hold one ArrayList for each day
        //that will hold the shifts for that day
        shiftDays = new ArrayList[7];
        //create the ArrayLists for each day inside the array
        for (int i = 0; i < shiftDays.length; i++) {
            shiftDays[i] = new ArrayList<Shift>();
        }
        //now assign the shifts to the correct day
        for (int i = 0; i < theShifts.length; i++) {
            long displayShiftStart;
            long displayShiftEnd;
            //Get the start day and end day of the shift, if they
            //are the same then add the shift to one day, if they
            //are different then add the shift to each day, for display purposes.
            // for each day of the week
            for (int j = 0; j < (dayStarts.length - 1); j++) {
                displayShiftStart = 0;
                shiftEnd = 0;
                //if the shift started before the start of this day, but finishes after it
                if ((theShifts[i].getStartTime() < dayStarts[j]) && (theShifts[i].getEndTime() > dayStarts[j])) {
                    displayShiftStart = dayStarts[j];
                }
                //if the start time is between the start and the end of the day
                if ((theShifts[i].getStartTime() > dayStarts[j]) && (theShifts[i].getStartTime() < dayStarts[j + 1])) {
                    displayShiftStart = theShifts[i].getStartTime();
                }
                //if the shift end time is between the start and end of the day
                if ((theShifts[i].getEndTime() > dayStarts[j]) && (theShifts[i].getEndTime() < dayStarts[j + 1])) {
                    displayShiftEnd = theShifts[i].getEndTime();
                }
                //if the shift end time is after the end of the day, but started before it
                if ((theShifts[i].getEndTime() > dayStarts[j + 1]) && (theShifts[i].getStartTime() < dayStarts[j])) {
                    displayShiftEnd = (dayStarts[j + 1] - 1);
                }
                // if the shift is for this day, then add it to the array
                if (shiftStart != 0 && shiftEnd != 0) {
                    shiftDays[j].add(new Shift(shiftStart, shiftEnd, theShifts[i].getId()));
                }
            }

            notifyDataSetChanged();
        }


    }
}
