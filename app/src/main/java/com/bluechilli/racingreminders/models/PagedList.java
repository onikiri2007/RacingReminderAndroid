package com.bluechilli.racingreminders.models;

/**
 * Created by monishi on 29/06/15.
 */
public class PagedList<T> {
    public int pageSize;
    public int currentPage;
    public int pageCount;
    public int totalCount;
    public T data;
}
