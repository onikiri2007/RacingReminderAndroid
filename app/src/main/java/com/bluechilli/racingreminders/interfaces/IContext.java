package com.bluechilli.racingreminders.interfaces;

import android.content.Context;

import com.path.android.jobqueue.JobManager;

/**
 * Created by monishi on 4/03/15.
 */
public interface IContext {
    public Context context();
    public JobManager jobManager();
}
