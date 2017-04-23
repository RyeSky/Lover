package com.skye.lover.mvp.presenter;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.view.BaseView;

/**
 * 主导器接口
 */
public interface Presenter<V extends BaseView> {
    /**
     * 与view关联
     */
    void attachView(V v);

    /**
     * 与view解除关联
     */
    void detachView();

    /**
     * 取消网络请求
     */
    void cancelCalls();

    /**
     * 添加穿越
     *
     * @param cross 穿越
     */
    void add(Cross cross);

    /**
     * 移除穿越
     *
     * @param cross 穿越
     */
    void remove(Cross cross);
}
