package com.bluechilli.racingreminders.stores;

import com.bluechilli.racingreminders.interfaces.IContext;
import com.path.android.jobqueue.JobManager;

/**
 * Created by monishi on 4/03/15.
 */
public abstract class BaseStore {

    private IContext context;

    public void start(IContext context) {
        this.context = context;
    }

    public void stop() {
        context = null;
    }

    public IContext getContext() {
        return context;
    }

    public JobManager getJobManager() {
        return context.jobManager();
    }
}
