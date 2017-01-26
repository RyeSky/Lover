package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.PrivateMessageDeleteDao;

import java.sql.SQLException;

/**
 * 删除私信DAO实现
 */
public class PrivateMessageDeleteDaoImpl extends BaseDao implements PrivateMessageDeleteDao {
    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param privateMessageId 私信记录id
     * @param chater           聊天者id
     * @return 私信记录id
     */
    @Override
    public boolean delete(String privateMessageId, String chater) {
        String sql = "INSERT INTO private_message_delete (private_message_id,chater) VALUES (?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, privateMessageId);
            pst.setString(2, chater);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }


    /**
     * 根据聊天的两个用户删除整个会话记录
     *
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteByChater(String one, String another) {
        String sql = "INSERT INTO private_message_delete (chater,private_message_id) SELECT ?,id FROM private_message WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) AND id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?) ORDER BY private_message.create_time DESC";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, one);
            pst.setString(3, another);
            pst.setString(4, another);
            pst.setString(5, one);
            pst.setString(6, one);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
