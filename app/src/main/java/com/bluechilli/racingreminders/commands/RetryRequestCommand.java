package com.bluechilli.racingreminders.commands;

import com.bluechilli.racingreminders.interfaces.RetryListener;

/**
 * Created by monishi on 6/02/15.
 */
public class RetryRequestCommand {

    private final String message;
    private final boolean show;
    private final RetryListener listener;

    public RetryRequestCommand(String message, boolean show) {
        this(message, show, null);
    }

    public RetryRequestCommand(String message, boolean show, RetryListener listener) {
        this.message = message;
        this.show = show;
        this.listener = listener;
    }

    public String getMessage() {
        return message;
    }

    public boolean isShow() {
        return show;
    }

    public RetryListener getListener() {
        return listener;
    }
}
