package com.bluechilli.racingreminders.services;

import com.bluechilli.racingreminders.models.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by monishi on 13/01/15.
 */
public interface AccountService {

    @FormUrlEncoded()
    @POST("/me/registerNewPushToken")
    void registerNotification(@Field("mobileToken") String token, @Field("deviceId") String deviceId, @Field("provider") String type, Callback<User> callback);

    @GET("/me/{apiUserKey}")
    void getAccountInfo(@Path("apiUserKey") String userKey,  Callback<User> callback);

    @POST("/me/update")
    void updateAccountInfo(@Body User user, Callback<User> callback);

}

