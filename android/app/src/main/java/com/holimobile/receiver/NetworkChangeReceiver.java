package com.holimobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tmslibrary.utils.RxBus;
import com.holimobile.event.NetWorkRecoverEvent;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private boolean hasNetWork = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {

                if(!hasNetWork){
                    //从没网到有网
                    RxBus.getInstance().post(new NetWorkRecoverEvent());
                }
                hasNetWork = true;
            } else {
                hasNetWork = false;
            }
        }

    }
}
