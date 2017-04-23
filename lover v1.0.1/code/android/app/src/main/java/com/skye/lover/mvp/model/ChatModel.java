package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 聊聊模型
 */
public interface ChatModel {

    /**
     * 刷新
     *
     * @param context 上下文对象
     * @return 穿越
     */
    Cross refresh(Context context);

    /**
     * 根据聊天双方删除聊天会话
     *
     * @param another 聊天中的另一方
     * @param position item position
     * @return 穿越
     */
    Cross deleteByPrivateMessageSession(Context context, String another, int position);
}
