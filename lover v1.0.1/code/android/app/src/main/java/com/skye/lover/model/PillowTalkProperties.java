package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

/**
 * 悄悄话部分信息
 */
@Keep
public class PillowTalkProperties {
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

    public int getPublisherOpen() {
        return publisherOpen;
    }

    public int getAnotherOpen() {
        return anotherOpen;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
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
