package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.OtherInfoModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 其他用户信心模型实现
 */
public class OtherInfoModelImpl implements OtherInfoModel {
    @Override
    public Cross otherInfo(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_OTHER_INFO, params, true, false);
    }

    @Override
    public Cross breakUp(Context context, String another) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("another", another);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_BREAK_UP, params, false, false);
    }

    @Override
    public Cross fallInLove(Context context, String another) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("another", another);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_FALL_IN_LOVE, params, false, false);
    }
}
