package com.skye.lover.service;

import com.skye.lover.dao.impl.BugDaoImpl;
import com.skye.lover.dao.interf.BugDao;
import com.skye.lover.model.BaseResponse;

/**
 * bug业务层
 */
public class BugService {
    private BugDao bd = new BugDaoImpl();

    /**
     * 提交bug日志数据
     *
     * @param br              返回数据基类     *
     * @param mobileBrand     手机品牌
     * @param mobileVersion   手机型号
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     * @param bugDetails      bug日志
     */
    public void submitBug(BaseResponse br, String mobileBrand, String mobileVersion,
                          int platform, String platformVersion, String appVersion, String bugDetails) {
        if (!bd.insert(mobileBrand, mobileVersion, platform, platformVersion, appVersion, bugDetails)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
