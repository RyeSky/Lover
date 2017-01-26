package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 私信实体类
 */
public class PrivateMessage {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "private_message";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 私信发送者id
     */
    public static final String SENDER = "sender";
    /**
     * 私信发送者昵称
     */
    public static final String SENDER_NICKNAME = "sender_nickname";
    /**
     * 私信发送者头像
     */
    public static final String SENDER_AVATAR = "sender_avatar";
    /**
     * 私信发送者性别【0：保密；1：男；2：女】
     */
    public static final String SENDER_GENDER = "sender_gender";
    /**
     * 私信接收者id
     */
    public static final String RECEIVER = "receiver";
    /**
     * 私信接收者昵称
     */
    public static final String RECEIVER_NICKNAME = "receiver_nickname";
    /**
     * 私信接收者头像
     */
    public static final String RECEIVER_AVATAR = "receiver_avatar";
    /**
     * 私信接收者性别【0：保密；1：男；2：女】
     */
    public static final String RECEIVER_GENDER = "receiver_gender";
    /**
     * 聊天内容
     */
    public static final String CONTENT = "content";
    /**
     * 消息创建时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 私信发送者id
     */
    @Expose
    public String sender;
    /**
     * 私信发送者昵称
     */
    @Expose
    public String senderNickname;
    /**
     * 私信发送者头像
     */
    @Expose
    public String senderAvatar;
    /**
     * 私信发送者性别【0：保密；1：男；2：女】
     */
    @Expose
    public int senderGender;
    /**
     * 私信接收者id
     */
    @Expose
    public String receiver;
    /**
     * 私信接收者昵称
     */
    @Expose
    public String receiverNickname;
    /**
     * 私信接收者头像
     */
    @Expose
    public String receiverAvatar;
    /**
     * 私信接收者性别【0：保密；1：男；2：女】
     */
    @Expose
    public int receiverGender;
    /**
     * 聊天内容
     */
    @Expose
    public String content;
    /**
     * 消息创建时间
     */
    @Expose
    public String createTime;

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", senderAvatar='" + senderAvatar + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverNickname='" + receiverNickname + '\'' +
                ", receiverAvatar='" + receiverAvatar + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", senderGender=" + senderGender +
                ", receiverGender=" + receiverGender +
                '}';
    }
}
