package com.skye.lover.mvp.view;

import com.skye.lover.model.Comment;

import java.util.List;

/**
 * 评论列表视图
 */
public interface CommentListView extends BaseView {
    /**
     * 刷新数据
     *
     * @param list       数据集合
     * @param isLastPage 是否是最后一页
     */
    void refreshData(List<Comment> list, boolean isLastPage);

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
    void loadMoreData(List<Comment> list, boolean isLastPage);

    /**
     * 加载更多完成
     */
    void loadMoreFinished();

    /**
     * 删除评论之后
     *
     * @param position 被删除评论的位置
     */
    void afterDeleteComment(int position);
}
