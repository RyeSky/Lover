package com.skye.lover.model;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 跟随消息话实体类
 */
public class FollowPillowTalk {
    //id，悄悄话id，发布者id，昵称，头像，内容，发布时间
    @Expose
    private String id, pillowTalkId, publisher, nickname, avatar, content, createTime;
    //性别
    @Expose
    private int gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPillowTalkId() {
        return pillowTalkId;
    }

    public void setPillowTalkId(String pillowTalkId) {
        this.pillowTalkId = pillowTalkId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowPillowTalk that = (FollowPillowTalk) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "FollowPillowTalk{" +
                "id='" + id + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", publisher='" + publisher + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", gender=" + gender +
                '}';
    }
}
