package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.AvatarModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 头像模型实现
 */
public class AvatarModelImpl implements AvatarModel {
    @Override
    public Cross uploadAvatarFile(File file) {
        List<File> files = new ArrayList<>(1);
        files.add(file);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPLOAD_FILE, files);
    }

    @Override
    public Cross updateAvatar(Context context, String avatar) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("avatar", avatar);
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_UPDATE_AVATAR, params, false, false).setExtra(avatar);
    }
}
