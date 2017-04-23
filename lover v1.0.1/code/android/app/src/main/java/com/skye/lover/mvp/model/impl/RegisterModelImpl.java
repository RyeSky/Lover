package com.skye.lover.mvp.model.impl;

import android.text.TextUtils;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.model.RegisterModel;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册模型实现
 */
public class RegisterModelImpl implements RegisterModel {
    @Override
    public Cross register(String name, String password, String gender) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", CommonUtil.md5(password));
        params.put("gender", TextUtils.isEmpty(gender) ? "0" : gender);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_REGISTER, params, false, false);
    }
}
