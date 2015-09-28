package com.bluechilli.racingreminders;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.bluechilli.racingreminders.configs.StoreConfigurator;
import com.bluechilli.racingreminders.interfaces.IContext;
import com.orm.SugarApp;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by monishi on 31/12/14.
 */
public class App extends SugarApp implements IContext {
    private static App ourInstance;
    private JobManager jobManager;

    public synchronized static App getInstance() {
        return ourInstance;
    }

    public App() {
        ourInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureJobManager();
        StoreConfigurator.startStores(this);
//        FontConfigurator.ConfigFonts();
     }

    @Override
    public void onTerminate() {
        super.onTerminate();
        StoreConfigurator.stopStores();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(5)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();

        jobManager = new JobManager(this, configuration);
    }

    @Override
    public Context context() {
        return this.getApplicationContext();
    }

    @Override
    public JobManager jobManager() {
        return jobManager;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
