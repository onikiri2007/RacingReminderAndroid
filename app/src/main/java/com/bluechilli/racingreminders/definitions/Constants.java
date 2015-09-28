package com.bluechilli.racingreminders.definitions;

import com.bluechilli.racingreminders.BuildConfig;

/**
 * Created by monishi on 23/12/2014.
 */
public class Constants {

    public static final String PREFERENCE_DATA = "com.bluechilli.racingreminders.data";
    public static final String USER_DATA = "USER";
    public static final String API_URL = "https://local.bluechilli.com/RacingReminders/api/v1";
    public static final String API_STAGING_URL = "https://dev.bluechilli.com/racingreminders/api";
    public static final String API_PRODUCTION_URL = "https://www.racingreminders.com/api";
    public static final String API_KEY = "62a09ea7-e6c6-420b-8db6-139ad68d4d31";
    public static final String API_SALT = "NJrusrzPRkijVS6fz6mIZw";
    public static final int DEFAULT_FADE_IN_OUT_ANIMATION_DURATION = 200;
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String NOTIFICATION_DATA = "NOTIFICATION";
    public static final String TAG = "com.bluechilli.rr";
    public static final String ACTION_BAR_TITLE = "ACTION_BAR_TITLE";
    public static final int DEFAULT_PAGE_SIZE = 50;

    public static String getApiURL() {
        if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            return API_URL;
        }
        else if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("staging")) {
            return API_STAGING_URL;
        }
        else {
            return API_PRODUCTION_URL;
        }
    }


}

