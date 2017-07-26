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

import java.util.ArrayList;
import java.util.Calendar;

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
//        final ArrayList item = shiftDays[position];
//        if (item != null) {
//            return shiftDays[position].id;
//        }
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //get the amount of screen height remaining for the shift cell
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        int availableSpace = (MainActivity.deviceHeight - usedSpace);
        //amount of pixels taken up by a single textview
        int pixelPerTextview = (int) (10 * MainActivity.deviceDensity);
        //get the relative number of minutes (based on cell height being 24 hours) needed to display
        // a text view
        int minutesPerTextview = ShiftView.getMinutesBreakpoint();
        LinearLayout day_cell = new LinearLayout(parent.getContext());
        day_cell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //set the height of the cell to the remaining space
        day_cell.setMinimumHeight(availableSpace);
        day_cell.setPadding(10, 10, 10, 10);
        day_cell.setBackgroundResource(R.drawable.normal_background);
        day_cell.setClickable(false);
        day_cell.setOrientation(LinearLayout.VERTICAL);
        ArrayList<Shift> thisDaysShifts = shiftDays[position];
        int index = 0;
        long shiftEndMillis = 0; //declare outside loop so I can access the previous shift end time.
        String previousShiftEndFormatted = null; //declare outside loop so I can access the previous shift end time.

        if (thisDaysShifts.size() > 0) { //if there are some shifts for this day/cell
            Calendar shiftTimeHelper = Calendar.getInstance(MainActivity.locale);
            //get the millisecond time of midnight this morning (this calendar day of cell)
            shiftTimeHelper.setTimeInMillis(thisDaysShifts.get(0).getStartTime());
            shiftTimeHelper.set(Calendar.HOUR_OF_DAY, 0);
            shiftTimeHelper.set(Calendar.MINUTE, 0);
            long midnightThisMorning = shiftTimeHelper.getTimeInMillis();

            //for each shift for this day (grid column)
            for (int i = 0; i < thisDaysShifts.size(); i++) {
                long previousShiftEnd = 0;
                if (shiftEndMillis != 0) { //if this is not the first shift
                    //get the end time of the previous shift.
                    previousShiftEnd = shiftEndMillis;
                }
                boolean startTimeInsideRectangle = false; //set true if there is not enough space otherwise
                boolean endTimeInsideRectangle = false; //set true if there is not enough space otherwise

                // Get the times of the current shift

                //set the calendar helper object to the start time of the shift
                shiftTimeHelper.setTimeInMillis(thisDaysShifts.get(i).getStartTime());
                int startHour = shiftTimeHelper.get(Calendar.HOUR_OF_DAY);
                int startMinute = shiftTimeHelper.get(Calendar.MINUTE);
                String formattedStartHour = String.format("%02d", startHour);
                String formattedStartMinute = String.format("%02d", startMinute);
                String shiftStart = formattedStartHour + ":" + formattedStartMinute;
                //store the end time for special cases (for access in next loop iteration)
                shiftEndMillis = thisDaysShifts.get(i).getEndTime();
                //set the calendar helper object to the end time of the shift
                shiftTimeHelper.setTimeInMillis(shiftEndMillis);
                int endHour = shiftTimeHelper.get(Calendar.HOUR_OF_DAY);
                int endMinute = shiftTimeHelper.get(Calendar.MINUTE);
                String formattedEndHour = String.format("%02d", endHour);
                String formattedEndMinute = String.format("%02d", endMinute);
                String shiftEnd = formattedEndHour + ":" + formattedEndMinute;
                previousShiftEndFormatted = shiftEnd;
                //now we have the times of the shift, create any whitespace needed

                //if this is the first shift of the day, then create whitespace up until it starts
                if (previousShiftEnd == 0) {

                    ShiftView whitespaceShift = new ShiftView("00:00", shiftStart);
                    //if there is not enough space to display the start time
                    if ((thisDaysShifts.get(i).getStartTime() - midnightThisMorning) < minutesPerTextview) {
                        startTimeInsideRectangle = true;
                    }
                    day_cell.addView(whitespaceShift.getWhitespaceShiftView(parent, pixelPerTextview));
                }
                if (previousShiftEnd != 0) { //if this is not the first shift of the day
                    ShiftView whitespaceShift = new ShiftView(previousShiftEndFormatted, shiftStart);
                    if ((thisDaysShifts.get(i).getStartTime() - previousShiftEnd) < (minutesPerTextview*2)) {
                        startTimeInsideRectangle = true;
                    }
                    if (startTimeInsideRectangle) {
                        day_cell.addView(whitespaceShift.getWhitespaceShiftView(parent, (pixelPerTextview*2)));
                    }
                    else{
                        day_cell.addView(whitespaceShift.getWhitespaceShiftView(parent, (0)));
                                            }
                }
                //if there is another shift after this one
                if (thisDaysShifts.size() > (i + 1)){
                    long thisShiftEndTime = thisDaysShifts.get(i).getEndTime();
                    long nextShiftStartTime = thisDaysShifts.get(i+1).getStartTime();
                    //if there isnt enough room to display the textViews
                if ((nextShiftStartTime - thisShiftEndTime) < (minutesPerTextview*2)){
                    endTimeInsideRectangle = true;
                }
                }
                ShiftView currentShiftView = new ShiftView(shiftStart, shiftEnd);
                day_cell.addView(currentShiftView.getNormalShiftView(parent));
            }
        }
        view = day_cell;
        return view;
    }

    public final void refreshShifts() {
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
                displayShiftEnd = 0;
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
                if (displayShiftStart != 0 && displayShiftEnd != 0) {
                    shiftDays[j].add(new Shift(displayShiftStart, displayShiftEnd, theShifts[i].getId()));
                }
            }

            notifyDataSetChanged();
        }


    }
}
