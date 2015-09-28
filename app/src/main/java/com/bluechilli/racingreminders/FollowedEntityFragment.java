package com.bluechilli.racingreminders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bluechilli.racingreminders.adapters.EntityAdapter;
import com.bluechilli.racingreminders.models.Entity;
import com.bluechilli.racingreminders.models.MessageResult;
import com.bluechilli.racingreminders.models.SearchCriteria;
import com.bluechilli.racingreminders.stores.EntityStore;
import com.bluechilli.racingreminders.stores.UserStore;
import com.commonsware.cwac.endless.EndlessAdapter;

import java.util.ArrayList;
import java.util.Collection;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowedEntityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowedEntityFragment extends BaseListFragment {

    EntityEndlessAdapter endlessAdapter;
    EntityAdapter adapter;
    View footerView;

    public static FollowedEntityFragment newInstance() {
        FollowedEntityFragment fragment = new FollowedEntityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FollowedEntityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.entity_list, container, false);

        adapter = new EntityAdapter(getActivity(), R.layout.entity_list_item, new ArrayList<Entity>());
        footerView = view.findViewById(R.id.footer);
        endlessAdapter = new EntityEndlessAdapter(new EntityAdapter(getActivity(), R.layout.entity_list_item, new ArrayList<Entity>()));
        setListAdapter(endlessAdapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Entity entity = (Entity) endlessAdapter.getItem(position);

        if(entity != null) {
            if(entity.following) {
                entity.following = false;
                entity.save();
                adapter.remove(entity);
                endlessAdapter.notifyDataSetChanged();
            }

            entity.apiUserKey = UserStore.getInstance().getUser().userKey;
            EntityStore.getInstance().follow(entity, new Callback<MessageResult>() {
                @Override
                public void success(MessageResult messageResult, Response response) {
                    endlessAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    class EntityEndlessAdapter extends EndlessAdapter {
        Collection<Entity> entities;
        View pendingView;

        public EntityEndlessAdapter(ListAdapter wrapped) {
            super(wrapped);
            setSerialized(true);
        }

        @Override
        protected boolean cacheInBackground() throws Exception {
            SearchCriteria criteria = ((EntityAdapter) getWrappedAdapter()).getCritera();
            entities = EntityStore.getInstance().getFollowingEntities(new SearchCriteria(criteria.searchText, criteria.page));
            return true;
        }

        @Override
        protected void appendCachedData() {

            if(entities != null && entities.size() > 0) {
                EntityAdapter adapter = ((EntityAdapter) getWrappedAdapter());
                ((EntityAdapter) getWrappedAdapter()).getCritera().page++;
                adapter.addAll(entities);
            }

        }


        @Override
        public void onDataReady() {
            super.onDataReady();
            android.os.Handler handler = new android.os.Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    footerView.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }

            }, 4);


        }

        @Override
        protected View getPendingView(ViewGroup parent) {

            if(pendingView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                pendingView = inflater.inflate(R.layout.pending_list_item, parent, false);
            }

            pendingView.setVisibility(View.GONE);
            footerView.setVisibility(View.VISIBLE);
            return pendingView;

        }

    }
}
