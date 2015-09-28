package com.bluechilli.racingreminders.stores;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.Entity;
import com.bluechilli.racingreminders.models.MessageResult;
import com.bluechilli.racingreminders.models.PagedList;
import com.bluechilli.racingreminders.models.Product;
import com.bluechilli.racingreminders.models.SearchCriteria;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.services.EntityService;
import com.bluechilli.racingreminders.utils.ApiAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by monishi on 16/06/15.
 */
public final class EntityStore extends BaseStore {

    private static EntityStore ourInstance = new EntityStore();

    public static EntityStore getInstance() {
        return ourInstance;
    }

    private User user;
    private EntityService entityService;

    private EntityStore() {
        entityService = ApiAdapter.create(Constants.getApiURL(), EntityService.class);

    }

    public Collection<Entity> searchEntities(SearchCriteria criteria) {

        try
        {
            PagedList<Collection<Entity>> list = entityService.searchEntities(criteria.searchText, criteria.page, criteria.pageSize);

            for(Entity entity : list.data) {
                List<Entity> existings = Entity.find(Entity.class , "external_id = ?", String.format("%s", entity.externalId));
                Entity e;

                if(existings.size() > 0 ) {
                    e = existings.get(0);
                    entity.setId(e.getId());
                    entity.following = e.following;
                }

            }

            Entity.saveInTx(list.data);

            return list.data;
        }
        catch(RetrofitError error) {
            Log.d(Constants.TAG, error.getLocalizedMessage());
        }

        return null;
    }

    public void searchEntities(SearchCriteria criteria , final Callback<PagedList<Collection<Entity>>> callback) {

       Collection<Entity> entities;

       if(!TextUtils.isEmpty(criteria.searchText)) {
          entities = Entity.findWithQuery(Entity.class, "SELECT * FROM ENTITY WHERE name like ? LIMIT ? OFFSET ?", criteria.searchText, String.format("%d", criteria.pageSize), String.format("%d", criteria.page * criteria.pageSize));
       }
       else {
          entities = Entity.findWithQuery(Entity.class, "SELECT * FROM ENTITY LIMIT ? OFFSET ?", String.format("%d", criteria.pageSize), String.format("%d", criteria.page * criteria.pageSize));
       }

        if(entities != null && entities.size() > 0) {
            PagedList<Collection<Entity>> list = new PagedList<>();
            list.data = entities;
            list.currentPage = criteria.page;
            list.pageSize = criteria.pageSize;
            callback.success(list, null);
        }

       entityService.searchEntities(criteria.searchText, criteria.page, criteria.pageSize, new Callback<PagedList<Collection<Entity>>>() {
           @Override
           public void success(PagedList<Collection<Entity>> entities, Response response) {

               AsyncTask<PagedList<Collection<Entity>>, Void, PagedList<Collection<Entity>>> task = new AsyncTask<PagedList<Collection<Entity>>, Void, PagedList<Collection<Entity>>>() {
                   @Override
                   protected PagedList<Collection<Entity>> doInBackground(PagedList<Collection<Entity>>... params) {
                       PagedList<Collection<Entity>> list = params[0];
                       Collection<Entity> r = list.data;

                      for(Entity entity : r) {
                           List<Entity> existings = Entity.find(Entity.class , "external_id = ?", String.format("%s", entity.externalId));
                           Entity e;

                           if(existings.size() > 0 ) {
                               e = existings.get(0);
                               entity.setId(e.getId());
                               entity.following = e.following;
                           }

                       }

                       Entity.saveInTx(r);

                       return list;
                   }

                   @Override
                   protected void onPostExecute(PagedList<Collection<Entity>> list) {
                       callback.success(list, null);
                   }
               };


               task.execute(entities);

               callback.success(entities, response);
           }

           @Override
           public void failure(RetrofitError error) {
                callback.failure(error);
           }
       });
    }

    public Collection<Entity> getFollowingEntities(SearchCriteria criteria) {
        PagedList<Collection<Entity>> list = entityService.getFollowingEntities(criteria.page, criteria.pageSize);
        return list.data;
    }

    public void getFollowingEntities(SearchCriteria criteria , final Callback<PagedList<Collection<Entity>>> callback) {
        entityService.getFollowingEntities(criteria.page, criteria.pageSize, new Callback<PagedList<Collection<Entity>>>() {
            @Override
            public void success(final PagedList<Collection<Entity>> entities, Response response) {

                for(Entity entity : entities.data) {
                    entity.following = true;
                }

                AsyncTask<Collection<Entity>, Void, Void> task = new AsyncTask<Collection<Entity>, Void, Void>() {
                    @Override
                    protected Void doInBackground(Collection<Entity>... params) {
                        Collection<Entity> r = params[0];

                        Collection<Entity> entities1 = new ArrayList<Entity>();

                        for(Entity entity : r) {
                            List<Entity> existings = Entity.find(Entity.class , "external_id = ?", String.format("%s", entity.externalId));
                            Entity e;

                            if(existings.size() > 0 ) {
                                e = existings.get(0);
                            }
                            else {
                                e = entity;
                            }

                            e.following = true;
                            entities1.add(e);
                        }

                        Entity.saveInTx(entities1);
                        return null;
                    }
                };


                task.execute(entities.data);

                callback.success(entities, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public void follow(Entity entity, Callback<MessageResult> callback) {
        entityService.follow(entity, callback);
    }

    public void getProducts(Callback<Collection<Product>> callback) {
        entityService.getProducts(callback);
    }


}
