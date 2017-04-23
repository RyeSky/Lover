package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.UpdateBirthdayModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新生日模型实现
 */
public class UpdateBirthdayModelImpl implements UpdateBirthdayModel {
    @Override
    public Cross updateBirthday(Context context, String birthday) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("birthday", birthday);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPDATE_BIRTHDAY, params, false, false).setExtra(birthday);
    }
}
