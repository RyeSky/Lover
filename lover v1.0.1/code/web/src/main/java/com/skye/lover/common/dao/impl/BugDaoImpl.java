package com.skye.lover.common.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.common.dao.BugDao;
import com.skye.lover.common.model.req.SubmitBugRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * bugDao实现
 */
@Repository
public class BugDaoImpl extends BaseDao implements BugDao {
    /**
     * 插入bug日志数据
     *
     * @param mobileBrand     手机品牌
     * @param mobileVersion   手机型号
     * @param platform        手机平台【0：android;1:ios】
     * @param platformVersion 平台系统版本号
     * @param appVersion      app版本号
     * @param bugDetails      bug日志
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String mobileBrand, String mobileVersion, int platform, String platformVersion, String appVersion, String bugDetails) {
        String sql = "INSERT INTO bugs (mobile_brand,mobile_version,platform,platform_version,app_version,bug_details) " +
                "VALUES (:mobile_brand,:mobile_version,:platform,:platform_version,:app_version,:bug_details)";
        Map<String, Object> params = new HashMap<>();
        params.put(SubmitBugRequest.MOBILE_BRAND, mobileBrand);
        params.put(SubmitBugRequest.MOBILE_VERSION, mobileVersion);
        params.put(SubmitBugRequest.PLATFORM, platform);
        params.put(SubmitBugRequest.PLATFORM_VERSION, platformVersion);
        params.put(SubmitBugRequest.APP_VERSION, appVersion);
        params.put(SubmitBugRequest.BUG_DETAILS, bugDetails);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
