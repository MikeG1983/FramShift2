package com.sepapps.frameshift;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int mId;
    private TimePickerDialogListener mListener;
    int mHour;
    int mMinute;

    static TimePickerFragment newInstance(int id, int hour, int minute) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        args.putInt("set_hour", hour);
        args.putInt("set_minute", minute);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** Creating a bundle object to pass currently set time to the fragment **/
        Bundle b = getArguments();

        /** Getting the hour of day from bundle **/
        mHour = b.getInt("set_hour");

        /** Getting the minute of hour from bundle **/
        mMinute = b.getInt("set_minute");

        /**getting the id from the bundle**/
        mId = b.getInt("picker_id");
        mListener = getActivity() instanceof TimePickerDialogListener ? (TimePickerDialogListener) getActivity() : null;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, mHour, mMinute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mListener != null) mListener.onTimeSet(mId, view, hourOfDay, minute);
    }

    public  interface TimePickerDialogListener {
        void onTimeSet(int id, TimePicker view, int hourOfDay, int minute);
    }

}