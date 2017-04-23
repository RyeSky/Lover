package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

/**
 * 其他用户信息模型
 */
public interface OtherInfoModel {
    /**
     * 其他用户信息
     *
     * @param userId 用户id
     * @return 穿越
     */
    Cross otherInfo(String userId);

    /**
     * 分手
     *
     * @param context 上下文对象
     * @param another 用户id
     * @return 穿越
     */
    Cross breakUp(Context context, String another);

    /**
     * 坠入爱河
     *
     * @param context 上下文对象
     * @param another 用户id
     * @return 穿越
     */
    Cross fallInLove(Context context, String another);
}
