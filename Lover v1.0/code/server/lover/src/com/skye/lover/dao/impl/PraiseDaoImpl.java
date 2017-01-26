package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.PraiseDao;

import java.sql.SQLException;

/**
 * 赞DAO实现
 */
public class PraiseDaoImpl extends BaseDao implements PraiseDao {
    /**
     * 赞悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     * @return 赞记录id
     */
    public String insert(String pillowTalkId, String praiser) {
        String sql = "INSERT INTO praise_pillow_talk (pillow_talk_id,praiser) VALUES (?,?)";
        String collectId = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, praiser);
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
     * @param praiser      赞者
     * @return 数据是否删除成功
     */
    public boolean delete(String pillowTalkId, String praiser) {
        String sql = "DELETE FROM praise_pillow_talk WHERE pillow_talk_id = ? AND praiser = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, praiser);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
