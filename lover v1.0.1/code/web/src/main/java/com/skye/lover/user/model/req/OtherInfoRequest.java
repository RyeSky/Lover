package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 其他用户信息请求实体类
 */
public class OtherInfoRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId);
    }

    @Override
    public String toString() {
        return "OtherInfoRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
