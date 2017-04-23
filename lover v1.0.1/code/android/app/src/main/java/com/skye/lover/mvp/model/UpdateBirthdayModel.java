package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 更新生日模型
 */
public interface UpdateBirthdayModel {
    /**
     * 更新生日
     *
     * @param context  上下文对象
     * @param birthday 新生日
     * @return 穿越
     */
    Cross updateBirthday(Context context, String birthday);
}
