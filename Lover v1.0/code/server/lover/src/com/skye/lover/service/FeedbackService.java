package com.skye.lover.service;

import com.skye.lover.dao.impl.FeedbackDaoImpl;
import com.skye.lover.dao.interf.FeedbackDao;
import com.skye.lover.model.BaseResponse;

/**
 * 意见反馈业务层
 */
public class FeedbackService {
    private FeedbackDao fd = new FeedbackDaoImpl();

    /**
     * 插入意见反馈数据
     *
     * @param br              返回数据基类
     * @param publisher       意见反馈发布者id
     * @param content         反馈内容
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     */
    public void submitFeedback(BaseResponse br, String publisher, String content, int platform,
                               String platformVersion, String appVersion) {
        if (!fd.insert(publisher, content, platform, platformVersion, appVersion)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
