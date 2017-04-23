package com.skye.lover.mvp.view;

/**
 * 用户赞过的悄悄话列表视图
 */
public interface PraisedPillowTalkListView extends BaseMyOperatedPillowTalkListView {
    /**
     * 取消赞悄悄话之后
     *
     * @param position 被取消赞的悄悄话或世界广播的位置
     */
    void afterCancelPraise(int position);
}
