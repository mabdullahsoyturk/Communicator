package com.example.muhammet.communicator.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Muhammet on 8.12.2017.
 */

public class CommunicatorSyncIntentService extends IntentService{

    public CommunicatorSyncIntentService() {
        super("CommunicatorSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CommunicatorSyncTask.syncBuyMes(this, intent.getStringExtra("user_id"), intent.getStringExtra("house_id"));
    }
}
