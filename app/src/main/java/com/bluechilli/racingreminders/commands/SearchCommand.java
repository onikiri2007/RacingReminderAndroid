package com.bluechilli.racingreminders.commands;

/**
 * Created by monishi on 2/07/15.
 */
public class SearchCommand {
    private final String query;

    public SearchCommand(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
