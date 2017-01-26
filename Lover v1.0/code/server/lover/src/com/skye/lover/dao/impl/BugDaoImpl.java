package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.BugDao;
import com.skye.lover.model.Bug;

import java.sql.SQLException;

/**
 * bugDao实现
 */
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
    public boolean insert(String mobileBrand, String mobileVersion,
                          int platform, String platformVersion, String appVersion, String
                                  bugDetails) {
        String sql = "insert into " + Bug.TABLE_NAME + " (" + Bug.MOBILE_BRAND + "," + Bug.MOBILE_VERSION + ","
                + Bug.PLATFORM + "," + Bug.PLATFORM_VERSION + "," + Bug.APP_VERSION + ","
                + Bug.BUG_DETAILS + ") values (?,?,?,?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, mobileBrand);
            pst.setString(2, mobileVersion);
            pst.setInt(3, platform);
            pst.setString(4, platformVersion);
            pst.setString(5, appVersion);
            pst.setString(6, bugDetails);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
