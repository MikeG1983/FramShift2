package com.sepapps.frameshift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;


public class FrameShiftDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "frameShift"; //the name of the database
    private static final int DB_VERSION = 1; //the version of the database

    FrameShiftDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SHIFT ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "START_TIME NUMERIC , "
                + "END_TIME NUMERIC, " +
                "COMMENT TEXT) ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }






    }


