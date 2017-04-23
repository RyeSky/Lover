package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

/**
 * app版本信息
 */
@Keep
public class AppVersion {
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * app版本号
     */
    @Expose
    private String appVersion;
    /**
     * 手机平台【0：android;1：ios】
     */
    @Expose
    private int platform;
    /**
     * 是否强制更新【0：不强制;1：强制】
     */
    @Expose
    private int forceUpdate;
    /**
     * app下载地址
     */
    @Expose
    private String downloadUrl;
    /**
     * 更新标题
     */
    @Expose
    private String title;
    /**
     * 更新内容
     */
    @Expose
    private String content;
    /**
     * app版本创建时间
     */
    @Expose
    private String createTime;


    public String getId() {
        return id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public int getPlatform() {
        return platform;
    }

    public boolean isForceUpdate() {
        return forceUpdate == 1;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreateTime() {
        return createTime;
    }

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
