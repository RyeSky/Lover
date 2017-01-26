package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 私信会话实体类
 */
public class PrivateMessageSession {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "private_message_session";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 私信会话中的一方
     */
    public static final String ONE = "one";
    /**
     * 私信会话对方id
     */
    public static final String ANOTHER = "another";
    /**
     * 私信会话对方的昵称
     */
    public static final String ANOTHER_NICKNAME = "another_nickname";
    /**
     * 私信会话对方的头像
     */
    public static final String ANOTHER_AVATAR = "another_avatar";
    /**
     * 私信会话对方的性别【0：保密；1：男；2：女】
     */
    public static final String ANOTHER_GENDER = "another_gender";
    /**
     * 最后一条消息id
     */
    public static final String LAST_PRIVATE_MESSAGE_ID = "last_private_message_id";
    /**
     * 最后一条消息内容
     */
    public static final String LAST_PRIVATE_MESSAGE_CONTENT = "last_private_message_content";
    /**
     * 最后一条消息创建时间
     */
    public static final String LAST_PRIVATE_MESSAGE_CREATE_TIME = "last_private_message_create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 私信会话中的一方
     */
    @Expose
    public String one;
    /**
     * 私信会话对方id
     */
    @Expose
    public String another;
    /**
     * 私信会话对方的昵称
     */
    @Expose
    public String anotherNickname;
    /**
     * 私信会话对方的头像
     */
    @Expose
    public String anotherAvatar;
    /**
     * 私信会话对方的性别【0：保密；1：男；2：女】
     */
    @Expose
    public int anotherGender;
    /**
     * 最后一条消息id
     */
    @Expose
    public String lastPrivateMessageId;
    /**
     * 最后一条消息内容
     */
    @Expose
    public String lastPrivateMessageContent;
    /**
     * 最后一条消息创建时间
     */
    @Expose
    public String lastPrivateMessageCreateTime;

    @Override
    public String toString() {
        return "PrivateMessageSession{" +
                "id='" + id + '\'' +
                ", one='" + one + '\'' +
                ", another='" + another + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", lastPrivateMessageId='" + lastPrivateMessageId + '\'' +
                ", lastPrivateMessageContent='" + lastPrivateMessageContent + '\'' +
                ", lastPrivateMessageCreateTime='" + lastPrivateMessageCreateTime + '\'' +
                ", anotherGender=" + anotherGender +
                '}';
    }
}
