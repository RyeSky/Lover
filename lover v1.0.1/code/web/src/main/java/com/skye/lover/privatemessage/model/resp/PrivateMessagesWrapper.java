package com.skye.lover.privatemessage.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 私信记录列表返回
 */
public class PrivateMessagesWrapper {
    /**
     * 私信记录集合
     */
    @Expose
    public Object list;
    /**
     * 私信聊天记录的第一条记录id，如果和客户端显示的第一条id一致，则加载完成，没有更早的聊天记录了
     */
    @Expose
    public String firstPrivateMessageId;

    public PrivateMessagesWrapper(Object list, String firstPrivateMessageId) {
        this.list = list;
        this.firstPrivateMessageId = firstPrivateMessageId;
    }

    @Override
    public String toString() {
        return "PrivateMessagesWrapper{" +
                "list=" + list +
                ", firstPrivateMessageId='" + firstPrivateMessageId + '\'' +
                '}';
    }
}
