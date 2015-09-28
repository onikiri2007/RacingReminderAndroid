package com.bluechilli.racingreminders.utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by monishi on 6/02/15.
 */
public class CancellableCallback<T> implements Callback<T> {

    private final Callback<T> callback;
    private boolean isCancelled = false;

    public CancellableCallback(Callback<T> callback) {

        this.callback = callback;
    }


    @Override
    public void success(T t, Response response) {
        if(!this.isCancelled) {
            this.callback.success(t, response);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        if(!this.isCancelled) {
            this.callback.failure(error);
        }
    }

    public void cancel() {
        this.isCancelled = true;
    }
}
