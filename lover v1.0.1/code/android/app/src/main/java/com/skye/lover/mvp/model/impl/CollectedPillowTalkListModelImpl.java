package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.CollectedPillowTalkListModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户收藏的悄悄话列表模型实现
 */
public class CollectedPillowTalkListModelImpl implements CollectedPillowTalkListModel {
    private int page = 1;

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Cross refresh(Context context) {
        page = 1;
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_COLLETED_PILLOW_TALKS, params, true, true);
    }

    @Override
    public Cross loadMore(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_COLLETED_PILLOW_TALKS, params, true, true);
    }

    @Override
    public Cross cancelCollect(Context context, String pillowTalkId, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_CANCEL_COLLECT, params, false, false).setExtra(position);
    }
}
