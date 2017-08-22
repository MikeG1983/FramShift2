package com.sepapps.frameshift;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EnterShift extends Activity
        implements TimePickerFragment.TimePickerDialogListener, DatePickerFragment.DatePickerDialogListener {

    private SimpleDateFormat dayOfWeekFormat;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String currentDayOfWeek;
    private String currentDate;
    private String edit;
    private String currentTime;
    private long currentlySetFromDate;
    private long currentlySetToDate;
    private long shiftID;
    private static final int START_PICKER_ID = 1;
    private static final int END_PICKER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Long defaultLong = 0L;
        currentlySetFromDate = intent.getLongExtra("startTime", defaultLong);
        currentlySetToDate = intent.getLongExtra("endTime", defaultLong);
        shiftID = intent.getLongExtra("id", defaultLong);

        edit = intent.getStringExtra("edit");
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        if (edit.equals("yes")) {
            setContentView(R.layout.activity_edit_shift);
        } else {
            setContentView(R.layout.activity_enter_shift);
        }
        //set the format for the text fields
        dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.UK);
        dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.UK);
        timeFormat = new SimpleDateFormat("HH:mm", Locale.UK);
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView toTime = (TextView) findViewById(R.id.endTime);
        final Button addEditButton = (Button) findViewById(R.id.saveButton);
        if (edit.equals("yes")) {
            addEditButton.setText("Edit");
            addEditButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editShift(v);
                }
            });
            Button deleteButton = (Button) findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteShift(v);
                }
            });
        } else {
            addEditButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveShift(v);
                }
            });
        }
        //get the shift start time into the fields
        c.setTimeInMillis(currentlySetFromDate);
        currentDayOfWeek = dayOfWeekFormat.format(c.getTime());
        currentDate = dateFormat.format(c.getTime());
        startDate.setText(currentDayOfWeek + ", " + currentDate);
        currentTime = timeFormat.format(c.getTime());
        startTime.setText(currentTime);
        c.setTimeInMillis(currentlySetToDate);
        currentDayOfWeek = dayOfWeekFormat.format(c.getTime());
        currentDate = dateFormat.format(c.getTime());
        endDate.setText(currentDayOfWeek + ", " + currentDate);
        currentTime = timeFormat.format(c.getTime());
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
//        String stringHourOfDay = Integer.toString(hourOfDay);
//        String stringMinute =  Integer.toString(minute);

        // if the timepicker was from the fromtime text view
        if (id == START_PICKER_ID) {
            TextView startTime = (TextView) findViewById(R.id.startTime);
            startTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
        }
        if (id == END_PICKER_ID) {
            TextView endTime = (TextView) findViewById(R.id.endTime);
            endTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
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

    public void loadCalendar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentWeek", currentlySetFromDate);
        startActivity(intent);
    }


    public void saveShift(View view) {
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView toTime = (TextView) findViewById(R.id.endTime);
        long shiftStartLong = TimeConverter.getLongFromDateAndTime("" + startDate.getText(), "" + startTime.getText());
        long shiftEndLong = TimeConverter.getLongFromDateAndTime("" + endDate.getText(), "" + toTime.getText());
        FrameShiftDatabaseHelper dbHelper = new FrameShiftDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (shiftStartLong > shiftEndLong) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error: Shift cannot finish before it starts!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (this.insertShift(db, shiftStartLong, shiftEndLong, "")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("currentWeek", shiftStartLong);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Error: Can't save shift, it overlaps with an existing shift.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    private void editShift(View v) {
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView toTime = (TextView) findViewById(R.id.endTime);
        long shiftStartLong = TimeConverter.getLongFromDateAndTime("" + startDate.getText(), "" + startTime.getText());
        long shiftEndLong = TimeConverter.getLongFromDateAndTime("" + endDate.getText(), "" + toTime.getText());
        if (shiftStartLong > shiftEndLong) { //if the shift finishes before it starts
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error: Shift cannot finish before it starts!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else { // if it finishes after it starts, check for conflicts
            FrameShiftDatabaseHelper dbHelper = new FrameShiftDatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("SHIFT",
                    new String[]{"_id"},
                    "START_TIME < ? and END_TIME > ? and _id != ?", new String[]{String.valueOf(shiftEndLong), String.valueOf(shiftStartLong), String.valueOf(shiftID)}, null, null, null);
            if (cursor.getCount() <= 0) { //if there is no conflicting shift
                cursor.close();
                ContentValues shiftValues = new ContentValues();
                shiftValues.put("START_TIME", shiftStartLong);
                shiftValues.put("END_TIME", shiftEndLong);
                shiftValues.put("COMMENT", "");
                // do the update
                db.update("SHIFT",
                        shiftValues,
                        "_id = ?",
                        new String[]{String.valueOf(shiftID)}
                );
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("currentWeek", shiftStartLong);
                startActivity(intent);
            } else {
                cursor.close();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Error: Can't save shift, it overlaps with an existing shift.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }


    /**
     * Inserts a shift into the database with the start time, end time and comment passed in the
     * parameters
     *
     * @param db the database
     */
    private static boolean insertShift(SQLiteDatabase db, long startTime, long endTime, String comment) {
        Cursor cursor = db.query("SHIFT",
                new String[]{"_id"},
                "START_TIME < ? and END_TIME > ?", new String[]{String.valueOf(endTime), String.valueOf(startTime)}, null, null, null);

        //if there is no conflicting shift
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues shiftValues = new ContentValues();
            shiftValues.put("START_TIME", startTime);
            shiftValues.put("END_TIME", endTime);
            shiftValues.put("COMMENT", comment);
            db.insert("SHIFT", null, shiftValues);
            return true;
        } else { //if there is a conflict
            cursor.close();
            return false;
        }
    }

    private void deleteShift(View v) {
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        long shiftStartLong = TimeConverter.getLongFromDateAndTime("" + startDate.getText(), "" + startTime.getText());
        FrameShiftDatabaseHelper dbHelper = new FrameShiftDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("SHIFT", "_id = ?", new String[]{String.valueOf(shiftID)});
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentWeek", shiftStartLong);
        startActivity(intent);
    }

}
