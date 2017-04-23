package com.skye.lover.user.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 意见反馈业务层
 */
public interface FeedbackService {
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
    void submitFeedback(BaseResponse br, String publisher, String content, int platform,
                        String platformVersion, String appVersion);
}
