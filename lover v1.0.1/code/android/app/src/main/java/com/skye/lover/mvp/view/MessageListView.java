package com.skye.lover.mvp.view;

import com.skye.lover.model.Message;

import java.util.List;

/**
 * 消息列表视图
 */
public interface MessageListView extends BaseView {
    /**
     * 刷新数据
     *
     * @param list       数据集合
     * @param isLastPage 是否是最后一页
     */
    void refreshData(List<Message> list, boolean isLastPage);

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
    void loadMoreData(List<Message> list, boolean isLastPage);

    /**
     * 加载更多完成
     */
    void loadMoreFinished();

    /**
     * 删除消息之后
     *
     * @param position 被删除消息的位置
     */
    void afterDeleteMessage(int position);
}
