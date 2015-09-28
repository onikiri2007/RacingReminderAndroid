package com.bluechilli.racingreminders.models;


/**
 * Created by monishi on 30/12/14.
 */
public class User {


    private boolean empty;

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String userKey;
    public String token;
    public int maxNumberOfFollows;
    public int numberOfFollows;
    public boolean isNewUser;
    public String deviceId;

    public enum UserStatus {
        SignIn,
        LoggedOut, Deleted, SignOut
    }
}
