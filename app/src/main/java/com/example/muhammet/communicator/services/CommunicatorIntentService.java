package com.example.muhammet.communicator.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class CommunicatorIntentService extends IntentService{

    public CommunicatorIntentService() {
        super("CommunicatorIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(action.equals(ServiceTasks.ACTION_ADD_BUY_ME)){
            Log.i("IntentService", "Worked");
            ServiceTasks.addBuyMe(
                    this,
                    intent.getStringExtra("name"),
                    intent.getStringExtra("description"),
                    intent.getStringExtra("facebook_id"),
                    intent.getStringExtra("house_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_MEMBER)){
            ServiceTasks.addMember(
                    this,
                    intent.getStringExtra("facebook_id"),
                    intent.getStringExtra("house_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_NEW_HUOSE)){
            ServiceTasks.addNewHouse(
                    this,
                    intent.getStringExtra("name"),
                    intent.getStringExtra("facebook_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_ADD_SPENDING)){
            ServiceTasks.addSpending(
                    this,
                    intent.getStringExtra("name"),
                    intent.getDoubleExtra("cost",0),
                    intent.getStringExtra("facebook_id"),
                    intent.getStringExtra("house_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_DELETE_ALL_BUY_MES)){

        }

        else if(action.equals(ServiceTasks.ACTION_DELETE_BUY_ME)){

        }

        else if(action.equals(ServiceTasks.ACTION_DELETE_SPENDING)){

        }

        else if(action.equals(ServiceTasks.ACTION_CHECK_USER)){
            ServiceTasks.checkUser(this,
                    intent.getStringExtra("first_name"),
                    intent.getStringExtra("last_name"),
                    intent.getStringExtra("photo_url"),
                    intent.getStringExtra("facebook_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_CHECK_HOUSES)){
            ServiceTasks.checkHouses(this,
                    intent.getStringExtra("invitation_code"),
                    intent.getStringExtra("facebook_id")
            );
        }

        else if(action.equals(ServiceTasks.ACTION_UPDATE_USER)){
            ServiceTasks.updateUser(this,
                    intent.getStringExtra("facebook_id"),
                    intent.getStringExtra("house_id")
            );
        }
    }
}
