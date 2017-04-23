package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.user.model.resp.User;
import com.skye.lover.util.CommonUtil;

/**
 * 更新用户性别实体类
 */
public class UpdateGenderRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    public int gender;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && (gender == User.GENDER_SECRET || gender == User.GENDER_MALE || gender == User.GENDER_FEMALE);
    }

    @Override
    public String toString() {
        return "UpdateGenderRequest{" +
                "userId='" + userId + '\'' +
                ", gender=" + gender +
                '}';
    }
}
