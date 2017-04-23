package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PillowTalkDetailModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 悄悄话详情模型实现
 */
public class PillowTalkDetailModelImpl extends BasePillowTalkDetailModelImpl implements PillowTalkDetailModel {
    @Override
    public Cross open(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_OPEN_PILLOW_TALK, params, false, false);
    }

    @Override
    public Cross follows(String pillowTalkId, int page, ScrollPosition position) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("page", page + "");
        Map<String, Object> extra = new HashMap<>(2);
        extra.put("page", page);
        extra.put("position", position);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_FOLLOWS, params, true, false).setExtra(extra);
    }

    @Override
    public Cross deleteFollowPillowTalk(Context context, FollowPillowTalk fpt) {
        Map<String, String> params = new HashMap<>();
        params.put("followPillowTalkId", fpt.getId());
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_FOLLOW_PILLOW_TALK, params, false, false).setExtra(fpt);
    }
}
