package com.app.oktpus.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Gyandeep on 24/1/17.
 */

public class FABBehaviour extends CoordinatorLayout.Behavior<FloatingActionButton>  {

    public FABBehaviour(){};
    public FABBehaviour(Context context, AttributeSet attrs) {
        super();
    }
    private boolean enableFAB = true;

    public void enableScrollToTop(boolean value){
        enableFAB = value;
    }
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
                               final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,dxUnconsumed, dyUnconsumed);
        if (child.getVisibility() == View.VISIBLE && !enableFAB) { //dyConsumed == 0 && dyUnconsumed < 0 &&
            /*CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();*/
            child.hide();
        } /*else if (dyConsumed < 0 && child.getVisibility() == View.VISIBLE) {
            child.show();
        }*/
        else if(child.getVisibility() != View.VISIBLE && enableFAB){
            //child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            child.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    //super.onShown(fab);
                    child.setAlpha(0.8f);
                }
            });
        }

        /*if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }*/

        /*if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
          child.hide();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
          child.show();
        }*/
        //System.out.println("dyConsumed: " + dyConsumed + "       dyUnconsumed: "+ dyUnconsumed);
    }
}