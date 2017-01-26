package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.MessageDao;
import com.skye.lover.model.Message;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息Dao实现
 */
public class MessageDaoImpl extends BaseDao implements MessageDao {
    /**
     * 填充消息实体
     */
    private Message fillMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.id = rs.getString(Message.ID);
        message.userId = rs.getString(Message.USER_ID);
        message.type = rs.getInt(Message.TYPE);
        message.subType = rs.getInt(Message.SUB_TYPE);
        message.title = rs.getString(Message.TITLE);
        message.content = rs.getString(Message.CONTENT);
        message.anotherId = rs.getString(Message.ANOTHER);
        message.anotherNickname = rs.getString(Message.ANOTHER_NICKNAME);
        message.anotherAvatar = rs.getString(Message.ANOTHER_AVATAR);
        message.anotherGender = rs.getInt(Message.ANOTHER_GENDER);
        message.pillowTalkId = rs.getString(Message.PILLOW_TALK_ID);
        message.pillowTalkType = rs.getInt(Message.PILLOW_TALK_TYPE);
        message.imgs = rs.getString(PillowTalk.IMGS);
        message.createTime = CommonUtil.getTimestamp(rs.getString(Message.CREATE_TIME));
        return message;
    }

    /**
     * 插入消息
     *
     * @param userId       用户id
     * @param type         消息类型
     * @param subType      消息子类型
     * @param title        消息标题
     * @param content      消息内容
     * @param another      示爱的来源
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息记录id
     */
    @Override
    public String insert(String userId, int type, int subType, String title, String content, String another, String pillowTalkId) {
        String sql = "INSERT INTO message (user_id,message.type,sub_type,title,content,another,pillow_talk_id) VALUES (?,?,?,?,?,?,?)";
        String messageId = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setInt(2, type);
            pst.setInt(3, subType);
            pst.setString(4, title);
            pst.setString(5, content);
            pst.setString(6, another);
            pst.setString(7, pillowTalkId);
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageId;
    }

    /**
     * 插入系统消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 数据是否插入成功
     */
    @Override
    public boolean insertSystemMessage(String title, String content) {
        String sql = "INSERT INTO message (user_id,type,sub_type,title,content,another,pillow_talk_id) " + "SELECT id,0,0,?,?,0,0 FROM `user`";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, content);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 删除指定消息
     *
     * @param messageId 消息记录id
     * @param userId    消息所属用户
     * @return 数据是否删除成功
     */
    @Override
    public boolean delete(String messageId, String userId) {
        String sql = "update " + Message.TABLE_NAME + " set " + Message.DELETED + " = 1 where " + Message.ID + " = ? and " + Message.USER_ID + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, messageId);
            pst.setString(2, userId);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 查询指定消息
     *
     * @param messageId 消息记录id
     * @return 消息实体
     */
    @Override
    public Message query(String messageId) {
        String sql = "SELECT message.id,user_id,message.type,sub_type,title,message.content," +
                "message.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk_id,pillow_talk.type AS pillow_talk_type,imgs," +
                "message.create_time " +
                "FROM message " +
                "LEFT JOIN `user` AS ano ON ano.id = message.another " +
                "LEFT JOIN pillow_talk ON pillow_talk.id = message.pillow_talk_id " +
                "WHERE message.id = ? AND message.deleted = 0";
        Message message = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, messageId);
            rs = pst.executeQuery();
            if (rs.next()) {
                message = fillMessage(rs);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }

    /**
     * 消息列表
     *
     * @param userId 用户id
     * @param page   请求数据的页数
     * @return 消息数据集合
     */
    @Override
    public List<Message> messages(String userId, int page) {
        String sql = "SELECT message.id,user_id,message.type,sub_type,title,message.content," +
                "message.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk_id,pillow_talk.type AS pillow_talk_type,imgs," +
                "message.create_time " +
                "FROM message " +
                "LEFT JOIN `user` AS ano ON ano.id = message.another " +
                "LEFT JOIN pillow_talk ON pillow_talk.id = message.pillow_talk_id " +
                "WHERE user_id = ? AND message.deleted = 0 GROUP BY message.id ORDER BY message.create_time DESC LIMIT ?,?";
        List<Message> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setInt(2, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(3, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillMessage(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 消息列表总记录数
     *
     * @param userId 用户id
     * @return 消息列表总记录数
     */
    @Override
    public int countOfMessages(String userId) {
        String sql = "SELECT COUNT(*) AS count FROM message WHERE user_id = ? AND message.deleted = 0";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
