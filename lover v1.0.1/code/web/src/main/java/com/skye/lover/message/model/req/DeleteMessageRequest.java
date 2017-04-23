package com.skye.lover.message.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 刪除消息请求实体类
 */
public class DeleteMessageRequest implements RequestParameterCheck {
    /**
     * 消息所属用户id
     */
    @Expose
    public String userId;
    /**
     * 消息记录id
     */
    @Expose
    public String messageId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(messageId);
    }

    @Override
    public String toString() {
        return "DeleteMessageRequest{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
