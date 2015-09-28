package com.bluechilli.racingreminders.models;

import com.bluechilli.racingreminders.definitions.Constants;

/**
 * Created by monishi on 29/06/15.
 */
public class SearchCriteria {

    public SearchCriteria() {
        this(null, 1,Constants.DEFAULT_PAGE_SIZE);
    }

    public SearchCriteria(String searchText, int page) {
        this(searchText, page,Constants.DEFAULT_PAGE_SIZE);
    }

    public SearchCriteria(String searchText, int page, int pageSize) {
        this.searchText = searchText;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String searchText;
    public int page;
    public int pageSize;
}
