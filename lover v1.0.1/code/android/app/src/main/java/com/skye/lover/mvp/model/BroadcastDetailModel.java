package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Comment;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ScrollPosition;

/**
 * 世界广播详情模型
 */
public interface BroadcastDetailModel extends BasePillowTalkDetailModel {
    /**
     * 获取世界广播评论列表
     *
     * @param pillowTalkId 世界广播id
     * @param page         请求页数
     * @param position     请求回数据后的滚动位置
     * @return 穿越
     */
    Cross comments(String pillowTalkId, int page, ScrollPosition position);

    /**
     * 删除评论
     *
     * @param context 上下文对象
     * @param comment 评论
     * @return 穿越
     */
    Cross deleteComment(Context context, Comment comment);
}
