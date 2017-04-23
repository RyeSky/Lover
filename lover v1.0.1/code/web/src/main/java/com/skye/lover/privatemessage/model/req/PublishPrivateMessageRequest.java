package com.skye.lover.privatemessage.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 发送私信消息请求实体类
 */
public class PublishPrivateMessageRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 聊天中的另一方
     */
    @Expose
    public String another;
    /**
     * 聊天内容
     */
    @Expose
    public String content;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(another) && !CommonUtil.isBlank(content);
    }

    @Override
    public String toString() {
        return "PublishPrivateMessageRequest{" +
                "userId='" + userId + '\'' +
                ", another='" + another + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
