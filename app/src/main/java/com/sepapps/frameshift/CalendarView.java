package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/17/17.
 */

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CalendarView extends Fragment {
    protected final Calendar calendar;
    private final Locale locale;
    private ViewSwitcher calendarSwitcher;
    private TextView currentWeek;
    private CalendarAdapter calendarAdapter;
    private static long idCounter = 0;

    //constructor
    public CalendarView() {
        calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//        locale = Locale.getDefault();
        locale = Locale.UK;


    }

    // return the view object
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set the layout views to variables +  get a gesture detector
        final RelativeLayout calendarLayout = (RelativeLayout)inflater.inflate(R.layout.calendar, null);
        final RelativeLayout gridHolderLayout = (RelativeLayout)calendarLayout.findViewById(R.id.grid_holder);
        final RelativeLayout calendarHeaderLayout = (RelativeLayout)calendarLayout.findViewById(R.id.calendar_header);
        final GridView calendarDayGrid = (GridView)calendarLayout.findViewById(R.id.calendar_days_grid);
        final GestureDetector swipeDetector = new GestureDetector(getActivity(), new SwipeGesture(getActivity()));
        final GridView shiftGrid = (GridView)calendarLayout.findViewById(R.id.shift_grid);
        calendarSwitcher = (ViewSwitcher)calendarLayout.findViewById(R.id.calendar_switcher);
        currentWeek = (TextView)calendarLayout.findViewById(R.id.current_week);
        // create a new calendar adapter, and pass it the context and the calendar
        calendarAdapter = new CalendarAdapter(getActivity(), calendar);
        // I think this sets the month in the title
        updateCurrentWeek();
        // set the buttons to variables and set the event listeners for the buttons and the day
        //grid
        final TextView nextWeek = (TextView) calendarLayout.findViewById(R.id.next_week);
        nextWeek.setOnClickListener(new NextWeekClickListener());
        final TextView prevWeek = (TextView) calendarLayout.findViewById(R.id.previous_week);
        prevWeek.setOnClickListener(new PreviousWeekClickListener());
//        shift.setOnItemClickListener(new ShiftItemClickListener());
        //Set the adapter for the calendar days grid
        calendarDayGrid.setAdapter(calendarAdapter);
        //TODO set the adapter for the shift grid
//        shiftGrid.setAdapter(calendarAdapter);
        //set a swipe listener for the body
        shiftGrid.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return swipeDetector.onTouchEvent(event);
            }
        });



        return calendarLayout;
    }

    protected void updateCurrentWeek() {
        //call the calendar adapter method refresh days
        calendarAdapter.refreshDays();
        //call the shift adapter method refreshShifts
//        shiftAdapter.refreshDays();
        // sets the current week in the title
        // set the first day of the week for the locale (monday)

        // set the day to monday (this eventually will be read from user settings.
        Calendar calCopy = (Calendar)(calendar.clone());
        calCopy.set(Calendar.DAY_OF_WEEK, calCopy.getFirstDayOfWeek());
        String title = "" + calCopy.get(Calendar.DAY_OF_MONTH) + " "
                + calCopy.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
        calCopy.add(Calendar.DATE, 6);
        title += " - " + calCopy.get(Calendar.DAY_OF_MONTH) + " "
                + calCopy.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK) + " ";
        title += calCopy.get(Calendar.YEAR);
        currentWeek.setText("");
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(title);

    }

//    private final class ShiftItemClickListener implements OnItemClickListener {
//        //when a day item is clicked
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final TextView dayView = (TextView)view.findViewById(R.id.date);
//            final CharSequence text = dayView.getText();
//            if (text != null && !"".equals(text)) {
//                //highlight the day
//                calendarAdapter.setSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Integer.valueOf(String.valueOf(text)));
//            }
//        }
//    }

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
    private final class SwipeGesture extends SimpleOnGestureListener {
        private final int swipeMinDistance;
        private final int swipeThresholdVelocity;

        public SwipeGesture(Context context) {
            final ViewConfiguration viewConfig = ViewConfiguration.get(context);
            swipeMinDistance = viewConfig.getScaledTouchSlop();
            swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
        }
        // if the swipe velocity is more than the threshhold then call the relevant method
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
                onNextWeek();
            }  else if (e2.getX() - e1.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
                onPreviousWeek();
            }
            return false;
        }
    }

    public static synchronized Long createID() {
        return idCounter++;
    }
}
