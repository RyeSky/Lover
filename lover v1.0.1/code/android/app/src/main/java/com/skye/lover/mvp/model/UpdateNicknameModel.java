package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 更新昵称模型
 */
public interface UpdateNicknameModel {
    /**
     * 更新昵称
     *
     * @param context  上下文对象
     * @param nickname 新昵称
     * @return 穿越
     */
    Cross updateNickname(Context context, String nickname);
}
