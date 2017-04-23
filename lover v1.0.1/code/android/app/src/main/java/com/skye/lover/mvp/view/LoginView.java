package com.skye.lover.mvp.view;

/**
 * 登录界面视图
 */
public interface LoginView extends BaseView {
    /**
     * 跳转到主界面
     *
     * @param isSingle 是否是单身贵族
     */
    void jumpToMainActivity(boolean isSingle);
}
