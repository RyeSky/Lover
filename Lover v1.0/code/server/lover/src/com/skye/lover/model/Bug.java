package com.skye.lover.model;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * bug信息
 */
public class Bug {
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
    public static final String TABLE_NAME = "bugs";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 手机品牌
     */
    public static final String MOBILE_BRAND = "mobile_brand";
    /**
     * 手机型号
     */
    public static final String MOBILE_VERSION = "mobile_version";
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
     * bug日志
     */
    public static final String BUG_DETAILS = "bug_details";
    /**
     * bug创建时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 手机品牌
     */
    @Expose
    public String mobileBrand;
    /**
     * 手机型号
     */
    @Expose
    public String mobileVersion;
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
     * bug日志
     */
    @Expose
    public String bugDetails;
    /**
     * bug创建时间
     */
    @Expose
    public String createTime;

    /**
     * 检查参数有效性
     *
     * @return 参数是否有效
     */
    public boolean check() {
        return !CommonUtil.isBlank(mobileBrand) && !CommonUtil.isBlank(mobileVersion) && (platform == PLATFORM_ANDROID || platform == PLATFORM_IOS)
                && !CommonUtil.isBlank(platformVersion) && !CommonUtil.isBlank(appVersion) && !CommonUtil.isBlank(bugDetails);
    }

    @Override
    public String toString() {
        return "Bug [mobileBrand=" + mobileBrand + ", mobileVersion="
                + mobileVersion + ", platformVersion=" + platformVersion
                + ", appVersion=" + appVersion + ", bugDetails=" + bugDetails
                + ", createTime=" + createTime + ", platform=" + platform + "]";
    }
}
