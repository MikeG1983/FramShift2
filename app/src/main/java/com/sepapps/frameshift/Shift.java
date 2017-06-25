package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/25/17.
 */

public class Shift {
    private int startHour, startMinute, endHour, endMinute;
    private Long id;
    public Shift(int startH, int startM, int endH, int endM){
        this.startHour = startH;
        this.startMinute = startM;
        this.endHour = endH;
        this.endMinute = endM;
        this.id = idManager.createID();
            }
}
