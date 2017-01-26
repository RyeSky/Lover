package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 跟随悄悄话实体类
 */
public class FollowPillowTalk {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "follow_pillow_talk";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 被回复的悄悄话id
     */
    public static final String PILLOW_TALK_ID = "pillow_talk_id";
    /**
     * 发表者id
     */
    public static final String PUBLISHER = "publisher";
    /**
     * 跟随悄悄话内容
     */
    public static final String CONTENT = "content";
    /**
     * 跟随悄悄话是否被删除【0：没有被删除；1：已经被删除】
     */
    public static final String DELETED = "deleted";
    /**
     * 跟随悄悄话发表时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 被回复的悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 发表者id
     */
    @Expose
    public String publisher;
    /**
     * 发表者昵称
     */
    @Expose
    public String nickname;
    /**
     * 发表者头像
     */
    @Expose
    public String avatar;
    /**
     * 跟随悄悄话内容
     */
    @Expose
    public String content;
    /**
     * 跟随悄悄话发表时间
     */
    @Expose
    public String createTime;
    /**
     * 发表者性别【0：保密；1：男；2：女】
     */
    @Expose
    public int gender;

    @Override
    public String toString() {
        return "FollowPillowTalk{" +
                "id='" + id + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", publisher='" + publisher + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
