package com.skye.lover.pillowtalk.comment.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 发表评论请求实体类
 */
public class PublishCommentRequest implements RequestParameterCheck {
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 评论内容
     */
    @Expose
    public String content;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(content);
    }

    @Override
    public String toString() {
        return "PublishCommentRequest{" +
                "pillowTalkId='" + pillowTalkId + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
