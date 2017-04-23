package com.skye.lover.mvp.model.impl;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.model.MainModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 主模型实现
 */
public class MainModelImpl implements MainModel {
    @Override
    public Cross lastAppVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("platform", "0");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_LAST_APP_VERSION, params, false, false);
    }
}
