package com.skye.lover.common.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * bug业务层
 */
public interface BugService {
    /**
     * 提交bug日志数据
     *
     * @param br              返回数据基类
     * @param mobileBrand     手机品牌
     * @param mobileVersion   手机型号
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     * @param bugDetails      bug日志
     */
    void submitBug(BaseResponse br, String mobileBrand, String mobileVersion,
                   int platform, String platformVersion, String appVersion, String bugDetails);
}
