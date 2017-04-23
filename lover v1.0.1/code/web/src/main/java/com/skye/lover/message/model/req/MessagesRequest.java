package com.skye.lover.message.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 消息列表请求实体类
 */
public class MessagesRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 请求页数
     */
    @Expose
    public int page;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && page > 0;
    }

    @Override
    public String toString() {
        return "MessagesRequest{" +
                "userId='" + userId + '\'' +
                ", page=" + page +
                '}';
    }
}
