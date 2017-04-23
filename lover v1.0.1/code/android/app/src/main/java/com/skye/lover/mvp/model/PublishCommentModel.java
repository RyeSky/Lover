package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 发表评论模型
 */
public interface PublishCommentModel {
    /**
     * 发表评论
     *
     * @param context      上下文对象
     * @param pillowTalkId 被评论的悄悄话或世界广播id
     * @param content      评论内容
     * @return 穿越
     */
    Cross publishComment(Context context, String pillowTalkId, String content);
}
