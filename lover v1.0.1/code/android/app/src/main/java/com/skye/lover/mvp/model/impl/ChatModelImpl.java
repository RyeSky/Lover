package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.ChatModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊聊模型实现
 */
public class ChatModelImpl implements ChatModel {
    /**
     * 刷新
     *
     * @param context 上下文对象
     * @return 穿越
     */
    @Override
    public Cross refresh(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PRIVATE_MESSAGE_SESSIONS, params, true, true);
    }

    @Override
    public Cross deleteByPrivateMessageSession(Context context, String another, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("another", another);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_PRIVATE_MESSAGE_SESSION, params, false, false).setExtra(position);
    }
}
