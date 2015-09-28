package com.bluechilli.racingreminders.interfaces;

/**
 * Created by monishi on 23/12/2014.
 */
public interface IHandleAction<T> {
    public void Handle(T actionParams);
}

