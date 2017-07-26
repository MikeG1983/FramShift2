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

public class normalShiftView {
    private String startTime, endTime;

    public normalShiftView(String start, String end) {
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
        startTime.setGravity(View.TEXT_ALIGNMENT_CENTER);
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
        endTime.setGravity(View.TEXT_ALIGNMENT_CENTER);
        endTime.setText(this.endTime);
        normalShift.addView(endTime);
        return normalShift;
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

}
