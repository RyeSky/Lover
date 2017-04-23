package com.skye.lover.mvp.view;

import com.skye.lover.model.Comment;
import com.skye.lover.model.ScrollPosition;

import java.util.List;

/**
 * 世界广播详情视图
 */
public interface BroadcastDetailView extends BasePillowTalkDetailView {
    /**
     * 获取世界广播评论列表之后
     *
     * @param list        世界广播评论集合
     * @param pageCount   数据集合总页数
     * @param requestPage 请求页数
     * @param position    请求回数据后的滚动位置
     */
    void afterComments(List<Comment> list, int pageCount, int requestPage, ScrollPosition position);

    /**
     * 删除跟随悄悄话之后
     *
     * @param comment 评论
     */
    void afterDeleteComment(Comment comment);
}
