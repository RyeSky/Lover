package com.skye.lover.common.service.impl;

import com.skye.lover.common.dao.AppVersionDao;
import com.skye.lover.common.model.resp.AppVersion;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * app版本业务层实现
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {
    private AppVersionDao avd;

    @Autowired
    public AppVersionServiceImpl(AppVersionDao avd) {
        this.avd = avd;
    }

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
    public void insert(BaseResponse br, String appVersion, int platform, int forceUpdate, String downloadUrl, String title, String content) {
        if (!avd.insert(appVersion, platform, forceUpdate, downloadUrl, title, content)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_SUBMIT_FAIL;
        }
    }

    /**
     * 根据手机平台查询最新版本
     *
     * @param br       返回数据基类
     * @param platform 手机平台【0：android;1：ios】
     */
    public void query(BaseResponse br, int platform) {
        AppVersion version = avd.query(platform);
        if (version != null) br.result = version;
        else {// 查询数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
