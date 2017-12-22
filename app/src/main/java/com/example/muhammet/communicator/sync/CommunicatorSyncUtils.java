package com.example.muhammet.communicator.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class CommunicatorSyncUtils {

    public static void startImmediateSync(@NonNull final Context context, String action, String facebook_id, String house_id) {
        Intent intentToSyncImmediately = new Intent(context, CommunicatorSyncIntentService.class);
        intentToSyncImmediately.setAction(action);
        intentToSyncImmediately.putExtra("facebook_id", facebook_id);
        intentToSyncImmediately.putExtra("house_id", house_id);
        context.startService(intentToSyncImmediately);
    }

}
