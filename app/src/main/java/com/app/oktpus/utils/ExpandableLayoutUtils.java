package com.app.oktpus.utils;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;

/**
 * Created by Gyandeep on 11/3/17.
 */

public class ExpandableLayoutUtils {

    static ScrolledParent getScrolledParent (ViewGroup child) {
        ViewParent parent = child.getParent();
        int childBetweenParentCount =0;
        while (parent!=null){
            if((parent instanceof RecyclerView || parent instanceof AbsListView)) {
                ScrolledParent scrolledParent = new ScrolledParent();
                scrolledParent.scrolledView = (ViewGroup)parent;
                scrolledParent.childBetweenParentCount =childBetweenParentCount;
                return scrolledParent;
            }
            childBetweenParentCount++;
            parent = parent.getParent();
        }
        return null;
    }

    static ValueAnimator createParentAnimator(final View parent, int distance , long duration) {
        ValueAnimator parentAnimator = ValueAnimator.ofInt(0,distance);
        parentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int lastDy;
            int dy;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    dy = (int)animation.getAnimatedValue()-lastDy;
                    lastDy = (int)animation.getAnimatedValue();
                    parent.scrollBy(0,dy);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        parentAnimator.setDuration(duration);

        return  parentAnimator;
    }
}