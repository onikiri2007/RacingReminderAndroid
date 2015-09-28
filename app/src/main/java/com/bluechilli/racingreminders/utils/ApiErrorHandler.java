package com.bluechilli.racingreminders.utils;

import android.content.Context;

import com.bluechilli.racingreminders.R;
import com.bluechilli.racingreminders.managers.NetworkManager;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by monishi on 14/01/15.
 */
public class ApiErrorHandler implements ErrorHandler {

    private final Context context;

    class ApiError {
        public String errorMessage;
    }

    public ApiErrorHandler(Context context) {

        this.context = context;
    }
    @Override
    public Throwable handleError(RetrofitError cause) {

        String errorMessage;

        if(cause.getKind() == RetrofitError.Kind.NETWORK || !NetworkManager.getInstance().hasNetworkConnection()) {
            errorMessage = context.getResources().getString(R.string.network_error);
        }
        else {
            if(cause.getResponse() == null) {
                errorMessage = context.getResources().getString(R.string.generic_error);
            }
            else if(cause.getResponse().getStatus() == 404) {
                errorMessage = context.getResources().getString(R.string.no_end_point_error);
            }
            else if(cause.getResponse().getStatus() == 401 || cause.getResponse().getStatus() == 403) {
                errorMessage = context.getResources().getString(R.string.unauthorized_error);
            }
            else {

                try {
                    ApiError error = (ApiError) cause.getBodyAs(ApiError.class);
                    errorMessage = error.errorMessage;
                } catch(Exception ex) {
                    ex.printStackTrace();

                    if(cause.getResponse().getStatus() == 401) {
                        errorMessage = context.getResources().getString(R.string.unauthorized_error);
                    }
                    else {
                        errorMessage = context.getResources().getString(R.string.generic_error);
                    }

                }
            }
        }

        return new Exception(errorMessage, cause);
    }
}
