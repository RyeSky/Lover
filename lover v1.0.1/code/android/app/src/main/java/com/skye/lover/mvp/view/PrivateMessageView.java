package com.skye.lover.mvp.view;

import com.skye.lover.model.PrivateMessageWrapper;

/**
 * 私信聊天视图
 */
public interface PrivateMessageView extends BaseView {
    /**
     * 刷新数据
     *
     * @param wrapper 数据集合
     */
    void refreshData(PrivateMessageWrapper wrapper);

    /**
     * 隐藏刷新组件
     */
    void hideRefresh();

    /**
     * 删除私信成功
     *
     * @param position 要被删除的私信记录所在位置
     */
    void deleteSuccess(int position);
}
