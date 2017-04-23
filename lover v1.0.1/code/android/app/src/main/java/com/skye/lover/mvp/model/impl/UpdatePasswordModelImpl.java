package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.UpdatePasswordModel;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新密码模型实现
 */
public class UpdatePasswordModelImpl implements UpdatePasswordModel {
    @Override
    public Cross updatePassword(Context context, String oldPassword, String newPassword) {
        String password = CommonUtil.md5(newPassword);
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("oldPassword", CommonUtil.md5(oldPassword));
        params.put("newPassword", password);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPDATE_PASSWORD, params, false, false).setExtra(password);
    }
}
