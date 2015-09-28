package com.bluechilli.racingreminders;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bluechilli.racingreminders.commands.BackMenuCommand;
import com.bluechilli.racingreminders.commands.ChangeActionBarTitleCommand;
import com.bluechilli.racingreminders.commands.DrawerMenuCommand;
import com.bluechilli.racingreminders.commands.LoadingCommand;
import com.bluechilli.racingreminders.commands.MenuCommand;
import com.bluechilli.racingreminders.commands.RetryCommand;
import com.bluechilli.racingreminders.commands.RetryRequestCommand;
import com.bluechilli.racingreminders.events.UserStatusChangedEvent;
import com.bluechilli.racingreminders.interfaces.IHandleLoadingIndicator;
import com.bluechilli.racingreminders.interfaces.IHandleMenu;
import com.bluechilli.racingreminders.interfaces.IHandleRetry;
import com.bluechilli.racingreminders.interfaces.RetryListener;
import com.bluechilli.racingreminders.managers.NetworkManager;
import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.stores.UserStore;
import com.bluechilli.racingreminders.utils.SnackbarManager;

import de.greenrobot.event.EventBus;


public class BaseActivity extends AppCompatActivity implements IHandleLoadingIndicator, IHandleMenu, IHandleRetry, NavigationView.OnNavigationItemSelectedListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    protected DrawerLayout drawerLayout;
    private User user;
    private String name = "";
    private View loadingView;
    protected Menu menu;
    protected boolean enableMenuItem;
    private RetryListener retryListener;
    protected Toolbar toolbar;
    protected NavigationView drawerView;
    protected ImageButton retryButton;
    protected CoordinatorLayout coordinatorLayout;

    protected int baseLayout() {
        return R.layout.app_layout;
    }

    protected boolean isApp() {
        User user = UserStore.getInstance().getUser();

        if(user == null) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(baseLayout());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        //    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setHomeButtonEnabled(true);
        }
        loadingView = findViewById(R.id.loadingView);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_frame);
    }


/*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void Logout() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showLoading(int colorResourceId) {

        final int c = colorResourceId;

        loadingView.animate().alpha(1f).setDuration(Constants.DEFAULT_FADE_IN_OUT_ANIMATION_DURATION).withStartAction(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.VISIBLE);
                loadingView.setBackgroundResource(c);
            }
        });

    }

    @Override
    public void hideLoading() {

        loadingView.animate().alpha(0f).setDuration(Constants.DEFAULT_FADE_IN_OUT_ANIMATION_DURATION).withEndAction(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void enableBack(boolean enabled)
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        getSupportActionBar().setHomeButtonEnabled(enabled);
    }

    @Override
    public void enableMenu(boolean enabled) {
        enableMenuItem = enabled;
        this.invalidateOptionsMenu();
    }

    @Override
    public void enableDrawerMenu(boolean enabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        getSupportActionBar().setHomeButtonEnabled(enabled);
    }

    @Override
    public void setTitle(String title) {
        this.getSupportActionBar().setTitle(title);
    }

    @Override
    public void showRetry(String message) {
        loadingView.setVisibility(View.GONE);
        final Snackbar bar = SnackbarManager.make(coordinatorLayout, message, Snackbar.LENGTH_LONG, R.color._snackbar_background, R.color._text_color);
        bar.setActionTextColor(getResources().getColor(R.color._text_color));
        bar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkManager.getInstance().hasNetworkConnection()) {
                    if (retryListener != null) {
                        retryListener.retry();
                        retryListener = null;
                        bar.dismiss();
                    } else {
                        EventBus.getDefault().post(new RetryCommand());
                    }
                } else {
                    hideLoading();
                    bar.dismiss();
                }
            }
        });

        bar.show();

    }

    @Override
    public void showRetry(String message, RetryListener listener) {
       retryListener = listener;
       showRetry(message);
    }

    @Override
    public void hideRetry() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    //Event Handlers

    public void onEventMainThread(MenuCommand event) {
        enableMenu(event.isEnabled());
    }

    public void onEventMainThread(DrawerMenuCommand event) {
        enableDrawerMenu(event.isEnabled());
    }

    public void onEventMainThread(BackMenuCommand event) {
        enableBack(event.isEnabled());
    }

    public void onEventMainThread(RetryRequestCommand event) {

        if(event.isShow()) {
            retryListener = event.getListener();
            showRetry(event.getMessage());
        }
        else {
            hideRetry();
        }
    }

    public void onEventMainThread(LoadingCommand event) {

        if(event.isShow()) {
            showLoading(event.getBackgroundColorResourceId());
        }
        else {
            hideLoading();
        }
    }
    public void onEventMainThread(ChangeActionBarTitleCommand cmd) {
        this.setTitle(cmd.getTitle());
    }




    private SharedPreferences getPreferences() {
        return getSharedPreferences(Constants.USER_DATA,MODE_PRIVATE);
    }


    public void onEventMainThread(UserStatusChangedEvent event) {
        if(event.getStatus() == User.UserStatus.LoggedOut || event.getStatus() == User.UserStatus.Deleted) {
            this.Logout();
        }
    }

    protected boolean canGoBack(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.home || !(drawerView.isEnabled() && item.getTitle() == null);
    }

    protected boolean popback(MenuItem item) {
        boolean canGoBack = false;
        if(this.canGoBack(item)) {
            getSupportFragmentManager().popBackStack();
            canGoBack = true;
        }

        return canGoBack;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }
}
