package com.wingsofts.byeburgernavigationview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Base Behavior
 * Created by wing on 11/8/16.
 */

abstract public class ByeBurgerBehavior extends CoordinatorLayout.Behavior<View> {
    protected final int mTouchSlop, screenHeight, blankTitleBottom;
    protected boolean isFirstMove = true;
    protected boolean canInit = true;
    protected boolean isAble = true;
    protected AnimateHelper mAnimateHelper;

    public ByeBurgerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        blankTitleBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
    }

    public static ByeBurgerBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof ByeBurgerBehavior)) {
            throw new IllegalArgumentException("The view is not associated with ByeBurgerBehavior");
        }
        return (ByeBurgerBehavior) behavior;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        RecyclerView recycler = null;
        View view;
        for (int i = 0, n = coordinatorLayout.getChildCount(); i < n; i++) {
            view = coordinatorLayout.getChildAt(i);
            if (view instanceof RecyclerView) {
                recycler = (RecyclerView) view;
                break;
            }
        }
        if (recycler != null) {
            int height = 0;
            for (int i = 0, n = recycler.getChildCount(); i < n; i++) {
                view = recycler.getChildAt(i);
                height += view.getHeight();
            }

            isAble = height >= screenHeight - blankTitleBottom;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        if (!isAble) return;

        onNestPreScrollInit(child);

        if (Math.abs(dy) > mTouchSlop) {
            if (dy < 0) {
                if (mAnimateHelper.getState() == TranslateAnimateHelper.STATE_HIDE) {
                    mAnimateHelper.show();
                }
            } else if (dy > 0) {
                if (mAnimateHelper.getState() == TranslateAnimateHelper.STATE_SHOW) {
                    mAnimateHelper.hide();
                }
            }
        }
    }

    protected abstract void onNestPreScrollInit(View child);

    public void show() {
        mAnimateHelper.show();
    }

    public void hide() {
        mAnimateHelper.hide();
    }
}
