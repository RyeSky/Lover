package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PrivateMessageModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 私信聊天模型实现
 */
public class PrivateMessageModelImpl implements PrivateMessageModel {
    @Override
    public Cross refresh(Context context, String another, String privateMessageId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("another", another);
        params.put("privateMessageId", privateMessageId);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_PRIVATE_MESSAGES, params, true, true);
    }

    @Override
    public Cross deletePrivateMessage(Context context, String privateMessageId, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("privateMessageId", privateMessageId);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_BY_PRIVATE_MESSAGE_ID, params, false, false).setExtra(position);
    }
}
