package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PublishPillowTalkModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表悄悄话模型实现
 */
public class PublishPillowTalkModelImpl implements PublishPillowTalkModel {
    @Override
    public Cross uploadFiles(List<File> files) {
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPLOAD_FILE, files);
    }

    @Override
    public Cross publishPillowTalk(Context context, String type, String content, String imgs) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("type", type);
        params.put("content", content);
        params.put("imgs", imgs);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PUBLISH_PILLOW_TALK, params, false, false);
    }
}
