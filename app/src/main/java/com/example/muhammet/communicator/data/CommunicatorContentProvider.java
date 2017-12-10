package com.example.muhammet.communicator.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.muhammet.communicator.data.CommunicatorContract.BuyMeEntry.TABLE_NAME;

public class CommunicatorContentProvider extends ContentProvider{

    public static final int USERS = 100;
    public static final int USERS_WITH_ID = 101;
    public static final int HOUSES = 200;
    public static final int HOUSES_WITH_ID = 201;
    public static final int BUY_MES = 300;
    public static final int BUY_MES_WITH_ID = 301;
    public static final int SPENDINGS = 400;
    public static final int SPENDINGS_WITH_ID = 401;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_USERS, USERS);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_USERS + "/#", USERS_WITH_ID);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_HOUSES, HOUSES);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_HOUSES + "/#", HOUSES_WITH_ID);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_BUY_MES, BUY_MES);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_BUY_MES + "/#", BUY_MES_WITH_ID);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_SPENDINGS, SPENDINGS);
        uriMatcher.addURI(CommunicatorContract.AUTHORITY, CommunicatorContract.PATH_SPENDINGS + "/#", SPENDINGS_WITH_ID);

        return uriMatcher;
    }

    private CommunicatorDbHelper mCommunicatorDbHelper;


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mCommunicatorDbHelper = new CommunicatorDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = mCommunicatorDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case BUY_MES:
                retCursor =  db.query(CommunicatorContract.BuyMeEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;

            case BUY_MES_WITH_ID:
                // Get the task ID from the URI path
                String buyMeId = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                retCursor = db.query(CommunicatorContract.BuyMeEntry.TABLE_NAME,
                        null,
                        "_id=?",
                        new String[]{buyMeId},
                        null,
                        null,
                        null);
                break;
            case USERS:
                retCursor = db.query(CommunicatorContract.UserEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                        );

            case HOUSES:
                retCursor = db.query(CommunicatorContract.HouseEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

            case SPENDINGS:
                retCursor = db.query(CommunicatorContract.SpendingEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mCommunicatorDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case BUY_MES:
                // Insert new values into the database
                // Inserting values into tasks table
                long buy_me_id = db.insert(CommunicatorContract.BuyMeEntry.TABLE_NAME, null, contentValues);
                Log.i("buy_me_id",""+ buy_me_id);
                if ( buy_me_id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CommunicatorContract.BuyMeEntry.CONTENT_URI, buy_me_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case USERS:
                long user_id = db.insert(CommunicatorContract.UserEntry.TABLE_NAME, null, contentValues);
                if ( user_id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CommunicatorContract.UserEntry.CONTENT_URI, user_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case HOUSES:
                long house_id = db.insert(CommunicatorContract.HouseEntry.TABLE_NAME, null, contentValues);
                if ( house_id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CommunicatorContract.HouseEntry.CONTENT_URI, house_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case SPENDINGS:
                long spending_id = db.insert(CommunicatorContract.SpendingEntry.TABLE_NAME, null, contentValues);
                if ( spending_id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CommunicatorContract.SpendingEntry.CONTENT_URI, spending_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mCommunicatorDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case BUY_MES_WITH_ID:
                // Get the task ID from the URI path
                String buy_me_id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(CommunicatorContract.BuyMeEntry.TABLE_NAME, "_id=?", new String[]{buy_me_id});
                break;
            case BUY_MES:
                tasksDeleted = db.delete(CommunicatorContract.BuyMeEntry.TABLE_NAME, null, null);
                break;

            case USERS_WITH_ID:
                // Get the task ID from the URI path
                String user_id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(CommunicatorContract.UserEntry.TABLE_NAME, "_id=?", new String[]{user_id});
                break;

            case HOUSES_WITH_ID:
                // Get the task ID from the URI path
                String house_id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(CommunicatorContract.HouseEntry.TABLE_NAME, "_id=?", new String[]{house_id});
                break;

            case SPENDINGS_WITH_ID:
                // Get the task ID from the URI path
                String spending_id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(CommunicatorContract.SpendingEntry.TABLE_NAME, "_id=?", new String[]{spending_id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BUY_MES_WITH_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = mCommunicatorDbHelper.getWritableDatabase().update(TABLE_NAME, contentValues, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }
}
