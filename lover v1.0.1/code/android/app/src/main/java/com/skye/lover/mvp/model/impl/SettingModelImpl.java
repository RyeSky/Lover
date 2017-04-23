package com.skye.lover.mvp.model.impl;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.model.SettingModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置模型实现
 */
public class SettingModelImpl implements SettingModel {
    @Override
    public Cross lastAppVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("platform", "0");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_LAST_APP_VERSION, params, false, false);
    }
}
