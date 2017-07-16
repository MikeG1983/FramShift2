package com.sepapps.frameshift;

import android.app.Activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends Activity {
   public static int deviceHeight, deviceWidth, actionBarHeight;
         public static float deviceDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        deviceHeight = metrics.heightPixels;
        deviceWidth = metrics.widthPixels;
        deviceDensity = metrics.density;
        final TypedArray styledAttributes = MainActivity.this.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
