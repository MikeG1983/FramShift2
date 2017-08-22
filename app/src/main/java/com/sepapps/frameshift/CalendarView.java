package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/17/17.
 */

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CalendarView extends Fragment {
    protected final Calendar calendar;
    private ViewSwitcher calendarSwitcher;
    private TextView currentWeek;
    private CalendarAdapter calendarAdapter;
    private static long idCounter = 0;
    private GridView shiftGrid;
    private ShiftAdapter shiftAdapter;
    private ViewConfiguration viewConfig;
    public static int gmtOffset;

    //constructor
    public CalendarView() {
        Locale locale = MainActivity.locale;
        TimeZone defaultTime = TimeZone.getDefault();
        calendar = new GregorianCalendar(defaultTime, locale);
        if (MainActivity.currentWeek != 0L) {
            calendar.setTimeInMillis(MainActivity.currentWeek);
        }
        gmtOffset = defaultTime.getOffset(
                calendar.get(Calendar.ERA),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.DAY_OF_WEEK),
                calendar.get(Calendar.MILLISECOND));
        gmtOffset = gmtOffset / (60*60*1000);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, gmtOffset);
    }

    // return the view object
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set the layout views to variables +  get a gesture detector
        final RelativeLayout calendarLayout = (RelativeLayout) inflater.inflate(R.layout.calendar, null);
        final GridView calendarDayGrid = (GridView) calendarLayout.findViewById(R.id.calendar_days_grid);
        viewConfig = ViewConfiguration.get(getActivity());
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    private MotionEvent mLastOnDownEvent = null;

                    @Override
                    public boolean onDown(MotionEvent e) {
                        //Android 4.0 bug means e1 in onFling may be NULL due to onLongPress eating it.
                        mLastOnDownEvent = e;
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        final int swipeMinDistance = viewConfig.getScaledTouchSlop();
                        final int swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
                        try {
                            if (e1 == null)
                                e1 = mLastOnDownEvent;
                            if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
                                onNextWeek();
                            } else if (e2.getX() - e1.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
                                onPreviousWeek();
                            }
                            return false;
                        } catch (Exception e) {
                            //nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        shiftGrid = (GridView) calendarLayout.findViewById(R.id.shift_grid);
        shiftGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        calendarSwitcher = (ViewSwitcher) calendarLayout.findViewById(R.id.calendar_switcher);
        currentWeek = (TextView) calendarLayout.findViewById(R.id.current_week);
        // create a new calendar adapter, and pass it the context and the calendar
        calendarAdapter = new
                CalendarAdapter(getActivity(), calendar);
        shiftAdapter = new
                ShiftAdapter(getActivity(), calendar);

        // This sets the month in the title
        updateCurrentWeek();

        // set the buttons to variables and set the event listeners for the buttons and the day
        //grid
        final TextView nextWeek = (TextView) calendarLayout.findViewById(R.id.next_week);
        nextWeek.setOnClickListener(new

                NextWeekClickListener());
        final TextView prevWeek = (TextView) calendarLayout.findViewById(R.id.previous_week);
        prevWeek.setOnClickListener(new

                PreviousWeekClickListener());

        //Set the adapter for the calendar days grid
        calendarDayGrid.setAdapter(calendarAdapter);

        shiftGrid.setAdapter(shiftAdapter);

        return calendarLayout;
    }

    protected void updateCurrentWeek() {
        //call the calendar adapter method refresh days
        calendarAdapter.refreshDays();
        //call the shift adapter method refreshShifts
        shiftAdapter.refreshShifts();
        // sets the current week in the title

        // set the day to monday (this eventually will be read from user settings.
        Calendar calCopy = (Calendar) (calendar.clone());
        long theTime = calCopy.getTimeInMillis();
        calCopy.set(Calendar.DAY_OF_WEEK, calCopy.getFirstDayOfWeek());
        theTime = calCopy.getTimeInMillis();
        String title = "" + calCopy.get(Calendar.DAY_OF_MONTH) + " "
                + calCopy.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
        calCopy.add(Calendar.DATE, 6);
        title += " - " + calCopy.get(Calendar.DAY_OF_MONTH) + " "
                + calCopy.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK) + " ";
        title += calCopy.get(Calendar.YEAR);
        currentWeek.setText(title);
//        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setTitle(title);

    }


    protected final void onNextWeek() {
        //when the next button is clicked, set the animations and show the next week's days
        calendarSwitcher.setInAnimation(getActivity(), R.anim.in_from_right);
        calendarSwitcher.setOutAnimation(getActivity(), R.anim.out_to_left);
        calendarSwitcher.showNext();
        // If the week is at the end of the year, then show the next year's first week
        int thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (thisWeek == 52) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            calendar.set(Calendar.WEEK_OF_YEAR, 1);
            // otherwise show the next week of the year
        } else {
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + 1);
        }
        //update the title
        updateCurrentWeek();
    }

    protected final void onPreviousWeek() {
        //when the previous button is clicked, set the animations for the previous week's days
        calendarSwitcher.setInAnimation(getActivity(), R.anim.in_from_left);
        calendarSwitcher.setOutAnimation(getActivity(), R.anim.out_to_right);
        calendarSwitcher.showPrevious();
        // if this is the first week of the year, then show the last week of the previous year
        int thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (thisWeek == 1) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            calendar.set(Calendar.WEEK_OF_YEAR, 52);
            //other wise show the previous week of the year
        } else {
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1);
        }
        updateCurrentWeek();
    }

    private final class NextWeekClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onNextWeek();
        }
    }

    private final class PreviousWeekClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onPreviousWeek();
        }
    }


    public static synchronized Long createID() {
        return idCounter++;
    }
}
