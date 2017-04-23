package com.skye.lover.mvp.view;

/**
 * 用户收藏的悄悄话列表视图
 */
public interface CollectedPillowTalkListView extends BaseMyOperatedPillowTalkListView {
    /**
     * 取消收藏悄悄话之后
     *
     * @param position 被取消收藏的悄悄话或世界广播的位置
     */
    void afterCancelCollect(int position);
}
