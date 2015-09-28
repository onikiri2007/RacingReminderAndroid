package com.bluechilli.racingreminders.stores;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.events.UserStatusChangedEvent;
import com.bluechilli.racingreminders.interfaces.IContext;
import com.bluechilli.racingreminders.jobs.UserLoginJob;
import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.services.AccountService;
import com.bluechilli.racingreminders.utils.ApiAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by monishi on 9/01/15.
 */


public final class UserStore extends BaseStore {


    private static UserStore ourInstance = new UserStore();

    public static UserStore getInstance() {
        return ourInstance;
    }

    private User user;
    private AccountService accountService;
    private Object lock = new Object();

    private UserStore() {
        accountService = ApiAdapter.create(Constants.getApiURL(), AccountService.class);

    }

    public String getToken() {
        final SharedPreferences preferences = getNotificationPreferences();
        String registrationId = preferences.getString(Constants.PROPERTY_REG_ID, "");

        if(TextUtils.isEmpty(registrationId)) {
            Log.d(Constants.TAG, "registrationId was not found");
            return "";
        }
        else {
            if(isAppVersionChanged()) {
                Log.d(Constants.TAG, "App version has changed");
                return "";
            }
        }

        return registrationId;
    }

    public boolean isAppVersionChanged() {
        boolean result = false;
        final SharedPreferences pref = getNotificationPreferences();
        int version = pref.getInt(Constants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = App.getAppVersion(App.getInstance());

        if(currentVersion != version) {
            result = true;
        }

        return result;
    }

    public User getUser() {

        if(user != null) {
            return user;
        }

        final SharedPreferences preferences = getPreferences();
        return  getUserFromPreferences(preferences);
    }

    private SharedPreferences getPreferences() {
        return getContext().context().getSharedPreferences(Constants.PREFERENCE_DATA, Context.MODE_PRIVATE);
    }

    private void setUser(User user) {
        synchronized (lock) {
            this.user = user;
            final SharedPreferences preferences = getPreferences();
            saveUserToPreferences(preferences, user);
        }
    }



    private User getUserFromPreferences(SharedPreferences preferences) {

        try {
            String s =  preferences.getString(Constants.USER_DATA, null);
            Gson gson = new Gson();

            if(!TextUtils.isEmpty(s)) {
                User u = gson.fromJson(s , User.class);
                return u;
            }
        }
        catch (JsonSyntaxException ex) {

        }


        return null;
    }


    private void saveUserToPreferences(final SharedPreferences preferences, final User user) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString(Constants.USER_DATA, gson.toJson(user));
                edit.commit();
            }
        });

        thread.start();
    }


    public void login(final User user, final Callback<User> callback) {

        User user1 = this.getUser();

        if(user1 == null) {
            accountService.registerNotification(user.token, user.deviceId, "Google", new Callback<User>() {
                @Override
                public void success(User user2, Response response) {
                    user2.token = user.token;
                    user2.deviceId = user.deviceId;
                    EventBus.getDefault().post(new UserStatusChangedEvent(User.UserStatus.SignIn, user2));
                    callback.success(user2, response);
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.failure(error);
                }
            });
        }
        else {
            callback.success(user1, null);
        }
    }



    public void onEvent(UserStatusChangedEvent event) {
        this.user = event.getUser();
        switch(event.getStatus()) {
            case SignIn:
                setUser(user);
                setToken(event.getUser().token);
            default:
                break;
        }

    }


    @Override
    public void start(IContext context) {
        super.start(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
        super.stop();
    }


    public SharedPreferences getNotificationPreferences() {
        return getPreferences();
    }

    public void setToken(String token) {
        final SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Constants.PROPERTY_REG_ID, token);
        int currentVersion = App.getAppVersion(App.getInstance());
        edit.putInt(Constants.PROPERTY_APP_VERSION, currentVersion);
        edit.commit();
    }
}
