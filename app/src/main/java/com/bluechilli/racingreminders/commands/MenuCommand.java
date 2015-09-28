package com.bluechilli.racingreminders.commands;

/**
 * Created by monishi on 6/02/15.
 */
public class MenuCommand {



    private final boolean enabled;

    public MenuCommand(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
