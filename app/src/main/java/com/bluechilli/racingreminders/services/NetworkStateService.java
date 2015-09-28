package com.bluechilli.racingreminders.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bluechilli.racingreminders.events.NetworkStatusChangedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by monishi on 14/01/15.
 */
public class NetworkStateService extends BroadcastReceiver {

    public NetworkStateService() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new NetworkStatusChangedEvent(isOnline(context)));
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }
}
