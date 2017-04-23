package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 消息列表模型
 */
public interface MessageListModel {
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

    /**
     * 删除消息
     *
     * @param context   上下文对象
     * @param messageId 被删除的消息id
     * @param position  被删除消息的位置
     * @return 穿越
     */
    Cross deleteMessage(Context context, String messageId, int position);
}
