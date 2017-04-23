package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Comment;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.BroadcastDetailModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 世界广播详情模型实现
 */
public class BroadcastDetailModelImpl extends BasePillowTalkDetailModelImpl implements BroadcastDetailModel {
    @Override
    public Cross comments(String pillowTalkId, int page, ScrollPosition position) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("page", page + "");
        Map<String, Object> extra = new HashMap<>(2);
        extra.put("page", page);
        extra.put("position", position);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_COMMENTS, params, true, false).setExtra(extra);
    }

    @Override
    public Cross deleteComment(Context context, Comment comment) {
        Map<String, String> params = new HashMap<>();
        params.put("commentId", comment.getId());
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_COMMENT, params, false, false).setExtra(comment);
    }
}
