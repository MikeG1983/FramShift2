package com.sepapps.frameshift;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

public class MainActivity extends Activity {
    public static int deviceHeight, deviceWidth, actionBarHeight, shiftGridCellHeight, dayGridHeight, navBarHeight;
    public static float deviceDensity;
    public static Locale locale;
    public static DisplayMetrics metrics;
    public static long currentWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Long defaultLong = 0L;
        currentWeek = intent.getLongExtra("currentWeek", defaultLong);
        locale = Locale.UK;
        metrics = new DisplayMetrics();
        //get the screen dimensions
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        deviceHeight = metrics.heightPixels;
        deviceWidth = metrics.widthPixels;
        deviceDensity = metrics.density;
        //get the height of the action bar
        final TypedArray styledAttributes = MainActivity.this.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        //work out how much space is available for the shift grid cells
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        shiftGridCellHeight = (MainActivity.deviceHeight - usedSpace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
