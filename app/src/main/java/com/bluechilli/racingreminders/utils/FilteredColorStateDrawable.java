package com.bluechilli.racingreminders.utils;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.SparseArray;

/**
* Created by monishi on 16/02/15.
*/
public class FilteredColorStateDrawable extends StateListDrawable {

    private SparseArray<ColorFilter> filters = new SparseArray<ColorFilter>() ;
    private ColorFilter defaultFilter;

    public FilteredColorStateDrawable() {
        super();
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {

        boolean isStatePressedInArray = false;

        for (int state : stateSet) {
            if (state == android.R.attr.state_pressed) {
                isStatePressedInArray = true;
                break;
            }
        }
        super.mutate();
        if(isStatePressedInArray) {
            super.setColorFilter(getColorFilterForIndex(android.R.attr.state_pressed));
        }
        else {
           if(defaultFilter != null) {
                super.setColorFilter(defaultFilter);
            }
            else {
                super.clearColorFilter();
            }
        }

        return super.onStateChange(stateSet);
    }

    public void addState(int[] stateSet, Drawable drawable, ColorFilter filter) {

        if(stateSet.length > 0) {
            for(int state : stateSet) {
                filters.put(state, filter);
            }
        }
        else {
            defaultFilter = filter;
        }

        super.addState(stateSet, drawable);
    }

    private ColorFilter getColorFilterForIndex(int index) {

        return filters != null ? filters.get(index) : null;
    }

    @Override
    public boolean isStateful() {

        return true;
    }
}
