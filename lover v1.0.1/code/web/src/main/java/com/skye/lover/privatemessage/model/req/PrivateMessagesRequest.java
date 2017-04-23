package com.skye.lover.privatemessage.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 私信聊天记录请求实体类
 */
public class PrivateMessagesRequest implements RequestParameterCheck {
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
    /**
     * 指定的私信记录id
     */
    @Expose
    public String privateMessageId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(another);
    }

    @Override
    public String toString() {
        return "PrivateMessagesRequest{" +
                "userId='" + userId + '\'' +
                ", another='" + another + '\'' +
                ", privateMessageId='" + privateMessageId + '\'' +
                '}';
    }
}
