package com.skye.lover.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 私信实体类
 */
public class PrivateMessage implements Parcelable {
    private final long THREE_MINUTE = 1000 * 60 * 3;//3分钟
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * 私信发送者id
     */
    @Expose
    private String sender;
    /**
     * 私信发送者昵称
     */
    @Expose
    private String senderNickname;
    /**
     * 私信发送者头像
     */
    @Expose
    private String senderAvatar;
    /**
     * 私信发送者性别【0：保密；1：男；2：女】
     */
    @Expose
    private int senderGender;
    /**
     * 私信接收者id
     */
    @Expose
    private String receiver;
    /**
     * 私信接收者昵称
     */
    @Expose
    private String receiverNickname;
    /**
     * 私信接收者头像
     */
    @Expose
    private String receiverAvatar;
    /**
     * 私信接收者性别【0：保密；1：男；2：女】
     */
    @Expose
    private int receiverGender;
    /**
     * 聊天内容
     */
    @Expose
    private String content;
    /**
     * 消息创建时间
     */
    @Expose
    private String createTime;

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

    public int getReceiverGender() {
        return receiverGender;
    }

    public void setReceiverGender(int receiverGender) {
        this.receiverGender = receiverGender;
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
        long time = 0L;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.sender);
        dest.writeString(this.senderNickname);
        dest.writeString(this.senderAvatar);
        dest.writeInt(this.senderGender);
        dest.writeString(this.receiver);
        dest.writeString(this.receiverNickname);
        dest.writeString(this.receiverAvatar);
        dest.writeInt(this.receiverGender);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
    }

    public PrivateMessage() {
    }

    protected PrivateMessage(Parcel in) {
        this.id = in.readString();
        this.sender = in.readString();
        this.senderNickname = in.readString();
        this.senderAvatar = in.readString();
        this.senderGender = in.readInt();
        this.receiver = in.readString();
        this.receiverNickname = in.readString();
        this.receiverAvatar = in.readString();
        this.receiverGender = in.readInt();
        this.content = in.readString();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<PrivateMessage> CREATOR = new Parcelable.Creator<PrivateMessage>() {
        @Override
        public PrivateMessage createFromParcel(Parcel source) {
            return new PrivateMessage(source);
        }

        @Override
        public PrivateMessage[] newArray(int size) {
            return new PrivateMessage[size];
        }
    };
}
