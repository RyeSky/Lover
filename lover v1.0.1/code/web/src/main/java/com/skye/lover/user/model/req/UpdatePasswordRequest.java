package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 更新用户密码请求实体类
 */
public class UpdatePasswordRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 32位md5加密后的旧密码
     */
    @Expose
    public String oldPassword;
    /**
     * 32位md5加密后的旧密码
     */
    @Expose
    public String newPassword;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(oldPassword) && !CommonUtil.isBlank(newPassword);
    }

    @Override
    public String toString() {
        return "UpdatePasswordRequest{" +
                "userId='" + userId + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
