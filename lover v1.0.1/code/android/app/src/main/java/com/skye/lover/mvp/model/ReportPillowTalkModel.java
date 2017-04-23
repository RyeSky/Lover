package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 举报悄悄话模型
 */
public interface ReportPillowTalkModel {
    /**
     * 举报悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 要举报的悄悄话id
     * @param content      举报内容
     * @return 穿越
     */
    Cross reportPillowTalk(Context context, String pillowTalkId, String content);
}
