package com.example.muhammet.communicator.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Muhammet on 12.12.2017.
 */

public class SpendingSyncIntentService extends IntentService {
    public SpendingSyncIntentService() {
        super("SpendingSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CommunicatorSyncTask.syncSpendings(this, intent.getStringExtra("user_id"), intent.getStringExtra("house_id"));
    }
}
