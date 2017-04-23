package com.skye.lover.mvp.model;

import com.skye.lover.model.Cross;

/**
 * 登录模型
 */
public interface LoginModel {
    /**
     * 登录
     *
     * @param name     用户名
     * @param password 密码
     * @return 穿越
     */
    Cross login(String name, String password);
}
