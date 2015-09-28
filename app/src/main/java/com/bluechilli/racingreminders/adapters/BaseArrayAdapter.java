package com.bluechilli.racingreminders.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by monishi on 6/01/15.
 */
public class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    protected Context context;
    protected Collection<T> data;
    protected int resourceId;
    protected int dropdownViewResourceId;

    public BaseArrayAdapter(Context context, int resourceId, Collection<T> data) {
        super(context, resourceId, new ArrayList<T>(data));
        this.context = context;
        this.resourceId = resourceId;
        this.data = data;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       return super.getView(position, convertView, parent);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropdownViewResourceId = resource;
    }
}
