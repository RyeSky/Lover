package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 评论列表模型
 */
public interface CommentListModel {
    /**
     * 设置请求页数
     *
     * @param page 请求页数
     */
    void setPage(int page);

    /**
     * 刷新
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross refresh(Context context, String pillowTalkId);


    /**
     * 加载更多
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross loadMore(Context context, String pillowTalkId);

    /**
     * 删除评论
     *
     * @param context   上下文对象
     * @param commentId 评论id
     * @param position  被删除的评论的位置
     * @return 穿越
     */
    Cross deleteComment(Context context, String commentId, int position);
}
