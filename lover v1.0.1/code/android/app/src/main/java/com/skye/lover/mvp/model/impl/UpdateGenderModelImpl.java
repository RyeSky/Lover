package com.skye.lover.mvp.model.impl;


import android.content.Context;
import android.text.TextUtils;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.UpdateGenderModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新性别模型实现
 */
public class UpdateGenderModelImpl implements UpdateGenderModel {
    @Override
    public Cross updateGender(Context context, String gender) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("gender", TextUtils.isEmpty(gender) ? "0" : gender);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPDATE_GENDER, params, false, false).setExtra(gender);
    }
}
