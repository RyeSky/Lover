package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 悄悄话或世界广播模型基类
 */
public interface BasePillowTalkDetailModel {
    /**
     * 悄悄话详情
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross pillowTalkDetail(Context context, String pillowTalkId);

    /**
     * 悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross pillowTalkProperties(String pillowTalkId);

    /**
     * 删除悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross deletePillowTalk(Context context, String pillowTalkId);

    /**
     * 收藏悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross collect(Context context, String pillowTalkId);

    /**
     * 取消收藏悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross cancelCollect(Context context, String pillowTalkId);

    /**
     * 赞悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross praise(Context context, String pillowTalkId);

    /**
     * 取消赞悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross cancelPraise(Context context, String pillowTalkId);
}
