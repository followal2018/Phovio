package com.videos.phovio.ui.view;

import android.animation.Animator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Tamim on 29/04/2018.
 */


public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationViewEx> {

    private int height;
    private FloatingActionButton fab;
    public BottomNavigationViewBehavior(FloatingActionButton fab) {
        this.fab=fab;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationViewEx child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationViewEx child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationViewEx child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    private void slideUp(BottomNavigationViewEx child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fab.show();
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void slideDown(BottomNavigationViewEx child) {
        child.clearAnimation();
        child.animate().translationY(height).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fab.hide();

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}