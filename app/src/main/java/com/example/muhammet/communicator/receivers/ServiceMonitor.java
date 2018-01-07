package com.example.muhammet.communicator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.muhammet.communicator.data.CommunicatorContract;

public class ServiceMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent ıntent) {
        context.sendBroadcast(new Intent(CommunicatorContract.SERVICE_FINISHED_BROADCAST));
    }
}
