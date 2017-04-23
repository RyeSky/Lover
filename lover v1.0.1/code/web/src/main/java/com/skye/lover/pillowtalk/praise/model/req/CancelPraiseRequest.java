package com.skye.lover.pillowtalk.praise.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 取消赞悄悄话请求实体类
 */
public class CancelPraiseRequest implements RequestParameterCheck {
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 赞者id
     */
    @Expose
    public String userId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(userId);
    }

    @Override
    public String toString() {
        return "CancelPraiseRequest{" +
                "pillowTalkId='" + pillowTalkId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
