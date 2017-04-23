package com.skye.lover.mvp.view;

import com.skye.lover.model.User;

/**
 * 其他用户信息视图
 */
public interface OtherInfoView extends BaseView {
    /**
     * 获取其他用户信息之后
     *
     * @param user 用户信息
     */
    void afterOtherInfo(User user);

    /**
     * 分手之后
     */
    void afterBreakUp();

    /**
     * 坠入爱河之后
     *
     * @param user 恋人用户信息
     */
    void afterFallInLove(User user);
}
