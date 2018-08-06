package com.app.oktpus.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Gyandeep on 9/8/17.
 */

public class SectionHeaderTextView extends TextView{
    public SectionHeaderTextView(Context context, String header, String desc) {
        super(context);

    }

    public SectionHeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionHeaderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SectionHeaderTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
