package com.skye.lover.mvp.model;

import com.skye.lover.model.Cross;

/**
 * 注册模型
 */
public interface RegisterModel {
    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     * @param gender   性别【0：保密；1：男；2：女】
     * @return 穿越
     */
    Cross register(String name, String password, String gender);
}
