package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.ReportPillowTalkModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 举报悄悄话模型实现
 */
public class ReportPillowTalkModelImpl implements ReportPillowTalkModel {
    @Override
    public Cross reportPillowTalk(Context context, String pillowTalkId, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("reporter", ShareDataUtil.get(context, User.ID));
        params.put("pillowTalkId", pillowTalkId);
        params.put("content", content);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_REPORT_PILLOW_TALK, params, false, false);
    }
}
