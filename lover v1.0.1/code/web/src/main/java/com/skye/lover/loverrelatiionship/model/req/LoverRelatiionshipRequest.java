package com.skye.lover.loverrelatiionship.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 恋爱关系请求实体类
 */
public class LoverRelatiionshipRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 恋爱关系中的另一方id
     */
    @Expose
    public String another;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(another) && !CommonUtil.isBlank(userId);
    }

    @Override
    public String toString() {
        return "BreakUpRequest{" +
                "userId='" + userId + '\'' +
                ", another='" + another + '\'' +
                '}';
    }
}
