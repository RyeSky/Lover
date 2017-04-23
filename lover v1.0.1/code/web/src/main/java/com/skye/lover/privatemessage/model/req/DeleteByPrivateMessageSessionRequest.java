package com.skye.lover.privatemessage.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 根据聊天的两个用户删除整个会话记录请求实体类
 */
public class DeleteByPrivateMessageSessionRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 聊天中的另一方id
     */
    @Expose
    public String another;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(another);
    }

    @Override
    public String toString() {
        return "DeleteByPrivateMessageSessionRequest{" +
                "userId='" + userId + '\'' +
                ", another='" + another + '\'' +
                '}';
    }
}
