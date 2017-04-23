package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 登录请求实体类
 */
public class LoginRequest implements RequestParameterCheck {
    /**
     * 用户账号
     */
    @Expose
    public String name;
    /**
     * md5加密后的密码
     */
    @Expose
    public String password;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(name) && !CommonUtil.isBlank(password);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
