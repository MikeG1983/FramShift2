package com.sepapps.frameshift;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Target;

/**
 * Created by ragebunny on 7/23/17.
 */

public class ShiftView {
    private String startTime, endTime;
    private long baseShiftStart, baseShiftEnd, baseShiftId;

    public ShiftView(String start, String end) {
        startTime = start;
        endTime = end;
            }

    public ShiftView(String start, String end, long longShiftStarting, long longShiftEnding, long baseShiftIdentifier) {
        startTime = start;
        endTime = end;
        baseShiftStart = longShiftStarting;
        baseShiftEnd = longShiftEnding;
        baseShiftId = baseShiftIdentifier;
    }

    public LinearLayout getNormalShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setPadding(0,0,0,0);
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(10);
        startTime.setIncludeFontPadding(false);
        startTime.setPadding(0, 2, 0, 2);
        startTime.setGravity(Gravity.CENTER);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        View rectangle = new View(parent.getContext());
        rectangle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sizeOfView()));
        rectangle.setBackgroundResource(R.drawable.normal_shift_rectangle);
        rectangle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View ve) {
                final View v = ve;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Edit Confirmation");
                builder.setMessage("Do you want to edit this shift ?");
//                builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(v.getRootView().getContext(), EnterShift.class);
                        intent.putExtra("startTime", baseShiftStart);
                        intent.putExtra("endTime", baseShiftEnd);
                        intent.putExtra("id", baseShiftId);
                        intent.putExtra("edit", "yes");
                        v.getRootView().getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        normalShift.addView(rectangle);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        endTime.setTextSize(10);
        endTime.setIncludeFontPadding(false);
        endTime.setPadding(0, 2, 0, 2);
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
        whitespaceShift.setPadding(0,0,0,0);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        whitespaceShift.setLayoutParams(LLParams);
        View rectangle = new View(parent.getContext());
        int theViewHeight = sizeOfView() - pixelReduce;
        if (theViewHeight < 0) {
            theViewHeight = 0;
        }
        rectangle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, theViewHeight));
        rectangle.setBackgroundResource(R.drawable.normal_background_test);
        rectangle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View ve) {
                final View v = ve;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Add Shift Confirmation");
                builder.setMessage("Do you want to add a new shift ?");
//                builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(v.getRootView().getContext(), EnterShift.class);
                        intent.putExtra("startTime", baseShiftStart);
                        intent.putExtra("endTime", baseShiftEnd);
                        intent.putExtra("id", baseShiftId);
                        intent.putExtra("edit", "no");
                        v.getRootView().getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        whitespaceShift.addView(rectangle);
        return whitespaceShift;
    }

    private Integer sizeOfView() {
        //calculate the amount of space used
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        int availableSpace = (MainActivity.deviceHeight - (usedSpace + 20));

        int totalMinutesInDay = 1439;
        String[] startArray = this.startTime.split(":");
        int startHour = Integer.valueOf(startArray[0]);
        int startMinute = Integer.valueOf(startArray[1]);
        String[] endArray = this.endTime.split(":");
        int endHour = Integer.valueOf(endArray[0]);
        int endMinute = Integer.valueOf(endArray[1]);
        int startMinuteOfDay = (startHour * 60) + startMinute;
        int endMinuteOfDay = (endHour * 60) + endMinute;
        int duration = endMinuteOfDay - startMinuteOfDay;
        double totalMinutesDividedBy100 = (double) totalMinutesInDay / 100;
        double durationPercentageOfDay = duration / totalMinutesDividedBy100;
        int sizeOfView = (int) Math.floor(durationPercentageOfDay * ((double) availableSpace / 100));
        return sizeOfView;
    }

    /**
     * This method will return the amount of minutes gap that is needed to display a textView
     * on this device, so that if the gap between 2 shifts is too small the app can display the text
     * inside the rectangle
     */
    public static int getMinutesBreakpoint() {
        int usedSpace = (int) MainActivity.deviceDensity * (92 + MainActivity.actionBarHeight);
        int availableSpace = (MainActivity.deviceHeight - (usedSpace + 20));
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
        noStartTextShift.setPadding(0,0,0,0);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        noStartTextShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView()));
        startTime.setTextSize(10); 
        startTime.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        startTime.setBackgroundResource(R.drawable.normal_shift_rectangle);
        startTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View ve) {
                final View v = ve;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Edit Confirmation");
                builder.setMessage("Do you want to edit this shift ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(v.getRootView().getContext(), EnterShift.class);
                        intent.putExtra("startTime", baseShiftStart);
                        intent.putExtra("endTime", baseShiftEnd);
                        intent.putExtra("id", baseShiftId);
                        intent.putExtra("edit", "yes");
                        v.getRootView().getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        startTime.setText(this.startTime);
        noStartTextShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        endTime.setTextSize(10);
        endTime.setIncludeFontPadding(false);
        endTime.setPadding(0, 2, 0, 2);
        endTime.setGravity(Gravity.CENTER);
        endTime.setText(this.endTime);
        noStartTextShift.addView(endTime);
        return noStartTextShift;
    }

    public LinearLayout getNoEndTextShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setGravity(Gravity.CENTER);
        normalShift.setPadding(0,0,0,0);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(10);
        startTime.setIncludeFontPadding(false);
        startTime.setPadding(0, 2, 0, 2);
        startTime.setGravity(Gravity.CENTER);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView()));
        endTime.setTextSize(10); 
        endTime.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        endTime.setText(this.endTime);
        endTime.setBackgroundResource(R.drawable.normal_shift_rectangle);
        endTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View ve) {
                final View v = ve;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Edit Confirmation");
                builder.setMessage("Do you want to edit this shift ?");
//                builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(v.getRootView().getContext(), EnterShift.class);
                        intent.putExtra("startTime", baseShiftStart);
                        intent.putExtra("endTime", baseShiftEnd);
                        intent.putExtra("id", baseShiftId);
                        intent.putExtra("edit", "yes");
                        v.getRootView().getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        normalShift.addView(endTime);
        return normalShift;
    }

    public LinearLayout getNoStartOrEndTextShiftView(ViewGroup parent) {
        LinearLayout normalShift = new LinearLayout(parent.getContext());
        normalShift.setOrientation(LinearLayout.VERTICAL);
        normalShift.setBackgroundResource(R.drawable.normal_shift_rectangle);
        normalShift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View ve) {
                final View v = ve;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Edit Confirmation");
                builder.setMessage("Do you want to edit this shift ?");
//                builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(v.getRootView().getContext(), EnterShift.class);
                        intent.putExtra("startTime", baseShiftStart);
                        intent.putExtra("endTime", baseShiftEnd);
                        intent.putExtra("id", baseShiftId);
                        intent.putExtra("edit", "yes");
                        v.getRootView().getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        normalShift.setPadding(0,0,0,0);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, sizeOfView());
        normalShift.setLayoutParams(LLParams);
        TextView startTime = new TextView(parent.getContext());
        startTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        startTime.setTextSize(10); 
        startTime.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        startTime.setText(this.startTime);
        normalShift.addView(startTime);
        TextView endTime = new TextView(parent.getContext());
        endTime.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        endTime.setTextSize(10); 
        endTime.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        endTime.setText(this.endTime);
        normalShift.addView(endTime);
        return normalShift;
    }


}
