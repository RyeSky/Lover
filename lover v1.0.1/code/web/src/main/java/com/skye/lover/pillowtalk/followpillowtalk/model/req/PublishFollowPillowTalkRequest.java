package com.skye.lover.pillowtalk.followpillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 发表跟随悄悄话请求实体类
 */
public class PublishFollowPillowTalkRequest implements RequestParameterCheck {
    /**
     * 发表者
     */
    @Expose
    public String userId;
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 跟随悄悄话内容
     */
    @Expose
    public String content;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId)
                && !CommonUtil.isBlank(userId)
                && !CommonUtil.isBlank(content);
    }

    @Override
    public String toString() {
        return "PublishFollowPillowTalkRequest{" +
                "userId='" + userId + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
