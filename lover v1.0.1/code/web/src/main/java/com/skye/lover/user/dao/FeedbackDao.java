package com.skye.lover.user.dao;

/**
 * 意见反馈Dao
 */
public interface FeedbackDao {
    /**
     * 插入意见反馈数据
     *
     * @param publisher       意见反馈发布者id
     * @param content         反馈内容
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     * @return 数据是否插入成功
     */
    boolean insert(String publisher, String content, int platform, String platformVersion, String appVersion);
}
