package com.skye.lover.common.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.common.dao.AppVersionDao;
import com.skye.lover.common.model.resp.AppVersion;
import com.skye.lover.util.CommonUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app版本信息Dao实现
 */
@Repository
public class AppVersionDaoImpl extends BaseDao implements AppVersionDao {
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
    @Override
    public boolean insert(String appVersion, int platform, int forceUpdate, String downloadUrl, String title, String content) {
        String sql = "INSERT INTO app_version (app_version, platform, force_update, download_url, title, content) " +
                "VALUES (:app_version, :platform, :force_update, :download_url, :title, :content)";
        Map<String, Object> params = new HashMap<>();
        params.put(AppVersion.APP_VERSION, appVersion);
        params.put(AppVersion.PLATFORM, platform);
        params.put(AppVersion.FORCE_UPDATE, forceUpdate);
        params.put(AppVersion.DOWNLOAD_URL, downloadUrl);
        params.put(AppVersion.TITLE, title);
        params.put(AppVersion.CONTENT, content);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 根据手机平台查询最新版本
     *
     * @param platform 手机平台【0：android;1：ios】
     * @return app版本信息实体
     */
    @Override
    public AppVersion query(int platform) {
        String sql = "SELECT * FROM app_version WHERE platform = :platform ORDER BY create_time DESC LIMIT 0,1";
        Map<String, Object> params = new HashMap<>();
        params.put(AppVersion.PLATFORM, platform);
        List<AppVersion> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> {
            AppVersion version = new AppVersion();
            version.id = rs.getString(AppVersion.ID);
            version.appVersion = rs.getString(AppVersion.APP_VERSION);
            version.platform = platform;
            version.forceUpdate = rs.getInt(AppVersion.FORCE_UPDATE);
            version.downloadUrl = rs.getString(AppVersion.DOWNLOAD_URL);
            version.title = rs.getString(AppVersion.TITLE);
            version.content = rs.getString(AppVersion.CONTENT);
            version.createTime = CommonUtil.getTimestamp(rs.getString(AppVersion.CREATE_TIME));
            return version;
        });
        if (list != null && !list.isEmpty()) return list.get(0);
        else return new AppVersion(platform);
    }
}
