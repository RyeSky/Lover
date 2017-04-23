package com.skye.lover.privatemessage.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 私信会话列表请求实体类
 */
public class PrivateMessageSessionsRequest implements RequestParameterCheck {
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
        return "PrivateMessageSessionsRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
