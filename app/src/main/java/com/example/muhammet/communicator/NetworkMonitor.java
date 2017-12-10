package com.example.muhammet.communicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Muhammet on 10.12.2017.
 */

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent ıntent) {
        if(checkNetworkConnectivity(context)){

        }
    }

    public boolean checkNetworkConnectivity(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
