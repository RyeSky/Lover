package com.skye.lover.model;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 意见反馈
 */
public class Feedback {
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
    public static final String TABLE_NAME = "feedback";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 意见反馈发表者id
     */
    public static final String PUBLISHER = "publisher";
    /**
     * 反馈内容
     */
    public static final String CONTENT = "content";
    /**
     * 手机平台【0：android;1:ios】
     */
    public static final String PLATFORM = "platform";
    /**
     * 平台系统版本号
     */
    public static final String PLATFORM_VERSION = "platform_version";
    /**
     * app版本号
     */
    public static final String APP_VERSION = "app_version";
    /**
     * 意见反馈创建时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 意见反馈发表者id
     */
    @Expose
    public String publisher;
    /**
     * 反馈内容
     */
    @Expose
    public String content;
    /**
     * 手机平台【0：android;1:ios】
     */
    @Expose
    public String platformVersion;
    /**
     * 平台系统版本号
     */
    @Expose
    public int platform;
    /**
     * app版本号
     */
    @Expose
    public String appVersion;
    /**
     * 意见反馈创建时间
     */
    @Expose
    public String createTime;

    /**
     * 检查参数有效性
     *
     * @return 参数是否有效
     */
    public boolean check() {
        return !CommonUtil.isBlank(publisher) && !CommonUtil.isBlank(content) && (platform == PLATFORM_ANDROID || platform == PLATFORM_IOS)
                && !CommonUtil.isBlank(platformVersion) && !CommonUtil.isBlank(appVersion);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "publisher='" + publisher + '\'' +
                ", content='" + content + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", createTime='" + createTime + '\'' +
                ", platform=" + platform +
                '}';
    }
}
