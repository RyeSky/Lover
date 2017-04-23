package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 私信聊天模型
 */
public interface PrivateMessageModel {
    /**
     * 刷新
     *
     * @param context          上下文对象
     * @param another          私信聊天中的另一方id
     * @param privateMessageId 当前界面显示的最早一条私信的id
     * @return 穿越
     */
    Cross refresh(Context context, String another, String privateMessageId);

    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param privateMessageId 要被删除的私信记录id
     * @param position         要被删除的私信记录所在位置
     * @return 穿越
     */
    Cross deletePrivateMessage(Context context, String privateMessageId, int position);
}
