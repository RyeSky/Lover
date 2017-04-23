package com.skye.lover.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 悄悄话
 */
@Keep
public class PillowTalk implements Parcelable {
    public static final int OPEN = 1;//已经公开
    public static final int PILLOW_TALK = 0;//悄悄话
    public static final int BROADCAST = 1;//世界广播
    public static final Parcelable.Creator<PillowTalk> CREATOR = new Parcelable.Creator<PillowTalk>() {
        @Override
        public PillowTalk createFromParcel(Parcel source) {
            return new PillowTalk(source);
        }

        @Override
        public PillowTalk[] newArray(int size) {
            return new PillowTalk[size];
        }
    };
    /**
     * 记录
     */
    @Expose
    private String id;
    /**
     * 悄悄话的类型【0:悄悄话；1:广播】
     */
    @Expose
    private int type;
    /**
     * 发表者id
     */
    @Expose
    private String publisherId;
    /**
     * 发表者昵称
     */
    @Expose
    private String publisherNickname;
    /**
     * 发表者头像
     */
    @Expose
    private String publisherAvatar;
    /**
     * 发表者性别【0：保密；1：男；2：女】
     */
    @Expose
    private int publisherGender;
    /**
     * 悄悄话的对方id
     */
    @Expose
    private String anotherId;
    /**
     * 悄悄话的对方昵称
     */
    @Expose
    private String anotherNickname;
    /**
     * 悄悄话的对方头像
     */
    @Expose
    private String anotherAvatar;
    /**
     * 悄悄话的对方性别【0：保密；1：男；2：女】
     */
    @Expose
    private int anotherGender;
    /**
     * 内容
     */
    @Expose
    private String content;
    /**
     * 图片路径，多张图片用逗号分隔
     */
    @Expose
    private String imgs;
    /**
     * 悄悄话发布者愿意公开悄悄话内容
     */
    @Expose
    private int publisherOpen;
    /**
     * 悄悄话另一方同意公开悄悄话内容
     */
    @Expose
    private int anotherOpen;
    /**
     * 赞数量
     */
    @Expose
    private int praiseCount;
    /**
     * 评论数量
     */
    @Expose
    private int commentCount;
    /**
     * 登录用户赞这条悄悄话产生的赞记录id
     */
    @Expose
    private String praiseId;
    /**
     * 登录用户收藏这条悄悄话产生的收藏记录id
     */
    @Expose
    private String collectId;
    /**
     * 悄悄话创建时间
     */
    @Expose
    private String createTime;

    public PillowTalk() {
    }

    protected PillowTalk(Parcel in) {
        this.id = in.readString();
        this.type = in.readInt();
        this.publisherId = in.readString();
        this.publisherNickname = in.readString();
        this.publisherAvatar = in.readString();
        this.publisherGender = in.readInt();
        this.anotherId = in.readString();
        this.anotherNickname = in.readString();
        this.anotherAvatar = in.readString();
        this.anotherGender = in.readInt();
        this.content = in.readString();
        this.imgs = in.readString();
        this.publisherOpen = in.readInt();
        this.anotherOpen = in.readInt();
        this.praiseCount = in.readInt();
        this.commentCount = in.readInt();
        this.praiseId = in.readString();
        this.collectId = in.readString();
        this.createTime = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherNickname() {
        return publisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        this.publisherNickname = publisherNickname;
    }

    public String getPublisherAvatar() {
        return publisherAvatar;
    }

    public void setPublisherAvatar(String publisherAvatar) {
        this.publisherAvatar = publisherAvatar;
    }

    public AvatarTag getPublisherAvatarTag() {
        return new AvatarTag(publisherAvatar, publisherGender);
    }

    public String getAnotherId() {
        return anotherId;
    }

    public void setAnotherId(String anotherId) {
        this.anotherId = anotherId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
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

    public int getPublisherGender() {
        return publisherGender;
    }

    public void setPublisherGender(int publisherGender) {
        this.publisherGender = publisherGender;
    }

    public int getAnotherGender() {
        return anotherGender;
    }

    public void setAnotherGender(int anotherGender) {
        this.anotherGender = anotherGender;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPublisherOpen() {
        return publisherOpen;
    }

    public void setPublisherOpen(int publisherOpen) {
        this.publisherOpen = publisherOpen;
    }

    public int getAnotherOpen() {
        return anotherOpen;
    }

    public void setAnotherOpen(int anotherOpen) {
        this.anotherOpen = anotherOpen;
    }

    public boolean isOpen() {
        return publisherOpen == OPEN && anotherOpen == OPEN;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public boolean isCollected() {
        return !TextUtils.isEmpty(collectId);
    }

    public String getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(String praiseId) {
        this.praiseId = praiseId;
    }

    public boolean isPraised() {
        return !TextUtils.isEmpty(praiseId);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.publisherId);
        dest.writeString(this.publisherNickname);
        dest.writeString(this.publisherAvatar);
        dest.writeInt(this.publisherGender);
        dest.writeString(this.anotherId);
        dest.writeString(this.anotherNickname);
        dest.writeString(this.anotherAvatar);
        dest.writeInt(this.anotherGender);
        dest.writeString(this.content);
        dest.writeString(this.imgs);
        dest.writeInt(this.publisherOpen);
        dest.writeInt(this.anotherOpen);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.commentCount);
        dest.writeString(this.praiseId);
        dest.writeString(this.collectId);
        dest.writeString(this.createTime);
    }

    @Override
    public String toString() {
        return "PillowTalk{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", publisherId='" + publisherId + '\'' +
                ", publisherNickname='" + publisherNickname + '\'' +
                ", publisherAvatar='" + publisherAvatar + '\'' +
                ", publisherGender=" + publisherGender +
                ", anotherId='" + anotherId + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", anotherGender=" + anotherGender +
                ", content='" + content + '\'' +
                ", imgs='" + imgs + '\'' +
                ", publisherOpen=" + publisherOpen +
                ", anotherOpen=" + anotherOpen +
                ", praiseCount=" + praiseCount +
                ", commentCount=" + commentCount +
                ", praiseId='" + praiseId + '\'' +
                ", collectId='" + collectId + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
