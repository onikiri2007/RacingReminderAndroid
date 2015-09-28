package com.bluechilli.racingreminders;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.User;
import com.bluechilli.racingreminders.stores.UserStore;
import com.bluechilli.racingreminders.utils.Installation;
import com.bluechilli.racingreminders.utils.SnackbarManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ImageButton button;
    GoogleCloudMessaging gcm;
    String registrationId;

    @Override
    protected int baseLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = (ImageButton) findViewById(R.id.btn_start);
        button.setOnClickListener(this);

        if(!TextUtils.isEmpty(UserStore.getInstance().getToken())) {
            showLoading(R.color._loading);
            login();
         }
     }


    class Notification {
        public Context context;
        public User user;
    }

    private void login() {


        Notification notification = new Notification();

        notification.context = this;
        User user = new User();
        user.token = registrationId;
        user.deviceId = Installation.getUniqueId(App.getInstance());
        notification.user = user;

        final AsyncTask<Notification, Void, User> registrationTask = new AsyncTask<Notification, Void, User>() {

            String message;

            @Override
            protected User doInBackground(Notification... params) {
                Notification data = params[0];
                Context context = data.context;
                User user = data.user;

                try {

                    if(TextUtils.isEmpty(user.token)) {
                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                        user.token = gcm.register(context.getString(R.string.sender_id));
                    }
                }
                catch(IOException ex) {
                    Log.d(Constants.TAG, ex.getLocalizedMessage());
                }

                return user;

            }

            @Override
            protected void onPostExecute(User user) {

                if(TextUtils.isEmpty(user.token)) {
                    hideLoading();
                    Snackbar.make(MainActivity.this.coordinatorLayout, "Could not login please try again!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                UserStore.getInstance().login(user, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        hideLoading();
                        MainActivity.this.startFollow();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        hideLoading();
                        Snackbar bar = SnackbarManager.make(MainActivity.this.coordinatorLayout, error.getLocalizedMessage(), Snackbar.LENGTH_LONG, R.color._snackbar_background, R.color._text_color);
                        bar.show();
                    }
                });

            }
        };

        registrationTask.execute(notification);
     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        showLoading(R.color._loading);
        login();

    }


    private void startFollow() {
        Intent intent = new Intent(MainActivity.this, FollowActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
