package com.skye.lover.common.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * app版本业务层
 */
public interface AppVersionService {
    /**
     * 插入新版本
     *
     * @param br          返回数据基类
     * @param appVersion  app版本号
     * @param platform    手机平台【0：android;1：ios】
     * @param forceUpdate 是否强制更新【0：不强制;1：强制】
     * @param downloadUrl app下载地址
     * @param title       更新标题
     * @param content     更新内容
     */
    void insert(BaseResponse br, String appVersion, int platform, int forceUpdate, String downloadUrl, String title, String content);

    /**
     * 根据手机平台查询最新版本
     *
     * @param br       返回数据基类
     * @param platform 手机平台【0：android;1：ios】
     */
    void query(BaseResponse br, int platform);
}
