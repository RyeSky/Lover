package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.user.model.resp.User;
import com.skye.lover.util.CommonUtil;

/**
 * 注册请求实体类
 */
public class RegisterRequest implements RequestParameterCheck {
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
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    public int gender;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(name) && !CommonUtil.isBlank(password)
                && (gender == User.GENDER_SECRET || gender == User.GENDER_MALE || gender == User.GENDER_FEMALE);
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                '}';
    }
}
