package com.app.oktpus.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Gyandeep on 15/1/17.
 */

public class CustomLinearLayout extends LinearLayout {
    private GestureDetector mTapDetector;
    public CustomLinearLayout(Context context) {
        super(context);
        mTapDetector = new GestureDetector(context,new GestureTap());
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTapDetector = new GestureDetector(context,new GestureTap());
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTapDetector = new GestureDetector(context,new GestureTap());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        return false;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO: handle tap here
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mTapDetector.onTouchEvent(ev);
        return false;
    }
}