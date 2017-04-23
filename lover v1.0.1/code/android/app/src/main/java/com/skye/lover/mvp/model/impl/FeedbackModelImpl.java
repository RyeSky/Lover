package com.skye.lover.mvp.model.impl;

import android.content.Context;
import android.os.Build;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.FeedbackModel;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈模型实现
 */
public class FeedbackModelImpl implements FeedbackModel {

    @Override
    public Cross submitFeedback(Context context, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("publisher", ShareDataUtil.get(context, User.ID));
        params.put("content", content);
        params.put("platform", "0");
        params.put("platformVersion", Build.VERSION.SDK);
        params.put("appVersion", CommonUtil.getVersionName(context));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_SUBMIT_FEEDBACK, params, false, false);
    }
}
