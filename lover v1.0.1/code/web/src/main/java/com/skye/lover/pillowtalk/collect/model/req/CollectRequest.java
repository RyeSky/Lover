package com.skye.lover.pillowtalk.collect.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 收藏悄悄话请求实体类
 */
public class CollectRequest implements RequestParameterCheck {
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 收藏者id
     */
    @Expose
    public String userId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(userId);
    }

    @Override
    public String toString() {
        return "CollectRequest{" +
                "pillowTalkId='" + pillowTalkId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
