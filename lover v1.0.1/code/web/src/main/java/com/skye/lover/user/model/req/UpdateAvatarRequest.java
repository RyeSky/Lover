package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 更新用户头像请求实体类
 */
public class UpdateAvatarRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 头像url
     */
    @Expose
    public String avatar;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(avatar);
    }

    @Override
    public String toString() {
        return "UpdateAvatarRequest{" +
                "userId='" + userId + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
