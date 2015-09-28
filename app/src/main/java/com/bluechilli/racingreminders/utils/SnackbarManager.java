package com.bluechilli.racingreminders.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.R;

/**
 * Created by monishi on 1/07/15.
 */
public final class SnackbarManager {

    public static Snackbar make(View view, CharSequence message, int duration, int background, int textColor) {

        Snackbar bar = Snackbar.make(view ,message, duration);
        bar = setupSnackbar(bar, background, textColor);
        return bar;
    }

    public static Snackbar setupSnackbar(Snackbar bar, int background, int textColor) {
        View view = bar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(App.getInstance().getResources().getColor(textColor));
        view.setBackgroundColor(App.getInstance().getResources().getColor(background));
        return bar;
    }

}
