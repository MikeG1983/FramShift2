package com.sepapps.frameshift;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EnterShift extends Activity
        implements TimePickerFragment.TimePickerDialogListener, DatePickerFragment.DatePickerDialogListener {

    private SimpleDateFormat dayOfWeekFormat;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String currentDayOfWeek;
    private String currentDate;
    private String currentTime;
    private Date currentlySetFromDate;
    private Date currentlySetToDate;
    private static final int START_PICKER_ID = 1;
    private static final int END_PICKER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        setContentView(R.layout.activity_enter_shift);
        //get the current date into the variables
        dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.UK);
        dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.UK);
        currentDayOfWeek = dayOfWeekFormat.format(c.getTime());
        currentDate = dateFormat.format(c.getTime());
        //set the current date as the initial values for the two date fields
        TextView startDate = (TextView) findViewById(R.id.startDate);
        startDate.setText(currentDayOfWeek + ", " + currentDate);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        endDate.setText(currentDayOfWeek + ", " + currentDate);
        //get the current time into the variables
        timeFormat = new SimpleDateFormat("HH:mm", Locale.UK);
        currentTime = timeFormat.format(c.getTime());
        //set the current time as the initial values for the two time fields
        TextView startTime = (TextView) findViewById(R.id.startTime);
        startTime.setText(currentTime);
        TextView toTime = (TextView) findViewById(R.id.endTime);
        toTime.setText(currentTime);

    }

    public void setStartTime(View v) {
        TextView startTime = (TextView) findViewById(R.id.startTime);
        String theStartTime = (String) startTime.getText();
        String[] parts = theStartTime.split(":");
        Integer theStartHour = Integer.valueOf(parts[0]);
        Integer theStartMinute = Integer.valueOf(parts[1]);

        // pass the ID for the TimePicker
        DialogFragment newFragment = TimePickerFragment.newInstance(START_PICKER_ID, theStartHour, theStartMinute);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void setEndTime(View v) {
        TextView endTime = (TextView) findViewById(R.id.endTime);
        String theEndTime = (String) endTime.getText();
        String[] parts = theEndTime.split(":");
        Integer theEndHour = Integer.valueOf(parts[0]);
        Integer theEndMinute = Integer.valueOf(parts[1]);

        // Pass the ID for the TimePicker
        DialogFragment newFragment = TimePickerFragment.newInstance(END_PICKER_ID, theEndHour, theEndMinute);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        Log.i("TimePicker", "Time picker set from id " + id + "!");
        // if the timepicker was from the fromtime text view
        if (id == START_PICKER_ID) {
            TextView startTime = (TextView) findViewById(R.id.startTime);
            startTime.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
        }
        if (id == END_PICKER_ID) {
            TextView endTime = (TextView) findViewById(R.id.endTime);
            endTime.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
        }


    }

    public void setStartDate(View v) {
        Calendar cal = Calendar.getInstance();
        TextView startDate = (TextView) findViewById(R.id.startDate);
        String theStartDate = (String) startDate.getText();
        String[] cutOffDay = theStartDate.split(",");
        String[] parts = cutOffDay[1].split(" ");
        Integer theStartDay = Integer.valueOf(parts[1]);
        String theStartMonthString = parts[2];
        try {
            cal.setTime(new SimpleDateFormat("MMM").parse(theStartMonthString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer theStartMonth = cal.get(Calendar.MONTH);
        Integer theStartYear = Integer.valueOf(parts[3]);

        // pass the ID for the DatePicker
        DialogFragment newFragment = DatePickerFragment.newInstance(START_PICKER_ID, theStartYear, theStartMonth, theStartDay);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void setEndDate(View v) {
        Calendar cal = Calendar.getInstance();
        TextView endDate = (TextView) findViewById(R.id.endDate);
        String theEndDate = (String) endDate.getText();
        String[] cutOffDay = theEndDate.split(",");
        String[] parts = cutOffDay[1].split(" ");
        Integer theEndDay = Integer.valueOf(parts[1]);
        String theEndMonthString = parts[2];
        try {
            cal.setTime(new SimpleDateFormat("MMM").parse(theEndMonthString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer theEndMonth = cal.get(Calendar.MONTH);
        Integer theEndYear = Integer.valueOf(parts[3]);

        //pass the ID for the DatePicker
        DialogFragment newFragment = DatePickerFragment.newInstance(END_PICKER_ID, theEndYear, theEndMonth, theEndDay);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Override
    public void onDateSet(int id, DatePicker view, int year, int month, int day) {
        Log.i("TimePicker", "Time picker set from id " + id + "!");
        // if the timepicker was from the from date text view
        if (id == START_PICKER_ID) {
            TextView startDate = (TextView) findViewById(R.id.startDate);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            currentDayOfWeek = dayOfWeekFormat.format(cal.getTime());
            currentDate = dateFormat.format(cal.getTime());
            startDate.setText(currentDayOfWeek + ", " + currentDate);
        }
        if (id == END_PICKER_ID) {
            TextView endDate = (TextView) findViewById(R.id.endDate);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            currentDayOfWeek = dayOfWeekFormat.format(cal.getTime());
            currentDate = dateFormat.format(cal.getTime());
            endDate.setText(currentDayOfWeek + ", " + currentDate);
        }


    }

}
