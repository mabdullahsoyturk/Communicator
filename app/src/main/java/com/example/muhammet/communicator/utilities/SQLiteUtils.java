package com.example.muhammet.communicator.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.muhammet.communicator.data.CommunicatorContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SQLiteUtils {

    public static JSONObject addBuyMeToLocal(Context mContext, String name, String description, String facebook_id, String house_id_server) throws JSONException {
        String formattedDate = DateUtilities.getFormattedDate();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("house_id_server", house_id_server);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.BuyMeEntry.CONTENT_URI, contentValues);

        long buyMeId;

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int buyMeIndex = cursor.getColumnIndex(CommunicatorContract.BuyMeEntry._ID);
            buyMeId = cursor.getLong(buyMeIndex);
        }else{
            buyMeId = ContentUris.parseId(uri);
        }

        cursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", buyMeId);
        jsonParam.put("name", name);
        jsonParam.put("description", description);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("house_id", house_id_server);
        jsonParam.put("created_time", formattedDate);

        return jsonParam;
    }

    public static JSONObject addSpendingToLocal(Context mContext, String name, double cost, String facebook_id, String house_id_server, List<String> listOfIds) throws JSONException {
        
        //////////////////////////////////////Add Spending Part/////////////////////////////////////
        String formattedDate = DateUtilities.getFormattedDate();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("cost", cost);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("house_id_server", house_id_server);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.SpendingEntry.CONTENT_URI, contentValues);

        long spendingId;

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(CommunicatorContract.SpendingEntry._ID);
            spendingId = cursor.getLong(idIndex);
        }else{
            spendingId = ContentUris.parseId(uri);
        }

        cursor.close();
        ////////////////////////////////////////////////////////////////////////////////////////////

        String hid_server = "";

        for(int i = 0; i < listOfIds.size(); i++){
            Cursor userCursor = mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                    null,
                    "facebook_id=?",
                    new String[]{listOfIds.get(i)},
                    null
            );
            
            if(userCursor.moveToFirst()){
                long user_id = userCursor.getLong(userCursor.getColumnIndex(CommunicatorContract.UserEntry._ID));
                String first_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
                String last_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
                double balance = userCursor.getDouble(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
                String photo_url = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
                int status = userCursor.getInt(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
                String created = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
                String fid = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
                String hid = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_HOUSE_ID));
                hid_server = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_HOUSE_ID_SERVER));

                if(listOfIds.get(i).equals(facebook_id)){
                    ContentValues cv = new ContentValues();
                    cv.put("_id", user_id);
                    cv.put("first_name", first_name);
                    cv.put("last_name", last_name);
                    cv.put("balance", balance + (cost - (cost/listOfIds.size())));
                    cv.put("photo_url", photo_url);
                    cv.put("status", status);
                    cv.put("created_time", created);
                    cv.put("facebook_id", fid);
                    cv.put("house_id", hid);
                    cv.put("house_id_server", hid_server);

                    mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put("_id", user_id);
                    cv.put("first_name", first_name);
                    cv.put("last_name", last_name);
                    cv.put("balance", balance - (cost/listOfIds.size()));
                    cv.put("photo_url", photo_url);
                    cv.put("status", status);
                    cv.put("created_time", created);
                    cv.put("facebook_id", fid);
                    cv.put("house_id", hid);
                    cv.put("house_id_server", hid_server);

                    mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < listOfIds.size(); i++){
            jsonArray.put(i, listOfIds.get(i));
        }

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", spendingId);
        jsonParam.put("name", name);
        jsonParam.put("cost", cost);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("house_id", hid_server);
        jsonParam.put("created_time", formattedDate);
        jsonParam.put("facebook_ids", jsonArray);

        return jsonParam;
    }

    public static JSONObject addHouseToLocal(Context mContext, String house_name, String facebook_id) throws JSONException {
        String formattedDate = DateUtilities.getFormattedDate();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", house_name);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.HouseEntry.CONTENT_URI, contentValues);

        long house_id;

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int houseIndex = cursor.getColumnIndex(CommunicatorContract.HouseEntry._ID);
            house_id = cursor.getLong(houseIndex);
            String face = cursor.getString(cursor.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_FACEBOOK_ID));
            Log.i("face", face);
        }else{
            house_id = ContentUris.parseId(uri);
        }

        cursor.close();

        Cursor userCursor = mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                null,
                "facebook_id=?",
                new String[]{facebook_id},
                null);

        if(userCursor.moveToFirst()){
            long user_id = userCursor.getLong(userCursor.getColumnIndex(CommunicatorContract.UserEntry._ID));
            String first_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            String last_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
            double balance = userCursor.getDouble(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
            String photo_url = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
            int status = userCursor.getInt(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
            String created = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
            String fid = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
            String hid = String.valueOf(house_id);

            ContentValues cv = new ContentValues();
            cv.put("_id", user_id);
            cv.put("first_name", first_name);
            cv.put("last_name", last_name);
            cv.put("balance", balance);
            cv.put("photo_url", photo_url);
            cv.put("status", status);
            cv.put("created_time", created);
            cv.put("facebook_id", fid);
            cv.put("house_id", hid);
            Log.i("addnewhouse", hid);

            mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
        }
        userCursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id",house_id);
        jsonParam.put("name", house_name);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("created_time", formattedDate);

        return jsonParam;
    }

    public static void updateHouse(Context mContext, String house_id, String house_id_server){
        Cursor cursor = mContext.getContentResolver().query(CommunicatorContract.HouseEntry.CONTENT_URI,
                null,
                "_id=?",
                new String[]{house_id},
                null);

        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_NAME));
            String facebook_id = cursor.getString(cursor.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_FACEBOOK_ID));
            String created_time = cursor.getString(cursor.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_CREATED_TIME));

            ContentValues cv = new ContentValues();
            cv.put("_id", house_id);
            cv.put("name", name);
            cv.put("facebook_id", facebook_id);
            cv.put("created_time", created_time);
            cv.put("house_id_server", house_id_server);

            mContext.getContentResolver().update(CommunicatorContract.HouseEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(house_id)).build(), cv, null,null);

        }
        cursor.close();
    }

    public static JSONObject addMemberToLocal(Context mContext, String first_name, String last_name, String photo_url, String facebook_id) throws JSONException {
        String formattedDate = DateUtilities.getFormattedDate();

        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("balance", 0);
        contentValues.put("photo_url", photo_url);
        contentValues.put("status", 1);
        contentValues.put("created_time", formattedDate);
        contentValues.put("facebook_id", facebook_id);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.UserEntry.CONTENT_URI, contentValues);

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        long id;

        if(cursor.moveToFirst()){
            int userIndex = cursor.getColumnIndex(CommunicatorContract.UserEntry._ID);
            id = cursor.getLong(userIndex);
        }else{
            id = ContentUris.parseId(uri);
        }

        cursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("first_name", first_name);
        jsonParam.put("last_name", last_name);
        jsonParam.put("photo_url", photo_url);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("id", String.valueOf(id));

        return jsonParam;
    }

    public static void addPayment(Context mContext, String facebook_id, double how_much, String to){
        Cursor userCursor = mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                null,
                "facebook_id=?",
                new String[]{facebook_id},
                null);

        long id;

        if(userCursor.moveToFirst()){
            id = userCursor.getLong(userCursor.getColumnIndex(CommunicatorContract.UserEntry._ID));
            long user_id = userCursor.getLong(userCursor.getColumnIndex(CommunicatorContract.UserEntry._ID));
            String first_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            String last_name = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
            double balance = userCursor.getDouble(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
            String photo_url = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
            int status = userCursor.getInt(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
            String created = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
            String fid = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
            String hid = userCursor.getString(userCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_HOUSE_ID));

            ContentValues cv = new ContentValues();
            cv.put("_id", id);
            cv.put("first_name", first_name);
            cv.put("last_name", last_name);
            cv.put("balance", balance + how_much);
            cv.put("photo_url", photo_url);
            cv.put("status", status);
            cv.put("created_time", created);
            cv.put("facebook_id", fid);
            cv.put("house_id", hid);

            mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
        }

        userCursor.close();

        Cursor userCursor2 = mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                null,
                "facebook_id=?",
                new String[]{facebook_id},
                null);

        long id2;

        if(userCursor2.moveToFirst()){
            id2 = userCursor2.getLong(userCursor2.getColumnIndex(CommunicatorContract.UserEntry._ID));
            long user_id = userCursor2.getLong(userCursor2.getColumnIndex(CommunicatorContract.UserEntry._ID));
            String first_name = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            String last_name = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
            double balance = userCursor2.getDouble(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
            String photo_url = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
            int status = userCursor2.getInt(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
            String created = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
            String fid = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
            String hid = userCursor2.getString(userCursor2.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_HOUSE_ID));

            ContentValues cv = new ContentValues();
            cv.put("_id", id2);
            cv.put("first_name", first_name);
            cv.put("last_name", last_name);
            cv.put("balance", balance - how_much);
            cv.put("photo_url", photo_url);
            cv.put("status", status);
            cv.put("created_time", created);
            cv.put("facebook_id", fid);
            cv.put("house_id", hid);

            mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
        }

        userCursor2.close();

    }



}
