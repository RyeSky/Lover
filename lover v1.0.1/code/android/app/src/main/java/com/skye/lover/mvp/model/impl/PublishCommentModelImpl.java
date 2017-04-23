package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PublishCommentModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 发表评论模型实现
 */
public class PublishCommentModelImpl implements PublishCommentModel {
    @Override
    public Cross publishComment(Context context, String pillowTalkId, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("content", content);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PUBLISH_COMMENT, params, false, false);
    }
}
