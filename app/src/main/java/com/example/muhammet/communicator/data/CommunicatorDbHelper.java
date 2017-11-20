package com.example.muhammet.communicator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommunicatorDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "communicator2.db";

    private static final int VERSION = 1;


    public CommunicatorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + CommunicatorContract.BuyMeEntry.TABLE_NAME + " (" +
                CommunicatorContract.BuyMeEntry._ID                + " INTEGER PRIMARY KEY, " +
                CommunicatorContract.BuyMeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommunicatorContract.BuyMeEntry.COLUMN_DESCRIPTION    + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CommunicatorContract.BuyMeEntry.TABLE_NAME);
        onCreate(db);
    }
}
