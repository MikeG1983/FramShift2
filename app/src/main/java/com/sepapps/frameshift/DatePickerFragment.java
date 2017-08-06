package com.sepapps.frameshift;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int mId;
    private DatePickerDialogListener mListener;
    int mDay;
    int mMonth;
    int mYear;

    static DatePickerFragment newInstance(int id, int year, int month, int day) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        args.putInt("set_year", year);
        args.putInt("set_month", month);
        args.putInt("set_day", day);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
/*
 * Creating a bundle object to pass currently set date to the fragment
 */
        Bundle b = getArguments();
        /*
         getting the year from bundle
         */
        mYear = b.getInt("set_year");
        /*
         * getting the month from the bundle
         */
        mMonth = b.getInt("set_month");
        /*
         * getting the day from the bundle*/
        mDay = b.getInt("set_day");
        /*
         * getting the id from the bundle*/
        mId = b.getInt("picker_id");
        mListener = getActivity() instanceof DatePickerDialogListener ? (DatePickerDialogListener) getActivity() : null;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (mListener != null) mListener.onDateSet(mId, view, year, month, day);
    }
    public interface DatePickerDialogListener {
        void onDateSet(int id, DatePicker view, int year, int month, int day);
    }

}
