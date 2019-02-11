package com.example.daveart.vocabularyapp.utils;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.daveart.vocabularyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class FabUtil extends FloatingActionButton.Behavior {

    Context context;
    public FabUtil(Context context, AttributeSet attributeSet){

        super();
        this.context = context;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int fab_bottomMargin = layoutParams.bottomMargin;

        if(child.getId() == R.id.fab_options) {
            if (dyConsumed > 0) {
    //
    //            child.hide();
                child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator
                        (new AccelerateInterpolator()).start();
    ////
    ////            child.animate().rotation(360).start();
    ////            child.animate().scaleX(0).scaleY(0).start();
    ////            child.setVisibility(View.GONE);
    //
            } else if (dyConsumed < 0) {
    ////
    //            child.show();
    ////            child.animate().rotation(-360).start();
    ////            child.animate().scaleX(1).scaleY(1).start();
    ////            child.setVisibility(View.VISIBLE);
                child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            }

        } else {

            if(dyConsumed > 0) {
                child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator
                        (new AccelerateInterpolator()).start();
//                child.hide();
            }else if(dyConsumed < 0) {
                child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
//                child.show();
            }
        }
    }

//    @Override
//    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
//                type);;
//    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll
                (coordinatorLayout, child, directTargetChild, target, axes, type);
    }
}
