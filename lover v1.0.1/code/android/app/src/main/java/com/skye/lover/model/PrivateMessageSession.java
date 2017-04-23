package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 私信会话实体类
 */
@Keep
public class PrivateMessageSession {
    //聊天中的一方、 私信会话的对方、私信会话的对方昵称、私信会话的对方头像、最后一条消息id、最后一条消息内容、最后一条消息创建时间
    @Expose
    private String id, one, another, anotherNickname, anotherAvatar,
            lastPrivateMessageId, lastPrivateMessageContent, lastPrivateMessageCreateTime;

    //私信会话的对方性别
    @Expose
    private int anotherGender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }

    public String getAnotherNickname() {
        return anotherNickname;
    }

    public void setAnotherNickname(String anotherNickname) {
        this.anotherNickname = anotherNickname;
    }

    public String getAnotherAvatar() {
        return anotherAvatar;
    }

    public void setAnotherAvatar(String anotherAvatar) {
        this.anotherAvatar = anotherAvatar;
    }

    public AvatarTag getAnotherAvatarTag() {
        return new AvatarTag(anotherAvatar, anotherGender);
    }

    public String getLastPrivateMessageId() {
        return lastPrivateMessageId;
    }

    public void setLastPrivateMessageId(String lastPrivateMessageId) {
        this.lastPrivateMessageId = lastPrivateMessageId;
    }

    public String getLastPrivateMessageContent() {
        return lastPrivateMessageContent;
    }

    public void setLastPrivateMessageContent(String lastPrivateMessageContent) {
        this.lastPrivateMessageContent = lastPrivateMessageContent;
    }

    public String getLastPrivateMessageCreateTime() {
        String time = "";
        try {
            time = CommonUtil.getCostDate(Long.parseLong(lastPrivateMessageCreateTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public void setLastPrivateMessageCreateTime(String lastPrivateMessageCreateTime) {
        this.lastPrivateMessageCreateTime = lastPrivateMessageCreateTime;
    }

    public int getAnotherGender() {
        return anotherGender;
    }

    public void setAnotherGender(int anotherGender) {
        this.anotherGender = anotherGender;
    }

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
