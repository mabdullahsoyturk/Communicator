package com.example.muhammet.communicator.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class CommunicatorSyncUtils {

    private static final int SYNC_INTERVAL_MINUTES = 1;
    private static final int SYNC_INTERVAL_HOURS = 168;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_INTERVAL_SECONDS_2 = (int)TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MINUTES);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS;
    private static final String COMMUNICATOR_SYNC_TAG = "communicator-sync";

    private static boolean sInitialized;

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(CommunicatorFirebaseJobService.class)
                .setTag(COMMUNICATOR_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_INTERVAL_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncSunshineJob);
    }

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;

        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);
    }

    public static void startImmediateSync(@NonNull final Context context, String action, String facebook_id, String house_id_server) {
        Intent intentToSyncImmediately = new Intent(context, CommunicatorSyncIntentService.class);
        intentToSyncImmediately.setAction(action);
        intentToSyncImmediately.putExtra("facebook_id", facebook_id);
        intentToSyncImmediately.putExtra("house_id_server", house_id_server);
        context.startService(intentToSyncImmediately);
    }
}
