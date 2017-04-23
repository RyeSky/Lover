package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

import java.io.File;

/**
 * 头像模型
 */
public interface AvatarModel {
    /**
     * 上传头像文件
     *
     * @param file 文件
     * @return 穿越
     */
    Cross uploadAvatarFile(File file);

    /**
     * 更新用户头像
     *
     * @param context 上下文对象
     * @param avatar 头像url
     * @return 穿越
     */
    Cross updateAvatar(Context context,String avatar);
}
