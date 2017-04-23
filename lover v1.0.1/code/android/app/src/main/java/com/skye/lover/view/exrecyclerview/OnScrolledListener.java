package com.skye.lover.view.exrecyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * recyclerview滑动时触发
 */
public interface OnScrolledListener {
    void onScrolled(RecyclerView recyclerView, int dx, int dy);
}
