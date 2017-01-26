package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.CollectDao;

import java.sql.SQLException;

/**
 * 收藏DAO实现
 */
public class CollectDaoImpl extends BaseDao implements CollectDao {
    /**
     * 收藏悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 收藏记录id
     */
    @Override
    public String insert(String pillowTalkId, String collecter) {
        String sql = "INSERT INTO collect_pillow_talk (pillow_talk_id,collecter) VALUES (?,?)";
        String collectId = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, collecter);
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                collectId = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collectId;
    }

    /**
     * 取消收藏
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 数据是否插入成功
     */
    @Override
    public boolean delete(String pillowTalkId, String collecter) {
        String sql = "DELETE FROM collect_pillow_talk WHERE pillow_talk_id = ? AND collecter = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, collecter);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
