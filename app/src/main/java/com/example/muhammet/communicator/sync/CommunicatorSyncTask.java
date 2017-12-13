package com.example.muhammet.communicator.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.URL;

public class CommunicatorSyncTask {

    synchronized public static void syncBuyMes(Context context, String user_id, String house_id) {

        try {
            String buyMeUrl = NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes";
            
            String jsonWeatherResponse = NetworkUtilities.getStringResponse(buyMeUrl);

            ContentValues[] buyMeValues = NetworkUtilities
                    .getBuyMeContentValuesFromJson(context, jsonWeatherResponse);

            if (buyMeValues != null && buyMeValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver communicatorContentResolver = context.getContentResolver();

//              COMPLETED (4) If we have valid results, delete the old data and insert the new
                /* Delete old weather data because we don't need to keep multiple days' data */
                communicatorContentResolver.delete(
                        CommunicatorContract.BuyMeEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                communicatorContentResolver.bulkInsert(
                        CommunicatorContract.BuyMeEntry.CONTENT_URI,
                        buyMeValues);
            }

            Log.i("Sync Worked", "Works");

            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

    synchronized public static void syncSpendings(Context context, String user_id, String house_id) {

        try {
            String spendingUrl = NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/spendings";

            String jsonWeatherResponse = NetworkUtilities.getStringResponse(spendingUrl);

            ContentValues[] spendingValues = NetworkUtilities
                    .getSpendingContentValuesFromJson(context, jsonWeatherResponse);

            if (spendingValues != null && spendingValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver communicatorContentResolver = context.getContentResolver();

//              COMPLETED (4) If we have valid results, delete the old data and insert the new
                /* Delete old weather data because we don't need to keep multiple days' data */
                communicatorContentResolver.delete(
                        CommunicatorContract.SpendingEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                communicatorContentResolver.bulkInsert(
                        CommunicatorContract.SpendingEntry.CONTENT_URI,
                        spendingValues);
            }

            Log.i("Sync Worked", "Works");

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

}
