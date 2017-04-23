package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 私信包装器
 */
@Keep
public class PrivateMessageWrapper {
    /**
     * 私信数据集合
     */
    @Expose
    public List<PrivateMessage> list;
    /**
     * 整个聊天会话中的第一条私信的id
     */
    @Expose
    public String firstPrivateMessageId;

    @Override
    public String toString() {
        return "PrivateMessageWrapper{" +
                "list=" + list +
                ", firstPrivateMessageId='" + firstPrivateMessageId + '\'' +
                '}';
    }
}
