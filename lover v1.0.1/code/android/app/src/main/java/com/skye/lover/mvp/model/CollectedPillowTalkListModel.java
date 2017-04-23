package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 用户收藏的悄悄话列表模型
 */
public interface CollectedPillowTalkListModel extends BaseMyOperatedPillowTalkListModel {
    /**
     * 取消收藏悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @param position     被取消收藏的悄悄话或世界广播的位置
     * @return 穿越
     */
    Cross cancelCollect(Context context, String pillowTalkId, int position);
}
