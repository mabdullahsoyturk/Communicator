package com.example.muhammet.communicator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommunicatorDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "communicator2.db";

    private static final int VERSION = 4;

    public CommunicatorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_USERS_TABLE = "CREATE TABLE " + CommunicatorContract.UserEntry.TABLE_NAME + " (" +
                CommunicatorContract.BuyMeEntry._ID + " INTEGER PRIMARY KEY, " +
                CommunicatorContract.UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_BALANCE + " REAL, " +
                CommunicatorContract.UserEntry.COLUMN_CREATED_TIME + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_HOUSE_ID + " INTEGER, " +
                "FOREIGN KEY (" + CommunicatorContract.UserEntry.COLUMN_HOUSE_ID + ") " + "REFERENCES " +
                CommunicatorContract.HouseEntry.TABLE_NAME + "(" + CommunicatorContract.HouseEntry._ID + "), " +
                CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID + " TEXT NOT NULL);";

        final String CREATE_HOUSES_TABLE = "CREATE TABLE " + CommunicatorContract.HouseEntry.TABLE_NAME + " (" +
                CommunicatorContract.HouseEntry._ID + " INTEGER PRIMARY KEY, " +
                CommunicatorContract.HouseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.HouseEntry.COLUMN_USER_ID + " INTEGER, " +
                "FOREIGN KEY (" + CommunicatorContract.HouseEntry.COLUMN_USER_ID + ") " + "REFERENCES " +
                CommunicatorContract.UserEntry.TABLE_NAME + "(" + CommunicatorContract.UserEntry._ID + "), " +
                CommunicatorContract.HouseEntry.COLUMN_CREATED_TIME + " TEXT NOT NULL);";

        final String CREATE_BUY_MES_TABLE = "CREATE TABLE "  + CommunicatorContract.BuyMeEntry.TABLE_NAME + " (" +
                CommunicatorContract.BuyMeEntry._ID                + " INTEGER PRIMARY KEY, " +
                CommunicatorContract.BuyMeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.BuyMeEntry.COLUMN_DESCRIPTION    + " TEXT NOT NULL, " +
                CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID + " INTEGER, " +
                "FOREIGN KEY (" + CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID + ") " + "REFERENCES " +
                CommunicatorContract.HouseEntry.TABLE_NAME + "(" + CommunicatorContract.HouseEntry._ID + "));";

        final String CREATE_SPENDINGS_TABLE = "CREATE TABLE " + CommunicatorContract.SpendingEntry.TABLE_NAME + " (" +
                CommunicatorContract.SpendingEntry._ID + " INTEGER PRIMARY KEY, " +
                CommunicatorContract.SpendingEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.SpendingEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                CommunicatorContract.SpendingEntry.COLUMN_USER_ID + " INTEGER, " +
                CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID + " INTEGER, " +
                "FOREIGN KEY (" + CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID + ") " + "REFERENCES " +
                CommunicatorContract.HouseEntry.TABLE_NAME + "(" + CommunicatorContract.HouseEntry._ID + "), " +
                "FOREIGN KEY (" + CommunicatorContract.SpendingEntry.COLUMN_USER_ID + ") " + "REFERENCES " +
                CommunicatorContract.UserEntry.TABLE_NAME + "(" + CommunicatorContract.UserEntry._ID + "));";

        db.execSQL(CREATE_BUY_MES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_HOUSES_TABLE);
        db.execSQL(CREATE_SPENDINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.HouseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.BuyMeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.SpendingEntry.TABLE_NAME);

        onCreate(db);
    }
}
