package com.skye.lover.model;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

import java.io.Serializable;

/**
 * 私信实体类
 */
public class PrivateMessage implements Serializable{
    private final long THREE_MINUTE = 1000 * 60 * 3;//3分钟
    // 私信发送者id、私信发送者昵称、私信发送者头像、私信发送者性别、
    // 私信接收者id、私信接受者昵称、私信接受者头像、私信接收者性别、聊天内容、私信创建时间
    @Expose
    private String id, sender, senderNickname, senderAvatar,
            receiver, receiverNickname, receiverAvatar,
            content, createTime;
    @Expose
    private int senderGender, receiverGender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public AvatarTag getSenderAvatarTag() {
        return new AvatarTag(senderAvatar, senderGender);
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }

    public AvatarTag getReceiverAvatarTag() {
        return new AvatarTag(receiverAvatar, receiverGender);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        String time = "";
        try {
            time = CommonUtil.getCostDate(Long.parseLong(createTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getTime() {
        long time = 0l;
        try {
            time = Long.parseLong(createTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 两条私信发送时间间隔是否大于3分钟
     */
    public boolean isDiffGtThreeMinute(long time) {
        return getTime() - time > THREE_MINUTE;
    }

    public int getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(int senderGender) {
        this.senderGender = senderGender;
    }

    public int getReceiverGender() {
        return receiverGender;
    }

    public void setReceiverGender(int receiverGender) {
        this.receiverGender = receiverGender;
    }

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
