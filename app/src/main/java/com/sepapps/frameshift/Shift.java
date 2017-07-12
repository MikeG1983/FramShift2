package com.sepapps.frameshift;

/**
 * Created by ragebunny on 6/25/17.
 */

public class Shift {
    //default access instance variables
    long startTime, endTime;
    long id;

    public Shift(long startingTime, long endingTime) {
        this.startTime = startingTime;
        this.endTime = endingTime;
        this.id = CalendarView.createID();
    }

    public Shift(long startingTime, long endingTime, long definedID) {
        this.startTime = startingTime;
        this.endTime = endingTime;
        this.id = definedID;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getId() {
        return id;
    }
}
