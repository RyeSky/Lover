package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.ReportPillowTalkDao;
import com.skye.lover.model.ReportPillowTalk;

import java.sql.SQLException;

/**
 * 举报悄悄话Dao实现
 */
public class ReportPillowTalkDaoImpl extends BaseDao implements ReportPillowTalkDao {
    /**
     * 插入举报数据
     *
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String pillowTalkId, String reporter, String content) {
        String sql = "insert into " + ReportPillowTalk.TABLE_NAME + " (" + ReportPillowTalk.PILLOW_TALK_ID + ","
                + ReportPillowTalk.REPORTER + "," + ReportPillowTalk.CONTENT + ") values (?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, reporter);
            pst.setString(3, content);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
