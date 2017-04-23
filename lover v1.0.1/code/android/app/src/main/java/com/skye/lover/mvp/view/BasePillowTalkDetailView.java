package com.skye.lover.mvp.view;

import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PillowTalkProperties;

/**
 * 悄悄话或世界广播视图基类
 */
public interface BasePillowTalkDetailView extends BaseView {
    /**
     * 获取详情之后
     *
     * @param pt 悄悄话或世界广播实体
     */
    void afterPillowTalkDetail(PillowTalk pt);

    /**
     * 获取悄悄话部分属性之后
     *
     * @param ptp 悄悄话部分信息
     */
    void afterPillowTalkProperties(PillowTalkProperties ptp);

    /**
     * 收藏悄悄话之后
     *
     * @param collectId 收藏记录id
     */
    void afterCollect(String collectId);

    /**
     * 取消收藏悄悄话之后
     */
    void afterCancelCollect();

    /**
     * 赞悄悄话之后
     *
     * @param praiseId 收藏记录id
     */
    void afterPraise(String praiseId);

    /**
     * 取消赞悄悄话之后
     */
    void afterCancelPraise();
}
