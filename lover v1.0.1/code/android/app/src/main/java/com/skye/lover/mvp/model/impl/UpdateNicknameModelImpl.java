package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.UpdateNicknameModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新昵称模型实现
 */
public class UpdateNicknameModelImpl implements UpdateNicknameModel {
    @Override
    public Cross updateNickname(Context context, String nickname) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("nickname", nickname);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPDATE_NICKNAME, params, false, false).setExtra(nickname);
    }
}
