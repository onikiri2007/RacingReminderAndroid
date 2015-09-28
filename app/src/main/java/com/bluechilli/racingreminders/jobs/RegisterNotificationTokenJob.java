package com.bluechilli.racingreminders.jobs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.events.UserStatusChangedEvent;
import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.services.AccountService;
import com.bluechilli.racingreminders.utils.ApiAdapter;
import com.bluechilli.racingreminders.utils.Installation;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by monishi on 23/02/15.
 */
public class RegisterNotificationTokenJob extends Job {

    private final String registrationId;

    public RegisterNotificationTokenJob(String registrationId) {
        super(new Params(1));
        this.registrationId = registrationId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        AccountService service = ApiAdapter.create(Constants.getApiURL(), AccountService.class);

        service.registerNotification(registrationId , Installation.getUniqueId(App.getInstance().getApplicationContext()), "Google", new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                final SharedPreferences pref = App.getInstance().getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PROPERTY_REG_ID, registrationId);
                editor.putInt(Constants.PROPERTY_APP_VERSION, App.getAppVersion(App.getInstance().getApplicationContext()));
                editor.commit();
                EventBus.getDefault().post(new UserStatusChangedEvent(User.UserStatus.SignIn, user));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(Constants.TAG, error.getLocalizedMessage());
            }
        });

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }
}
