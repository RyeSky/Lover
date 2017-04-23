package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 悄悄话或世界广播模型基类实现
 */
public class BasePillowTalkDetailModelImpl {
    /**
     * 悄悄话详情
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross pillowTalkDetail(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PILLOW_TALK_DETAIL, params, true, false);
    }

    /**
     * 悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross pillowTalkProperties(String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PILLOW_TALK_PROPERTIES, params, false, false);
    }


    /**
     * 删除悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross deletePillowTalk(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_PILLOW_TALK, params, false, false);
    }


    /**
     * 收藏悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross collect(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_COLLET, params, false, false);
    }


    /**
     * 取消收藏悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross cancelCollect(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_CANCEL_COLLECT, params, false, false);
    }

    /**
     * 赞悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross praise(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PRAISE, params, false, false);
    }

    /**
     * 取消赞悄悄话
     *
     * @param context      上下文对象
     * @param pillowTalkId 悄悄话或世界广播id
     * @return 穿越
     */
    public Cross cancelPraise(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_CANCEL_PRAISE, params, false, false);
    }
}
