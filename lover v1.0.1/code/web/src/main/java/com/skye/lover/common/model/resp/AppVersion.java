package com.skye.lover.common.model.resp;

import com.google.gson.annotations.Expose;

/**
 * app版本信息
 */
public class AppVersion {
    /**
     * ANDROID
     */
    public static final int PLATFORM_ANDROID = 0;
    /**
     * IOS
     */
    public static final int PLATFORM_IOS = 1;
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "app_version";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * app版本号
     */
    public static final String APP_VERSION = "app_version";
    /**
     * 手机平台【0：android;1：ios】
     */
    public static final String PLATFORM = "platform";
    /**
     * 是否强制更新【0：不强制;1：强制】
     */
    public static final String FORCE_UPDATE = "force_update";
    /**
     * app下载地址
     */
    public static final String DOWNLOAD_URL = "download_url";
    /**
     * 更新标题
     */
    public static final String TITLE = "title";
    /**
     * 更新内容
     */
    public static final String CONTENT = "content";
    /**
     * app版本创建时间
     */
    public static final String CREATE_TIME = "create_time";

    public AppVersion() {
    }

    public AppVersion(int platform) {
        this.appVersion = "1.0";
        this.platform = platform;
    }

    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * app版本号
     */
    @Expose
    public String appVersion;
    /**
     * 手机平台【0：android;1：ios】
     */
    @Expose
    public int platform;
    /**
     * 是否强制更新【0：不强制;1：强制】
     */
    @Expose
    public int forceUpdate;
    /**
     * app下载地址
     */
    @Expose
    public String downloadUrl;
    /**
     * 更新标题
     */
    @Expose
    public String title;
    /**
     * 更新内容
     */
    @Expose
    public String content;
    /**
     * app版本创建时间
     */
    @Expose
    public String createTime;

    @Override
    public String toString() {
        return "AppVersion{" +
                "id='" + id + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", platform=" + platform +
                ", forceUpdate=" + forceUpdate +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
