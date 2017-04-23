package com.skye.lover.privatemessage.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 根据私信记录id和聊天者id删除私信请求实体类
 */
public class DeleteByPrivateMessageIdRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 私信记录id
     */
    @Expose
    public String privateMessageId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(privateMessageId);
    }

    @Override
    public String toString() {
        return "DeleteByPrivateMessageIdRequest{" +
                "userId='" + userId + '\'' +
                ", privateMessageId='" + privateMessageId + '\'' +
                '}';
    }
}
