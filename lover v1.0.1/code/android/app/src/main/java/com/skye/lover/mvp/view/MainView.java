package com.skye.lover.mvp.view;

import com.skye.lover.model.AppVersion;

/**
 * 主视图
 */
public interface MainView extends BaseView {
    /**
     * 获取版本信息之后
     *
     * @param version app版本信息
     */
    void afterLastAppVersion(AppVersion version);
}
