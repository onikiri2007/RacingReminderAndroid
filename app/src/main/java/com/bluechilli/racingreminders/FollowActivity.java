package com.bluechilli.racingreminders;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.bluechilli.racingreminders.commands.SearchCommand;
import com.bluechilli.racingreminders.events.EntitySelectedEvent;
import com.bluechilli.racingreminders.models.Entity;

import java.util.Collection;

import de.greenrobot.event.EventBus;


public class FollowActivity extends BaseActivity {

    ViewPager pager;
    TabLayout tabs;
    Menu menu;
    @Override
    protected int baseLayout() {
        return R.layout.activity_follow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setHomeButtonEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        setupPager();
    }


    private void setupPager() {
        pager.setAdapter(new EntityPager(getSupportFragmentManager()));
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_follow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }




    public class EntityPager extends FragmentPagerAdapter {


        public EntityPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return FollowedEntityFragment.newInstance();
                default:
                    return SearchEntityFragment.newInstance();

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return FollowActivity.this.getString(R.string.follow_tab_title_1);
                default:
                    return FollowActivity.this.getString(R.string.follow_tab_title);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
