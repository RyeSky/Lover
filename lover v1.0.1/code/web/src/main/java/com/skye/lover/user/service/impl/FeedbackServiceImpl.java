package com.skye.lover.user.service.impl;

import com.skye.lover.user.dao.FeedbackDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.user.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 意见反馈业务层实现
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private FeedbackDao fd;

    @Autowired
    public FeedbackServiceImpl(FeedbackDao fd) {
        this.fd = fd;
    }

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
    @Override
    public void submitFeedback(BaseResponse br, String publisher, String content, int platform,
                               String platformVersion, String appVersion) {
        if (!fd.insert(publisher, content, platform, platformVersion, appVersion)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_SUBMIT_FAIL;
        }
    }
}
