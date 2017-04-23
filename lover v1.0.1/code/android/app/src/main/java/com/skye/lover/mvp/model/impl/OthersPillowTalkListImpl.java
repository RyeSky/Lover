package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.mvp.model.PillowTalkListModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 其他用户发表的悄悄话列表模型实现
 */
public class OthersPillowTalkListImpl implements PillowTalkListModel {
    private int page = 1;
    private String userId;

    public OthersPillowTalkListImpl(String userId) {
        this.userId = userId;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Cross refresh(Context context) {
        page = 1;
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_OTHERS_PILLOW_TALKS, params, true, true);
    }

    @Override
    public Cross loadMore(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_OTHERS_PILLOW_TALKS, params, true, true);
    }
}
