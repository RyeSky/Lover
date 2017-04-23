package com.skye.lover.mvp.view;

import com.skye.lover.model.PrivateMessageSession;

import java.util.List;

/**
 * 聊聊视图
 */
public interface ChatView extends BaseView {
    /**
     * 刷新数据
     *
     * @param list 数据集合
     */
    void refreshData(List<PrivateMessageSession> list);

    /**
     * 隐藏刷新组件
     */
    void hideRefresh();

    /**
     * 根据聊天双方删除聊天会话
     *
     * @param position 位置
     */
    void deleteByPrivateMessageSession(int position);
}
