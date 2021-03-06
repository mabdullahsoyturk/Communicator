package com.example.muhammet.communicator.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.fragments.BuyMeFragment;
import com.example.muhammet.communicator.utilities.NetworkUtilities;
import com.example.muhammet.communicator.utilities.NotificationUtilities;

public class CommunicatorSyncTask {

    public static final String ACTION_UPDATE_BUY_MES = "update-buy-mes";
    public static final String ACTION_UPDATE_SPENDINGS = "update-spendings";
    public static final String ACTION_UPDATE_MEMBERS = "update-members";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_CHECK_BUY_MES = "check-buymes";
    public static final String ACTION_CHECK_SPENDINGS = "check-spendings";
    public static final String ACTION_SHOP_DAY = "shop-day";


    public static void syncTask(Context context, String action, String facebook_id, String house_id_server){

        if(ACTION_UPDATE_BUY_MES.equals(action)){
            syncBuyMes(context, facebook_id, house_id_server);
        }

        else if(ACTION_UPDATE_MEMBERS.equals(action)){
            syncMembers(context, facebook_id, house_id_server);
        }

        else if(ACTION_UPDATE_SPENDINGS.equals(action)){
            syncSpendings(context, facebook_id, house_id_server);
        }

        else if(ACTION_DISMISS_NOTIFICATION.equals(action)){
            NotificationUtilities.clearAllNotifications(context);
        }

        else if(ACTION_CHECK_BUY_MES.equals(action)){
            Intent intent = new Intent(context, BaseActivity.class);
            intent.putExtra("facebook_id", facebook_id);
            intent.putExtra("house_id_server", house_id_server);
            intent.putExtra("fragment", "buy_me_fragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        else if(ACTION_CHECK_SPENDINGS.equals(action)){
            Intent intent = new Intent(context, BaseActivity.class);
            intent.putExtra("facebook_id", facebook_id);
            intent.putExtra("house_id_server", house_id_server);
            intent.putExtra("fragment", "spending_fragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        else if(ACTION_SHOP_DAY.equals(action)){
            NotificationUtilities.remindUserForShoppingDay(context);
        }
    }

    synchronized public static void syncBuyMes(Context context, String facebook_id, String house_id_server) {

        try {
            String buyMeUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/buy_mes";
            
            String jsonWeatherResponse = NetworkUtilities.getStringResponse(buyMeUrl);

            ContentValues[] buyMeValues = NetworkUtilities
                    .getBuyMeContentValuesFromJson(context, jsonWeatherResponse);

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

    synchronized public static void syncSpendings(Context context, String facebook_id, String house_id_server) {

        try {
            String spendingUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/spendings";

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

    synchronized public static void syncMembers(Context context, String facebook_id, String house_id_server) {

        try {
            String memberUrl = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/members";

            String jsonWeatherResponse = NetworkUtilities.getStringResponse(memberUrl);

            ContentValues[] memberValues = NetworkUtilities
                    .getMemberContentValuesFromJson(context, jsonWeatherResponse);

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
            }
            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }

}
