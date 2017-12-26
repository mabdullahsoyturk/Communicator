package com.example.muhammet.communicator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.muhammet.communicator.data.CommunicatorContract;

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent ıntent) {
        if(checkNetworkConnectivity(context)){
            context.sendBroadcast(new Intent(CommunicatorContract.UI_UPDATE_BROADCAST));
        }
    }

    public boolean checkNetworkConnectivity(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
