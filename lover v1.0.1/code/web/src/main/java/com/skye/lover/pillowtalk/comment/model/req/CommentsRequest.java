package com.skye.lover.pillowtalk.comment.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 评论列表请求实体类
 */
public class CommentsRequest implements RequestParameterCheck {
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 请求页数
     */
    @Expose
    public int page;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId) && page > 0;
    }

    @Override
    public String toString() {
        return "CommentsRequest{" +
                "pillowTalkId='" + pillowTalkId + '\'' +
                ", page=" + page +
                '}';
    }
}
