package com.bluechilli.racingreminders.utils;

import android.text.TextUtils;

import com.bluechilli.racingreminders.App;
import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.stores.UserStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public final class ApiAdapter {

    public static <T> T create(String endpoint, Class<T> serviceClass ) {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies( new SugarExclusionStrategy() )
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("ApiKey", Constants.API_KEY);
                        request.addQueryParam("ApiKey", Constants.API_KEY);
                        User user = UserStore.getInstance().getUser();
                        if(user != null && !TextUtils.isEmpty(user.userKey)) {
                            request.addQueryParam("apiUserKey", user.userKey);
                        }
                    }
                })
                .setEndpoint(endpoint).setConverter(new GsonConverter(gson))
                .setErrorHandler(new ApiErrorHandler(App.getInstance().getApplicationContext()))
                .build();
        return restAdapter.create( serviceClass );
    }
}
