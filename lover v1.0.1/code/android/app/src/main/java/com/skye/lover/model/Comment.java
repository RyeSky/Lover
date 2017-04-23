package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 评论实体类
 */
@Keep
public class Comment {
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * 被评论的悄悄话或世界广播的id
     */
    @Expose
    private String pillowTalkId;
    /**
     * 评论者id
     */
    @Expose
    private String commenter;
    /**
     * 评论者昵称
     */
    @Expose
    private String nickname;
    /**
     * 评论者头像
     */
    @Expose
    private String avatar;
    /**
     * 评论者性别【0：保密；1：男；2：女】
     */
    @Expose
    private int gender;
    /**
     * 评论内容
     */
    @Expose
    private String content;
    /**
     * 评论时间
     */
    @Expose
    private String createTime;

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

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", commenter='" + commenter + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", gender=" + gender +
                '}';
    }
}
