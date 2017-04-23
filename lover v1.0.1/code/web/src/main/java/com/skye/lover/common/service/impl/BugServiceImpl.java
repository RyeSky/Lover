package com.skye.lover.common.service.impl;

import com.skye.lover.common.dao.BugDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * bug业务层实现
 */
@Service
public class BugServiceImpl implements BugService {
    private BugDao bd;

    @Autowired
    public BugServiceImpl(BugDao bd) {
        this.bd = bd;
    }

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
    @Override
    public void submitBug(BaseResponse br, String mobileBrand, String mobileVersion,
                          int platform, String platformVersion, String appVersion, String bugDetails) {
        if (!bd.insert(mobileBrand, mobileVersion, platform, platformVersion, appVersion, bugDetails)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_SUBMIT_FAIL;
        }
    }
}
