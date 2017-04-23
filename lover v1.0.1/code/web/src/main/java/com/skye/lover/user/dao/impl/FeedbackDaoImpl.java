package com.skye.lover.user.dao.impl;


import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.user.dao.FeedbackDao;
import com.skye.lover.user.model.req.FeedbackRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈Dao实现
 */
@Repository
public class FeedbackDaoImpl extends BaseDao implements FeedbackDao {

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
    @Override
    public boolean insert(String publisher, String content, int platform, String platformVersion, String appVersion) {
        String sql = "INSERT INTO feedback (publisher, content, platform, platform_version, app_version) " +
                "VALUES (:publisher, :content, :platform, :platform_version, :app_version)";
        Map<String, Object> params = new HashMap<>();
        params.put(FeedbackRequest.PUBLISHER, publisher);
        params.put(FeedbackRequest.CONTENT, content);
        params.put(FeedbackRequest.PLATFORM, platform);
        params.put(FeedbackRequest.PLATFORM_VERSION, platformVersion);
        params.put(FeedbackRequest.APP_VERSION, appVersion);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
