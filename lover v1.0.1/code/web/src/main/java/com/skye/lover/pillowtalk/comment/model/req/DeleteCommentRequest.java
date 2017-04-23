package com.skye.lover.pillowtalk.comment.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 删除评论请求实体类
 */
public class DeleteCommentRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 评论id
     */
    @Expose
    public String commentId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(commentId);
    }

    @Override
    public String toString() {
        return "DeleteCommentRequest{" +
                "userId='" + userId + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }
}
