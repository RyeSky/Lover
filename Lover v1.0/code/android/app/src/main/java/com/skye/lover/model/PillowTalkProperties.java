package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 悄悄话部分信息
 */
public class PillowTalkProperties {
    // 赞数量、评论数量、悄悄话是否公开
    @Expose
    private int praiseCount, commentCount,
            publisherOpen, anotherOpen;

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
