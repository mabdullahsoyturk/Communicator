package com.example.muhammet.communicator.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Muhammet on 8.12.2017.
 */

public class CommunicatorSyncUtils {

    public static void startImmediateSync(@NonNull final Context context, String user_id, String house_id) {
//      COMPLETED (11) Within that method, start the SunshineSyncIntentService
        Intent intentToSyncImmediately = new Intent(context, CommunicatorSyncIntentService.class);
        intentToSyncImmediately.putExtra("user_id", user_id);
        intentToSyncImmediately.putExtra("house_id", house_id);
        context.startService(intentToSyncImmediately);
    }

}
