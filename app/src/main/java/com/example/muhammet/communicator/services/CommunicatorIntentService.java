package com.example.muhammet.communicator.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class CommunicatorIntentService extends IntentService{

    public CommunicatorIntentService() {
        super("CommunicatorIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        if(action.equals(ServiceTasks.ACTION_ADD_BUY_ME)){
            ServiceTasks.addBuyMe(this, intent.getStringExtra("name"), intent.getStringExtra("description"),intent.getStringExtra("facebook_id"), intent.getStringExtra("house_id"));
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_MEMBER)){
            ServiceTasks.addMember(this, intent.getStringExtra("facebook_id"), intent.getStringExtra("house_id"));
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_NEW_HUOSE)){
            ServiceTasks.addNewHouse(this, intent.getStringExtra("name"), intent.getStringExtra("facebook_id"));
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_SPENDING)){
            ServiceTasks.addSpending(this, intent.getStringExtra("name"), intent.getDoubleExtra("cost",0), intent.getStringExtra("facebook_id"), intent.getStringExtra("house_id"));
        }
    }
}
