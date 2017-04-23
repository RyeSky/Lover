package com.skye.lover.common.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.common.model.resp.AppVersion;

/**
 * 版本更新请求实体类
 */
public class AppVersionRequest implements RequestParameterCheck {
    /**
     * 手机平台【0：android;1：ios】
     */
    @Expose
    public int platform;

    @Override
    public boolean check() {
        return platform == AppVersion.PLATFORM_ANDROID || platform == AppVersion.PLATFORM_IOS;
    }

    @Override
    public String toString() {
        return "AppVersionRequest{" +
                "platform=" + platform +
                '}';
    }
}
