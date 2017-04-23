package com.skye.lover.mvp.view;

import com.skye.lover.model.PillowTalk;

import java.util.List;

/**
 * 用户操作过的悄悄话的列表模型基类
 */

public interface BaseMyOperatedPillowTalkListView extends BaseView {
    /**
     * 刷新数据
     *
     * @param list       数据集合
     * @param isLastPage 是否是最后一页
     */
    void refreshData(List<PillowTalk> list, boolean isLastPage);

    /**
     * 隐藏刷新组件
     */
    void hideRefresh();

    /**
     * 加载更多数据
     *
     * @param list       数据集合
     * @param isLastPage 是否是最后一页
     */
    void loadMoreData(List<PillowTalk> list, boolean isLastPage);

    /**
     * 加载更多完成
     */
    void loadMoreFinished();
}
