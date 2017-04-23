package com.skye.lover.mvp.model.impl;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.model.LoginModel;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录模型实现
 */
public class LoginModelImpl implements LoginModel {
    /**
     * 登录
     *
     * @param name     用户名
     * @param password 密码
     * @return 穿越
     */
    @Override
    public Cross login(String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", CommonUtil.md5(password));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_LOGIN, params, false, false);
    }
}
