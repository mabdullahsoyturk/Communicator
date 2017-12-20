package com.example.muhammet.communicator.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Muhammet on 8.12.2017.
 */

public class BuyMeSyncIntentService extends IntentService{

    public BuyMeSyncIntentService() {
        super("BuyMeSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CommunicatorSyncTask.syncBuyMes(this, intent.getStringExtra("facebook_id"), intent.getStringExtra("house_id"));
    }
}
