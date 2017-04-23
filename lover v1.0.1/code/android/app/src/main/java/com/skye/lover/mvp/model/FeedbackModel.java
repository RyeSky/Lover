package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 意见反馈模型
 */
public interface FeedbackModel {
    /**
     * 意见反馈
     *
     * @param context 上下文对象
     * @param content 反馈内容
     * @return 穿越
     */
    Cross submitFeedback(Context context, String content);
}
