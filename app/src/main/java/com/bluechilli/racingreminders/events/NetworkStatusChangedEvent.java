package com.bluechilli.racingreminders.events;

/**
 * Created by monishi on 14/01/15.
 */
public class NetworkStatusChangedEvent {

    private boolean isConnected;

    public NetworkStatusChangedEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
