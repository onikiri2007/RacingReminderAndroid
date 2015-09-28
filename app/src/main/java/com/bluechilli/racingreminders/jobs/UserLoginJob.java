package com.bluechilli.racingreminders.jobs;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.events.UserStatusChangedEvent;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.stores.UserStore;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by monishi on 13/01/15.
 */
public class UserLoginJob extends Job {

    private User user;

    public UserLoginJob(User user) {
        super(new Params(1)
                .requireNetwork());

        this.user = user;
    }
    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        UserStore.getInstance().login(user, new Callback<User>() {
            @Override
            public void success(User user1, Response response) {
                user1.deviceId = user.deviceId;
                user1.token = user.token;
                EventBus.getDefault().post(new UserStatusChangedEvent(User.UserStatus.SignIn, user));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
