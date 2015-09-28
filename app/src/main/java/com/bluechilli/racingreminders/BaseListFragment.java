package com.bluechilli.racingreminders;

import android.support.v4.app.ListFragment;

import com.bluechilli.racingreminders.commands.ChangeActionBarTitleCommand;
import com.bluechilli.racingreminders.interfaces.IHandleLoadingIndicator;
import com.bluechilli.racingreminders.interfaces.IHandleMenu;
import com.bluechilli.racingreminders.interfaces.IHandleRetry;
import com.bluechilli.racingreminders.interfaces.RetryListener;

import de.greenrobot.event.EventBus;

/**
 * Created by monishi on 6/02/15.
 */
public class BaseListFragment extends ListFragment implements IHandleLoadingIndicator, IHandleMenu,IHandleRetry {

    @Override
    public void showLoading(int colorResourceId) {

        if(isAdded()) {
            ((IHandleLoadingIndicator) getActivity()).showLoading(colorResourceId);
        }

    }

    @Override
    public void hideLoading() {
        if(isAdded()) {
            ((IHandleLoadingIndicator) getActivity()).hideLoading();
        }

    }

    @Override
    public void enableBack(boolean enabled) {
        IHandleMenu handler = ((IHandleMenu) getActivity());
        handler.enableBack(enabled);
    }

    @Override
    public void enableMenu(boolean enabled) {
        if(isAdded()) {
            IHandleMenu handler = ((IHandleMenu) getActivity());
            handler.enableMenu(enabled);
        }
    }

    @Override
    public void enableDrawerMenu(boolean enabled) {
        if(isAdded()) {
            IHandleMenu handler = ((IHandleMenu) getActivity());
            handler.enableDrawerMenu(enabled);
        }
    }

    @Override
    public void showRetry(String message) {
        if(isAdded()) {
            IHandleRetry handler = ((IHandleRetry) getActivity());
            handler.showRetry(message);
        }
    }

    @Override
    public void showRetry(String message, RetryListener listener) {
        if(isAdded()) {
            IHandleRetry handler = ((IHandleRetry) getActivity());
            handler.showRetry(message, listener);
        }

    }

    @Override
    public void hideRetry() {
        if(isAdded()) {
            IHandleRetry handler = ((IHandleRetry) getActivity());
            handler.hideRetry();
        }
    }

    @Override
    public void setTitle(String title) {
        EventBus.getDefault().post(new ChangeActionBarTitleCommand(title));
    }
}
