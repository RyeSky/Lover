package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.FeedbackDao;
import com.skye.lover.model.Feedback;

import java.sql.SQLException;

/**
 * 意见反馈Dao实现
 */
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
        String sql = "insert into " + Feedback.TABLE_NAME + " (" + Feedback.PUBLISHER + "," + Feedback.CONTENT + ","
                + Feedback.PLATFORM + "," + Feedback.PLATFORM_VERSION + "," + Feedback.APP_VERSION + ") values (?,?,?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, publisher);
            pst.setString(2, content);
            pst.setInt(3, platform);
            pst.setString(4, platformVersion);
            pst.setString(5, appVersion);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
