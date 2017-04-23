package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 用户赞过的悄悄话列表模型
 */
public interface PraisedPillowTalkListModel extends BaseMyOperatedPillowTalkListModel {
    /**
     * 取消赞悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @param position     被取消赞的悄悄话或世界广播的位置
     * @return 穿越
     */
    Cross cancelPraise(Context context, String pillowTalkId, int position);
}
