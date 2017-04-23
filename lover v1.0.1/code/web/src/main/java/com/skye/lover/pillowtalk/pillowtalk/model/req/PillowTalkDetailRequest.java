package com.skye.lover.pillowtalk.pillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 悄悄话详情请求实体类
 */
public class PillowTalkDetailRequest implements RequestParameterCheck {
    /**
     * 登录用户id
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
        return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(userId);
    }

    @Override
    public String toString() {
        return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + ", userId='" + userId + '\'' + '}';
    }
}
