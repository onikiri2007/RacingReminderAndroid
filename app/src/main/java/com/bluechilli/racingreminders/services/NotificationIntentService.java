package com.bluechilli.racingreminders.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by monishi on 20/02/15.
 */
public class NotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    static String MESSAGE_DATA = "message";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if(messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {

            String message = bundle.getString(MESSAGE_DATA);
            sendNotification(message);
        }

        NotificationReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String message) {

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        /*PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, MyNumberActivity.class), 0);

        builder = new NotificationCompat.Builder(this)
                 .setSmallIcon(R.drawable.ic_launcher)
                 .setAutoCancel(true)
                 .setDefaults(0)
                 .setContentTitle(getResources().getString(R.string.app_name))
                 .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);

        builder.setContentIntent(intent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());*/
    }
}
