package com.bluechilli.racingreminders.configs;

import com.bluechilli.racingreminders.interfaces.IContext;
import com.bluechilli.racingreminders.managers.NetworkManager;
import com.bluechilli.racingreminders.stores.UserStore;

/**
 * Created by monishi on 4/03/15.
 */
public final class StoreConfigurator {


    public static void startStores(IContext context) {
        UserStore.getInstance().start(context);
        NetworkManager.getInstance().registerEvents();
    }

    public static void stopStores() {
        UserStore.getInstance().stop();
        NetworkManager.getInstance().unregisterEvents();
    }
}
