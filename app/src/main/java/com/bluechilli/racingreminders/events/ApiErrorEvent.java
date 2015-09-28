package com.bluechilli.racingreminders.events;

import retrofit.RetrofitError;

/**
 * Created by monishi on 4/03/15.
 */
public class ApiErrorEvent {
    private final RetrofitError error;
    private final boolean canRetry;

    public ApiErrorEvent(RetrofitError error, boolean canRetry) {

        this.error = error;
        this.canRetry = canRetry;
    }

    public RetrofitError getError() {
        return error;
    }

    public boolean canRetry() {
        return canRetry;
    }
}
