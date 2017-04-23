package com.skye.lover.pillowtalk.pillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 公开悄悄话请求实体类
 */
public class OpenPillowTalkRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(pillowTalkId);
    }

    @Override
    public String toString() {
        return "OpenPillowTalkRequest{" +
                "userId='" + userId + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                '}';
    }
}
