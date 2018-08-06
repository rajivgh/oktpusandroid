package com.app.oktpus.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by Gyandeep on 14/1/17.
 */

public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            ViewParent parent = getParent();
            while (null != parent) {
                parent.requestDisallowInterceptTouchEvent(true);
                parent = parent.getParent();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return super.onTouchEvent(e);
    }
}