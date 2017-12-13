package com.example.muhammet.communicator.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class CommunicatorSyncUtils {

    public static void startImmediateSyncForBuyMes(@NonNull final Context context, String user_id, String house_id) {
        Intent intentToSyncImmediately = new Intent(context, BuyMeSyncIntentService.class);
        intentToSyncImmediately.putExtra("user_id", user_id);
        intentToSyncImmediately.putExtra("house_id", house_id);
        context.startService(intentToSyncImmediately);
    }

    public static void startImmediateSyncForSpendings(@NonNull final Context context, String user_id, String house_id) {
        Intent intentToSyncImmediately = new Intent(context, SpendingSyncIntentService.class);
        intentToSyncImmediately.putExtra("user_id", user_id);
        intentToSyncImmediately.putExtra("house_id", house_id);
        context.startService(intentToSyncImmediately);
    }

}
