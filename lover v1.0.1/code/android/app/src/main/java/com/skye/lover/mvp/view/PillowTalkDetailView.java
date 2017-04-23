package com.skye.lover.mvp.view;

import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ScrollPosition;

import java.util.List;

/**
 * 悄悄话详情视图
 */
public interface PillowTalkDetailView extends BasePillowTalkDetailView {
    /**
     * 公开悄悄话之后
     */
    void afterOpen();

    /**
     * 获取跟随悄悄话列表之后
     *
     * @param list        跟随悄悄话集合
     * @param pageCount   数据集合总页数
     * @param requestPage 请求页数
     * @param position    请求回数据后的滚动位置
     */
    void afterFollows(List<FollowPillowTalk> list, int pageCount, int requestPage, ScrollPosition position);

    /**
     * 删除跟随悄悄话之后
     *
     * @param fpt 跟随悄悄话
     */
    void afterDeleteFollowPillowTalk(FollowPillowTalk fpt);
}
