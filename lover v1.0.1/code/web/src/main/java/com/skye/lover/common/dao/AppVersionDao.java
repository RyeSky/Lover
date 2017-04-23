package com.skye.lover.common.dao;

import com.skye.lover.common.model.resp.AppVersion;

/**
 * app版本信息Dao
 */
public interface AppVersionDao {
    /**
     * 插入新版本
     *
     * @param appVersion  app版本号
     * @param platform    手机平台【0：android;1：ios】
     * @param forceUpdate 是否强制更新【0：不强制;1：强制】
     * @param downloadUrl app下载地址
     * @param title       更新标题
     * @param content     更新内容
     * @return 数据是否插入成功
     */
    boolean insert(String appVersion, int platform, int forceUpdate, String downloadUrl, String title, String content);

    /**
     * 根据手机平台查询最新版本
     *
     * @param platform 手机平台【0：android;1：ios】
     * @return app版本信息实体
     */
    AppVersion query(int platform);
}
