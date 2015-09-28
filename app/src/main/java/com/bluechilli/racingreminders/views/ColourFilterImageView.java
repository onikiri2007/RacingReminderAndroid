package com.bluechilli.racingreminders.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bluechilli.racingreminders.R;

/**
 * Created by monishi on 8/01/15.
 */
public class ColourFilterImageView extends ImageView {

    private int filterColor;

    public ColourFilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.colorFilter, 0, 0);

        try{
            filterColor = a.getColor(R.styleable.colorFilter_filterColour, getResources().getColor(R.color._icons));
        }
        finally {
            a.recycle();
        }

        this.setColorFilter(filterColor, PorterDuff.Mode.MULTIPLY);
    }

}
