package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ScrollPosition;

/**
 * 悄悄话详情模型
 */
public interface PillowTalkDetailModel extends BasePillowTalkDetailModel {
    /**
     * 公开悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    Cross open(Context context, String pillowTalkId);

    /**
     * 获取跟随悄悄话列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求页数
     * @param position     请求回数据后的滚动位置
     * @return 穿越
     */
    Cross follows(String pillowTalkId, int page, ScrollPosition position);

    /**
     * 删除跟随悄悄话
     *
     * @param context 上下文对象
     * @param fpt     跟随悄悄话
     * @return 穿越
     */
    Cross deleteFollowPillowTalk(Context context, FollowPillowTalk fpt);
}