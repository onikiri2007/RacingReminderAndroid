package com.bluechilli.racingreminders.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.events.NetworkStatusChangedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by monishi on 14/01/15.
 */
public class NetworkManager {
    private static NetworkManager ourInstance = new NetworkManager();
    private boolean hasNetworkConnection;

    public synchronized static NetworkManager getInstance() {
        return ourInstance;
    }

    private NetworkManager() {


    }

    public void registerEvents() {
        EventBus.getDefault().register(this);
    }

    public void unregisterEvents() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(NetworkStatusChangedEvent event) {
        hasNetworkConnection = event.isConnected();
    }

    public boolean hasNetworkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null) {
            return netInfo.isConnected();
        }
        else {
            return this.hasNetworkConnection;
        }
    }

}
