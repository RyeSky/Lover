package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PillowTalkListModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 发现模型
 */
public class FindModelImpl implements PillowTalkListModel {
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
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_FIND, params, true, true);
    }

    @Override
    public Cross loadMore(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_FIND, params, true, true);
    }
}
