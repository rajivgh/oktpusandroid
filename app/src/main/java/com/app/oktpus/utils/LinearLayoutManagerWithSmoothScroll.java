package com.app.oktpus.utils;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by Gyandeep on 11/2/17.
 */

public class LinearLayoutManagerWithSmoothScroll extends LinearLayoutManager{

    private static float MILLISECONDS_PER_INCH = 110f;
    public LinearLayoutManagerWithSmoothScroll(Context context) {
        super(context, VERTICAL, false);
    }

    public void setMillisecondsPerInch(float value) {
        MILLISECONDS_PER_INCH = value;
    }

    public LinearLayoutManagerWithSmoothScroll(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);

        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return LinearLayoutManagerWithSmoothScroll.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    /*private static final float MILLISECONDS_PER_INCH = 110f;
    private Context mContext;

    public LinearLayoutManagerWithSmoothScroll(Context context) {
        super(context, VERTICAL, false);
        mContext = context;
    }

    public LinearLayoutManagerWithSmoothScroll(Context context,int orientation, boolean reverseLayout) {
        super(context,orientation,reverseLayout);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext()){
            //This controls the direction in which smoothScroll looks for your view
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return new PointF(0, 1);
            }

            //This returns the milliseconds it takes to scroll one pixel.
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }


    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);

        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return LinearLayoutManagerWithSmoothScroll.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }*/
}
