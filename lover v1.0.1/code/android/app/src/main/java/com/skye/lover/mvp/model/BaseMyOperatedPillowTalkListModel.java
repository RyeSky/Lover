package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 用户操作过的悄悄话的列表模型基类
 */
public interface BaseMyOperatedPillowTalkListModel {
    /**
     * 设置请求页数
     *
     * @param page 请求页数
     */
    void setPage(int page);

    /**
     * 刷新
     *
     * @param context 上下文对象
     * @return 穿越
     */
    Cross refresh(Context context);


    /**
     * 加载更多
     *
     * @param context 上下文对象
     * @return 穿越
     */
    Cross loadMore(Context context);
}
