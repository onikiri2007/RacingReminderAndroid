package com.bluechilli.racingreminders.interfaces;

/**
 * Created by monishi on 5/02/15.
 */
public interface IHandleRetry {
    void showRetry(String message);
    void showRetry(String message, RetryListener listener);
    void hideRetry();
}
