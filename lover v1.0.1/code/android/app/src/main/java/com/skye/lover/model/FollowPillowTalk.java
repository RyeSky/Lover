package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 跟随消息话实体类
 */
@Keep
public class FollowPillowTalk {
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * 被回复的悄悄话id
     */
    @Expose
    private String pillowTalkId;
    /**
     * 发表者id
     */
    @Expose
    private String publisher;
    /**
     * 发表者昵称
     */
    @Expose
    private String nickname;
    /**
     * 发表者头像
     */
    @Expose
    private String avatar;
    /**
     * 跟随悄悄话内容
     */
    @Expose
    private String content;
    /**
     * 跟随悄悄话发表时间
     */
    @Expose
    private String createTime;
    /**
     * 发表者性别【0：保密；1：男；2：女】
     */
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
