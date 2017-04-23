package com.skye.lover.mvp.view;

import com.skye.lover.model.AppVersion;

/**
 * 设置视图
 */
public interface SettingView extends BaseView {
    /**
     * 获取版本信息之后
     *
     * @param version app版本信息
     */
    void afterLastAppVersion(AppVersion version);
}
