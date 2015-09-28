package com.bluechilli.racingreminders.events;

import com.bluechilli.racingreminders.models.User;

/**
 * Created by monishi on 13/03/15.
 */
public class UserStatusChangedEvent {

    private final User.UserStatus status;
    private final User user;

    public UserStatusChangedEvent(User.UserStatus status, User user) {

        this.status = status;
        this.user = user;
    }

    public User.UserStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}



