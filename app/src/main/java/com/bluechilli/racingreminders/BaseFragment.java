package com.bluechilli.racingreminders;

import android.support.v4.app.Fragment;

import com.bluechilli.racingreminders.commands.BackMenuCommand;
import com.bluechilli.racingreminders.commands.ChangeActionBarTitleCommand;
import com.bluechilli.racingreminders.commands.DrawerMenuCommand;
import com.bluechilli.racingreminders.commands.LoadingCommand;
import com.bluechilli.racingreminders.commands.MenuCommand;
import com.bluechilli.racingreminders.commands.RetryRequestCommand;
import com.bluechilli.racingreminders.interfaces.IHandleLoadingIndicator;
import com.bluechilli.racingreminders.interfaces.IHandleMenu;
import com.bluechilli.racingreminders.interfaces.IHandleRetry;
import com.bluechilli.racingreminders.interfaces.RetryListener;

import de.greenrobot.event.EventBus;

/**
 * Created by monishi on 20/01/15.
 */
public class BaseFragment extends Fragment implements IHandleLoadingIndicator , IHandleRetry , IHandleMenu {

    public interface OnCallback {
        public void success();
        public void failure();
    }

    @Override
    public void showLoading(int colorResourceId) {
      EventBus.getDefault().post(new LoadingCommand(colorResourceId, true));
    }

    @Override
    public void hideLoading() {
        EventBus.getDefault().post(new LoadingCommand(false));
    }

    @Override
    public void showRetry(String message) {
        EventBus.getDefault().post(new RetryRequestCommand(message, true));
    }

    @Override
    public void showRetry(String message, RetryListener listener) {
        EventBus.getDefault().post(new RetryRequestCommand(message, true, listener));
    }

    @Override
    public void hideRetry() {
        EventBus.getDefault().post(new RetryRequestCommand(null, false, null));
    }

    @Override
    public void enableBack(boolean enabled) {
        EventBus.getDefault().post(new BackMenuCommand(enabled));
    }

    @Override
    public void enableMenu(boolean enabled) {
        EventBus.getDefault().post(new MenuCommand(enabled));

    }

    @Override
    public void enableDrawerMenu(boolean enabled) {
        EventBus.getDefault().post(new DrawerMenuCommand(enabled));

    }

    @Override
    public void setTitle(String title) {
        EventBus.getDefault().post(new ChangeActionBarTitleCommand(title));
    }


}
