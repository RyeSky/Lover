package com.skye.lover.user.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 更新用户昵称请求实体类
 */
public class UpdateNicknameRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 昵称
     */
    @Expose
    public String nickname;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(nickname);
    }

    @Override
    public String toString() {
        return "UpdateNicknameRequest{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
