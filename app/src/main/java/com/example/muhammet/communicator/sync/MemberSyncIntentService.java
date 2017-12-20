package com.example.muhammet.communicator.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Muhammet on 20.12.2017.
 */

public class MemberSyncIntentService extends IntentService {


    public MemberSyncIntentService() {
        super("MemberSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CommunicatorSyncTask.syncMembers(this, intent.getStringExtra("facebook_id"), intent.getStringExtra("house_id"));
    }
}
