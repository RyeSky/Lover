package com.skye.lover.pillowtalk.pillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 悄悄话部分属性请求实体类
 */
public class PillowTalkPropertiesRequest implements RequestParameterCheck {
    /**
     * 悄悄话id
     */
    @Expose
    public String pillowTalkId;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId);
    }

    @Override
    public String toString() {
        return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + '}';
    }
}
