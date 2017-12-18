package com.example.muhammet.communicator.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.muhammet.communicator.data.CommunicatorContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {

    public static final String STATIC_COMMUNICATOR_URL =
            "http://10.0.2.2:3000/";

    //private static final String STATIC_COMMUNICATOR_URL =
    //        "https://warm-meadow-40773.herokuapp.com/";

    private static final String USER_BASE_URL = STATIC_COMMUNICATOR_URL;

    public static String getStringResponse(String url){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("communicatorJsonStr", communicatorJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainActivity", "Error closing stream", e);
                }
            }
        }

        return communicatorJsonStr;
    }


    public static ContentValues[] getBuyMeContentValuesFromJson(Context context, String buyMeStr)
            throws JSONException {

        JSONObject buyMeJson = new JSONObject(buyMeStr);
        
        JSONArray jsonBuyMeArray = buyMeJson.getJSONArray("data");

        ContentValues[] buyMeContentValues = new ContentValues[jsonBuyMeArray.length()];

        for (int i = 0; i < jsonBuyMeArray.length(); i++) {

            int _id;
            String name;
            String description;
            String facebook_id;
            String house_id;
            String created_time;

            JSONObject item = jsonBuyMeArray.getJSONObject(i);
            
            _id = item.getInt("id");
            name= item.getString("name");
            description = item.getString("description");
            facebook_id = item.getString("facebook_id");
            house_id = item.getString("house_id");
            created_time = item.getString("created_time");

            ContentValues buyMeValues = new ContentValues();
            buyMeValues.put(CommunicatorContract.BuyMeEntry._ID, _id);
            buyMeValues.put(CommunicatorContract.BuyMeEntry.COLUMN_NAME, name);
            buyMeValues.put(CommunicatorContract.BuyMeEntry.COLUMN_DESCRIPTION, description);
            buyMeValues.put(CommunicatorContract.BuyMeEntry.COLUMN_FACEBOOK_ID, facebook_id);
            buyMeValues.put(CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID, house_id);
            buyMeValues.put(CommunicatorContract.BuyMeEntry.COLUMN_CREATED_TIME, created_time);

            buyMeContentValues[i] = buyMeValues;
        }

        return buyMeContentValues;
    }

    public static ContentValues[] getSpendingContentValuesFromJson(Context context, String spendingStr)
            throws JSONException {

        JSONObject spendingJson = new JSONObject(spendingStr);

        JSONArray jsonSpendingArray = spendingJson.getJSONArray("data");

        ContentValues[] spendingContentValues = new ContentValues[jsonSpendingArray.length()];

        for (int i = 0; i < jsonSpendingArray.length(); i++) {

            int _id;
            String name;
            String created_time;
            double cost;
            String facebook_id;
            String house_id;

            JSONObject item = jsonSpendingArray.getJSONObject(i);

            _id = item.getInt("id");
            name= item.getString("name");
            created_time = item.getString("description");
            cost = item.getDouble("cost");
            facebook_id = item.getString("facebook_id");
            house_id = item.getString("house_id");

            ContentValues spendingValues = new ContentValues();
            spendingValues.put(CommunicatorContract.SpendingEntry._ID, _id);
            spendingValues.put(CommunicatorContract.SpendingEntry.COLUMN_NAME, name);
            spendingValues.put(CommunicatorContract.SpendingEntry.COLUMN_CREATED_TIME, created_time);
            spendingValues.put(CommunicatorContract.SpendingEntry.COLUMN_COST, cost);
            spendingValues.put(CommunicatorContract.SpendingEntry.COLUMN_FACEBOOK_ID, facebook_id);
            spendingValues.put(CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID, house_id);

            spendingContentValues[i] = spendingValues;
        }

        return spendingContentValues;
    }

}
