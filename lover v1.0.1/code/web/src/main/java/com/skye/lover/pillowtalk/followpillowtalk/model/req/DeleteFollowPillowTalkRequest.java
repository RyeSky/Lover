package com.skye.lover.pillowtalk.followpillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 删除跟随悄悄话请求实体类
 */
public class DeleteFollowPillowTalkRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 跟随悄悄话id
     */
    @Expose
    public String followPillowTalkId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(followPillowTalkId);
    }

    @Override
    public String toString() {
        return "DeleteFollowPillowTalkRequest{" +
                "userId='" + userId + '\'' +
                ", followPillowTalkId='" + followPillowTalkId + '\'' +
                '}';
    }
}
