package com.example.muhammet.communicator.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

public class CommunicatorSyncTask {

    public static final String ACTION_UPDATE_BUY_MES = "update-buy-mes";
    public static final String ACTION_UPDATE_SPENDINGS = "update-spendings";
    public static final String ACTION_UPDATE_MEMBERS = "update-members";

    public static void syncTask(Context context, String action, String facebook_id, String house_id){

        if(ACTION_UPDATE_BUY_MES.equals(action)){
            syncBuyMes(context, facebook_id, house_id);
        }

        if(ACTION_UPDATE_MEMBERS.equals(action)){
            syncMembers(context, facebook_id, house_id);
        }

        if(ACTION_UPDATE_SPENDINGS.equals(action)){
            syncSpendings(context, facebook_id, house_id);
        }
    }

    synchronized public static void syncBuyMes(Context context, String facebook_id, String house_id) {

        try {
            String buyMeUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/buy_mes";
            
            String jsonWeatherResponse = NetworkUtilities.getStringResponse(buyMeUrl);

            ContentValues[] buyMeValues = NetworkUtilities
                    .getBuyMeContentValuesFromJson(context, jsonWeatherResponse);

            Log.i("buyMeValues", "" + buyMeValues.length);

            if (buyMeValues != null) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver communicatorContentResolver = context.getContentResolver();

                communicatorContentResolver.delete(
                        CommunicatorContract.BuyMeEntry.CONTENT_URI,
                        null,
                        null);

                communicatorContentResolver.bulkInsert(
                        CommunicatorContract.BuyMeEntry.CONTENT_URI,
                        buyMeValues);
            }
            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

    synchronized public static void syncSpendings(Context context, String facebook_id, String house_id) {

        try {
            String spendingUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/spendings";

            String jsonWeatherResponse = NetworkUtilities.getStringResponse(spendingUrl);

            ContentValues[] spendingValues = NetworkUtilities
                    .getSpendingContentValuesFromJson(context, jsonWeatherResponse);

            if (spendingValues != null) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver communicatorContentResolver = context.getContentResolver();

                communicatorContentResolver.delete(
                        CommunicatorContract.SpendingEntry.CONTENT_URI,
                        null,
                        null);

                communicatorContentResolver.bulkInsert(
                        CommunicatorContract.SpendingEntry.CONTENT_URI,
                        spendingValues);
            }

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

    synchronized public static void syncMembers(Context context, String facebook_id, String house_id) {

        try {
            String memberUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/members";

            String jsonWeatherResponse = NetworkUtilities.getStringResponse(memberUrl);

            ContentValues[] memberValues = NetworkUtilities
                    .getMemberContentValuesFromJson(context, jsonWeatherResponse);

            Log.i("memberValues", "" + memberValues.length);

            if (memberValues != null) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver communicatorContentResolver = context.getContentResolver();

                communicatorContentResolver.delete(
                        CommunicatorContract.UserEntry.CONTENT_URI,
                        null,
                        null);

                communicatorContentResolver.bulkInsert(
                        CommunicatorContract.UserEntry.CONTENT_URI,
                        memberValues);

                Log.i("Sync Worked", "Works");
            }
            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

}
