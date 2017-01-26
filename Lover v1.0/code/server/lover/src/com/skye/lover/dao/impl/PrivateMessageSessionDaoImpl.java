package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.PrivateMessageSessionDao;
import com.skye.lover.model.PrivateMessageSession;
import com.skye.lover.util.CommonUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 私信会话DAO实现
 */
public class PrivateMessageSessionDaoImpl extends BaseDao implements PrivateMessageSessionDao {
    /**
     * 插入私信会话
     *
     * @param one              聊天中的一方id
     * @param another          聊天中的另一方id
     * @param privateMessageId 私信记录id
     * @return 私信会话记录id
     */
    @Override
    public String insert(String one, String another, String privateMessageId) {
        String privateMessageSessionId = null;
        try {
            String sql = "SELECT id FROM private_message_session WHERE one = ? AND another = ?";
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            rs = pst.executeQuery();
            if (rs.next()) {
                privateMessageSessionId = rs.getString(PrivateMessageSession.ID);
            }
            close();
            conn = BaseDao.getConnection();
            if (CommonUtil.isBlank(privateMessageSessionId)) {//还不存在记录
                sql = "INSERT INTO private_message_session (one,another,last_private_message_id) VALUES (?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, one);
                pst.setString(2, another);
                pst.setString(3, privateMessageId);
                pst.execute();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    privateMessageSessionId = rs.getString(1);
                }
            } else {//已经存在记录
                sql = "UPDATE private_message_session SET last_private_message_id = ? WHERE one = ? AND another = ?";
                int update = 0;// sql语句执行后影响的行数
                pst = conn.prepareStatement(sql);
                pst.setString(1, privateMessageId);
                pst.setString(2, one);
                pst.setString(3, another);
                update = pst.executeUpdate();
                if (update <= 0) privateMessageSessionId = "";
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privateMessageSessionId;
    }


    /**
     * 会话列表
     *
     * @param userId 用户id
     */
    @Override
    public List<PrivateMessageSession> query(String userId) {
        String sql = "SELECT private_message_session.id AS id," +
                "another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "last_private_message_id," +
                "private_message.content AS last_private_message_content,private_message.create_time AS last_private_message_create_time " +
                "FROM private_message_session " +
                "LEFT JOIN `user` AS ano ON ano.id = private_message_session.another " +
                "LEFT JOIN private_message ON private_message.id = private_message_session.last_private_message_id " +
                "WHERE one = ? AND last_private_message_id IS NOT NULL";
        List<PrivateMessageSession> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            PrivateMessageSession pms;
            byte content[];
            while (rs.next()) {
                pms = new PrivateMessageSession();
                pms.id = rs.getString(PrivateMessageSession.ID);
                pms.one = userId;
                pms.another = rs.getString(PrivateMessageSession.ANOTHER);
                pms.anotherNickname = rs.getString(PrivateMessageSession.ANOTHER_NICKNAME);
                pms.anotherAvatar = rs.getString(PrivateMessageSession.ANOTHER_AVATAR);
                pms.anotherGender = rs.getInt(PrivateMessageSession.ANOTHER_GENDER);
                pms.lastPrivateMessageId = rs.getString(PrivateMessageSession.LAST_PRIVATE_MESSAGE_ID);
                try {
                    content = rs.getBytes(PrivateMessageSession.LAST_PRIVATE_MESSAGE_CONTENT);
                    pms.lastPrivateMessageContent = new String(content, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pms.lastPrivateMessageCreateTime = CommonUtil.getTimestamp(rs.getString(PrivateMessageSession.LAST_PRIVATE_MESSAGE_CREATE_TIME));
                list.add(pms);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }


    /**
     * 删除私信会话
     *
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String one, String another) {
        String sql = "UPDATE private_message_session SET last_private_message_id = NULL WHERE one = ? AND another = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
