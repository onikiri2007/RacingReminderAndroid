package com.bluechilli.racingreminders.commands;

/**
 * Created by monishi on 26/02/15.
 */
public class ChangeActionBarTitleCommand {

    private final String title;

    public ChangeActionBarTitleCommand(String title) {

        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
