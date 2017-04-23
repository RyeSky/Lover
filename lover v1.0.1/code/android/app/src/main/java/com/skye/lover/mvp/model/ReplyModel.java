package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 回复悄悄话界面
 */
public interface ReplyModel {
    /**
     * 回复悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 要回复的悄悄话id
     * @param content      回复内容
     * @return 穿越
     */
    Cross reply(Context context, String pillowTalkId, String content);
}
