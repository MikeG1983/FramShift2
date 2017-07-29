package com.sepapps.frameshift;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ragebunny on 7/23/17.
 */

public class ShiftView {
    private String startTime, endTime;

    public ShiftView(String start, String end) {
        startTime = start;
        endTime = end;

    }

    public LinearLayout getNormalShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        startTime.setGravity(Gravity.CENTER);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        View rectangle = new View(parent.getContext());
        rectangle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sizeOfView()));
        rectangle.setBackgroundResource(R.drawable.normal_shift_rectangle);
        normalShift.addView(rectangle);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        endTime.setGravity(Gravity.CENTER);
        endTime.setText(this.endTime);
        normalShift.addView(endTime);
        return normalShift;
    }

    /**
     * @param parent
     * @param pixelReduce This variable is for reducing the size of the whitespace if we need to
     *                    account for TextViews. To keep the rectangle sizes correct.
     * @return
     */
    public LinearLayout getWhitespaceShiftView(ViewGroup parent, int pixelReduce) {
        LinearLayout whitespaceShift = new LinearLayout(parent.getContext());
        whitespaceShift.setOrientation(LinearLayout.VERTICAL);
        whitespaceShift.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        whitespaceShift.setLayoutParams(LLParams);
        View rectangle = new View(parent.getContext());
        int theViewHeight = sizeOfView() - pixelReduce;
        if (theViewHeight < 0) {
            theViewHeight = 0;
        }
        rectangle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, theViewHeight));
        rectangle.setBackgroundResource(R.drawable.normal_background);
        whitespaceShift.addView(rectangle);
        return whitespaceShift;
    }

    private Integer sizeOfView() {
        //calculate the amount of space used
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        int availableSpace = (MainActivity.deviceHeight - usedSpace);
        int totalMinutesInDay = 1440;
        String[] startArray = this.startTime.split(":");
        int startHour = Integer.valueOf(startArray[0]);
        int startMinute = Integer.valueOf(startArray[1]);
        String[] endArray = this.endTime.split(":");
        int endHour = Integer.valueOf(endArray[0]);
        int endMinute = Integer.valueOf(endArray[1]);
        int startMinuteOfDay = (startHour * 60) + startMinute;
        int endMinuteOfDay = (endHour * 60) + endMinute;
        int duration = endMinuteOfDay - startMinuteOfDay;
        double durationPercentageOfDay = duration / (totalMinutesInDay / 100);
        int sizeOfView = (int) Math.floor(durationPercentageOfDay * (availableSpace / 100));
        return sizeOfView;
    }

    /**
     * This method will return the amount of minutes gap that is needed to display a textView
     * on this device, so that if the gap between 2 shifts is too small the app can display the text
     * inside the rectangle
     */
    public static int getMinutesBreakpoint() {
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        int availableSpace = (MainActivity.deviceHeight - usedSpace);
        int totalMinutesInDay = 1440;
        int pixelPerTextview = (int) (10 * MainActivity.deviceDensity);
        double onePercentOfAvailableSpace = availableSpace / 100.0;
        //work out the percentage of the grid cell that a textview takes up
        double textViewPercentage = (pixelPerTextview / (onePercentOfAvailableSpace));
        //work out the relative number of minutes if a cell = 24hours
        int numberOfMinutes = (int) Math.floor(totalMinutesInDay * (textViewPercentage / 100));
        return numberOfMinutes;
    }

    public LinearLayout getNoStartTextShiftView(ViewGroup parent) {
        LinearLayout noStartTextShift = new LinearLayout(parent.getContext());
        noStartTextShift.setOrientation(LinearLayout.VERTICAL);
        noStartTextShift.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        noStartTextShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView()));
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        startTime.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        startTime.setBackgroundResource(R.drawable.normal_shift_rectangle);
        startTime.setText(this.startTime);
        noStartTextShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        endTime.setGravity(Gravity.CENTER);
        endTime.setText(this.endTime);
        noStartTextShift.addView(endTime);
        return noStartTextShift;
    }

    public LinearLayout getNoEndTextShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        startTime.setGravity(Gravity.CENTER);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView()));
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        endTime.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        endTime.setText(this.endTime);
        endTime.setBackgroundResource(R.drawable.normal_shift_rectangle);
        normalShift.addView(endTime);
        return normalShift;
    }

    public LinearLayout getNoStartOrEndTextShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setBackgroundResource(R.drawable.normal_shift_rectangle);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView());
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        startTime.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        endTime.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        endTime.setText(this.endTime);
        normalShift.addView(endTime);
        return normalShift;
    }


}
