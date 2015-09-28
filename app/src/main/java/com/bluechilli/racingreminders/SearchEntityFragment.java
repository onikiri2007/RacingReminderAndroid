package com.bluechilli.racingreminders;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchEntityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchEntityFragment extends BaseListFragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match

    EntityEndlessAdapter endlessAdapter;
    EntityAdapter adapter;
    View footerView;

    public static SearchEntityFragment newInstance() {
        SearchEntityFragment fragment = new SearchEntityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchEntityFragment() {
        // Required empty public constructor


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.entity_list, container, false);
        adapter = new EntityAdapter(getActivity(), R.layout.entity_list_item, new ArrayList<Entity>());
        endlessAdapter = new EntityEndlessAdapter(new EntityAdapter(getActivity(), R.layout.entity_list_item, new ArrayList<Entity>()));
        footerView = view.findViewById(R.id.footer);
        setListAdapter(endlessAdapter);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       final Entity entity = (Entity) endlessAdapter.getItem(position);

        if(entity != null) {
            if(entity.following) {
                entity.following = false;
            }
            else {
                entity.following = true;
            }
            entity.save();

            entity.apiUserKey = UserStore.getInstance().getUser().userKey;
            EntityStore.getInstance().follow(entity, new Callback<MessageResult>() {
                @Override
                public void success(MessageResult messageResult, Response response) {
                    endlessAdapter.notifyDataSetChanged();
                 }

                @Override
                public void failure(RetrofitError error) {
                    if(entity.following) {
                        entity.following = true;
                    }
                    else {
                        entity.following = false;
                    }
                    entity.save();
                    endlessAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_fragment_follow, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(((BaseActivity)getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, searchView);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if(TextUtils.isEmpty(s) || s.length() < 3) {
            getListView().clearTextFilter();
        }
        else {
            getListView().setFilterText(s);
        }

        return true;
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
            entities = EntityStore.getInstance().searchEntities(new SearchCriteria(criteria.searchText, criteria.page));
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
