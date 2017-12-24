package com.example.muhammet.communicator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommunicatorDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "communicator2.db";

    private static final int VERSION = 14;

    public CommunicatorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_USERS_TABLE = "CREATE TABLE " + CommunicatorContract.UserEntry.TABLE_NAME + " (" +
                CommunicatorContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommunicatorContract.UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_BALANCE + " DECIMAL(6,2), " +
                CommunicatorContract.UserEntry.COLUMN_PHOTO_URL + " TEXT NOT NULL, " +
                CommunicatorContract.UserEntry.COLUMN_STATUS + " INTEGER, " +
                CommunicatorContract.UserEntry.COLUMN_CREATED_TIME + " TEXT, " +
                CommunicatorContract.UserEntry.COLUMN_HOUSE_ID + " TEXT, " +
                CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE);";

        final String CREATE_HOUSES_TABLE = "CREATE TABLE " + CommunicatorContract.HouseEntry.TABLE_NAME + " (" +
                CommunicatorContract.HouseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommunicatorContract.HouseEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.HouseEntry.COLUMN_FACEBOOK_ID + " TEXT UNIQUE ON CONFLICT REPLACE, " +
                CommunicatorContract.HouseEntry.COLUMN_CREATED_TIME + " TEXT, " +
                "FOREIGN KEY (" + CommunicatorContract.HouseEntry.COLUMN_FACEBOOK_ID + ") " + "REFERENCES " +
                CommunicatorContract.UserEntry.TABLE_NAME + "(" + CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID  +
                "));";

        final String CREATE_BUY_MES_TABLE = "CREATE TABLE "  + CommunicatorContract.BuyMeEntry.TABLE_NAME + " (" +
                CommunicatorContract.BuyMeEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommunicatorContract.BuyMeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.BuyMeEntry.COLUMN_DESCRIPTION    + " TEXT NOT NULL, " +
                CommunicatorContract.BuyMeEntry.COLUMN_CREATED_TIME + " TEXT, " +
                CommunicatorContract.BuyMeEntry.COLUMN_FACEBOOK_ID + " TEXT, " +
                CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID + " TEXT, " +
                "FOREIGN KEY (" + CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID + ") " + "REFERENCES " +
                CommunicatorContract.HouseEntry.TABLE_NAME + "(" + CommunicatorContract.HouseEntry._ID + ")," +
                "FOREIGN KEY (" + CommunicatorContract.BuyMeEntry.COLUMN_FACEBOOK_ID + ") " + "REFERENCES " +
                CommunicatorContract.UserEntry.TABLE_NAME + "(" + CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID + ")" +
                ");";

        final String CREATE_SPENDINGS_TABLE = "CREATE TABLE " + CommunicatorContract.SpendingEntry.TABLE_NAME + " (" +
                CommunicatorContract.SpendingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommunicatorContract.SpendingEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.SpendingEntry.COLUMN_CREATED_TIME + " TEXT, " +
                CommunicatorContract.SpendingEntry.COLUMN_COST + " DECIMAL(6,2) NOT NULL, " +
                CommunicatorContract.SpendingEntry.COLUMN_FACEBOOK_ID + " TEXT, " +
                CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID + " TEXT, " +
                "FOREIGN KEY (" + CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID + ") " + "REFERENCES " +
                CommunicatorContract.HouseEntry.TABLE_NAME + "(" + CommunicatorContract.HouseEntry._ID + "), " +
                "FOREIGN KEY (" + CommunicatorContract.SpendingEntry.COLUMN_FACEBOOK_ID + ") " + "REFERENCES " +
                CommunicatorContract.UserEntry.TABLE_NAME + "(" + CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID + "));";

        final String CREATE_HOUSE_MEMBERS_TABLE = "CREATE TABLE " + CommunicatorContract.HouseMemberEntry.TABLE_NAME + " (" +
                CommunicatorContract.HouseMemberEntry.COLUMN_FACEBOOK_ID + " TEXT, " +
                CommunicatorContract.HouseMemberEntry.COLUMN_HOUSE_ID + " TEXT);";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_HOUSES_TABLE);
        db.execSQL(CREATE_BUY_MES_TABLE);
        db.execSQL(CREATE_SPENDINGS_TABLE);
        db.execSQL(CREATE_HOUSE_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.HouseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.BuyMeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.SpendingEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.HouseMemberEntry.TABLE_NAME);

        onCreate(db);
    }
}
