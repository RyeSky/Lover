package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 悄悄话部分信息
 */
public class PillowTalkProperties {
    /**
     * 悄悄话发布者愿意公开悄悄话内容
     */
    @Expose
    public int publisherOpen;
    /**
     * 悄悄话另一方同意公开悄悄话内容
     */
    @Expose
    public int anotherOpen;
    /**
     * 赞数量
     */
    @Expose
    public int praiseCount;
    /**
     * 评论数量
     */
    @Expose
    public int commentCount;

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

    @Override
    public String toString() {
        return "PillowTalkProperties{" +
                "praiseCount=" + praiseCount +
                ", commentCount=" + commentCount +
                ", publisherOpen=" + publisherOpen +
                ", anotherOpen=" + anotherOpen +
                '}';
    }
}
