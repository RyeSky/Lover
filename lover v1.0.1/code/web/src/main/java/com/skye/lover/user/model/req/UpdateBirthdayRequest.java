package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 更新用户生日请求实体类
 */
public class UpdateBirthdayRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 生日日期
     */
    @Expose
    public String birthday;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(birthday);
    }

    @Override
    public String toString() {
        return "UpdateBirthdayRequest{" +
                "userId='" + userId + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
