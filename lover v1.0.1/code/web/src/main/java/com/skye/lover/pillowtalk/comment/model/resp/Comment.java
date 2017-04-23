package com.skye.lover.pillowtalk.comment.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 评论实体类
 */
public class Comment {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "comment_pillow_talk";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 被评论的悄悄话或世界广播的id
     */
    public static final String PILLOW_TALK_ID = "pillow_talk_id";
    /**
     * 评论者id
     */
    public static final String COMMENTER = "commenter";
    /**
     * 评论内容
     */
    public static final String CONTENT = "content";
    /**
     * 评论是否被删除【0：没有被删除；1：已经被删除】
     */
    public static final String DELETED = "deleted";
    /**
     * 评论时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 被评论的悄悄话或世界广播的id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 评论者id
     */
    @Expose
    public String commenter;
    /**
     * 评论者昵称
     */
    @Expose
    public String nickname;
    /**
     * 评论者头像
     */
    @Expose
    public String avatar;
    /**
     * 评论者性别【0：保密；1：男；2：女】
     */
    @Expose
    public int gender;
    /**
     * 评论内容
     */
    @Expose
    public String content;
    /**
     * 评论时间
     */
    @Expose
    public String createTime;

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
