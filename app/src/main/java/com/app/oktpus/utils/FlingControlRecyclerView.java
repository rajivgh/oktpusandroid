package com.app.oktpus.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Gyandeep on 9/9/17.
 */

public class FlingControlRecyclerView extends RecyclerView{
    public FlingControlRecyclerView(Context context) {
        super(context);
    }

    public FlingControlRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingControlRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final double mVelocityMultiplier = 1.35;
    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY *= mVelocityMultiplier;
        return super.fling(velocityX, velocityY);
    }
}
