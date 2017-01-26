package com.skye.lover.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

import java.io.Serializable;

/**
 * 悄悄话
 */
public class PillowTalk implements Serializable {
    public static final int OPEN = 1;//已经公开
    public static final int PILLOW_TALK = 0;//悄悄话
    public static final int BROADCAST = 1;//世界广播
    // 悄悄话id，发布者id，发布者昵称，发布者头像，另一方id，另一方昵称，另一方头像，悄悄话内容，图片，收藏记录id，赞记录id，创建时间
    @Expose
    private String id, publisherId, publisherNickname, publisherAvatar,
            anotherId, anotherNickname, anotherAvatar, content, imgs, collectId, praiseId,
            createTime;
    // 发布者性别，另一方性别、赞数量、评论数量
    @Expose
    private int publisherGender, anotherGender, praiseCount, commentCount, publisherOpen, anotherOpen, type;

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
    public String toString() {
        return "PillowTalk{" +
                "id='" + id + '\'' +
                ", publisherId='" + publisherId + '\'' +
                ", publisherNickname='" + publisherNickname + '\'' +
                ", publisherAvatar='" + publisherAvatar + '\'' +
                ", anotherId='" + anotherId + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", content='" + content + '\'' +
                ", imgs='" + imgs + '\'' +
                ", collectId='" + collectId + '\'' +
                ", praiseId='" + praiseId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", publisherGender=" + publisherGender +
                ", anotherGender=" + anotherGender +
                ", praiseCount=" + praiseCount +
                ", commentCount=" + commentCount +
                ", publisherOpen=" + publisherOpen +
                ", anotherOpen=" + anotherOpen +
                ", type=" + type +
                '}';
    }
}
