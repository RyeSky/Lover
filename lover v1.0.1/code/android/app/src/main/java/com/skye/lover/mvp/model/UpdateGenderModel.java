package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 更新性别模型
 */
public interface UpdateGenderModel {
    /**
     * 更新性别
     *
     * @param context 上下文对象
     * @param gender  新性别
     * @return 穿越
     */
    Cross updateGender(Context context, String gender);
}
