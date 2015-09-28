package com.bluechilli.racingreminders.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by monishi on 20/02/15.
 */
public class NotificationReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentName component = new ComponentName(context.getPackageName(), NotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(component)));
        setResultCode(Activity.RESULT_OK);
    }
}
