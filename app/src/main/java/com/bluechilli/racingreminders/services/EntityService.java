package com.bluechilli.racingreminders.services;

import com.bluechilli.racingreminders.models.Entity;
import com.bluechilli.racingreminders.models.MessageResult;
import com.bluechilli.racingreminders.models.PagedList;
import com.bluechilli.racingreminders.models.Product;

import java.util.Collection;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by monishi on 29/06/15.
 */
public interface EntityService {


    @GET("/product/all")
    void getProducts(Callback<Collection<Product>> callback);

    @GET("/entities/search")
    PagedList<Collection<Entity>> searchEntities(@Query("text") String searchTerm, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("/entities/search")
    void searchEntities(@Query("text") String searchTerm, @Query("page") int page, @Query("pageSize") int pageSize,  Callback<PagedList<Collection<Entity>>> callback);

    @GET("/entities/following")
    void getFollowingEntities(@Query("page") int page, @Query("pageSize") int pageSize,  Callback<PagedList<Collection<Entity>>> callback);

    @GET("/entities/following")
    PagedList<Collection<Entity>> getFollowingEntities(@Query("page") int page, @Query("pageSize") int pageSize);

    @POST("/entities/follow")
    void follow(@Body Entity entity, Callback<MessageResult> callback);

}
