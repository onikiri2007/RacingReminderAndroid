package com.bluechilli.racingreminders.utils;

import android.text.TextUtils;

/**
 * Created by monishi on 23/02/15.
 */
public class StringUtils {


    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value);
    }

    public static String toCamelCase(String value) {

        if(isEmpty(value)) {
            return value;
        }
        return String.format("%s%s", value.substring(0, 1).toUpperCase(), value.substring(1, value.length()).toLowerCase());
    }
}
