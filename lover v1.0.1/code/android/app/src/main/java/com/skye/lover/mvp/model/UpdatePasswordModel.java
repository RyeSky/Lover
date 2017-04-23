package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 更新密码模型
 */
public interface UpdatePasswordModel {
    /**
     * 更新密码
     *
     * @param context     上下文对象
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 穿越
     */
    Cross updatePassword(Context context, String oldPassword, String newPassword);
}
