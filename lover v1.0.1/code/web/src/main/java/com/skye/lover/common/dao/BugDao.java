package com.skye.lover.common.dao;

/**
 * bugDao
 */
public interface BugDao {
    /**
     * 插入bug日志数据
     *
     * @param mobileBrand     手机品牌
     * @param mobileVersion   手机型号
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     * @param bugDetails      bug日志
     * @return 数据是否插入成功
     */
    boolean insert(String mobileBrand, String mobileVersion, int platform,
                   String platformVersion, String appVersion, String bugDetails);
}
