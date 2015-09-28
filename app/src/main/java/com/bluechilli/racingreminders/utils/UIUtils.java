package com.bluechilli.racingreminders.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by monishi on 23/02/15.
 */
public class UIUtils {

    public static void showToast(CharSequence message, int duration, Context context) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 400);
        toast.show();
    }
}
