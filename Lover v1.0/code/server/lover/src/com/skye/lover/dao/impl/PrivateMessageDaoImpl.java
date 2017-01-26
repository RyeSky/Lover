package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.PrivateMessageDao;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 私信Dao实现
 */
public class PrivateMessageDaoImpl extends BaseDao implements PrivateMessageDao {

    /**
     * 插入私信
     *
     * @param sender   私信发送者id
     * @param receiver 私信接受者id
     * @param content  私信内容
     * @return 私信记录id
     */
    @Override
    public String insert(String sender, String receiver, String content) {
        String sql = "INSERT INTO private_message (sender,receiver,content) VALUES (?,?,?)";
        String privateMessageId = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, sender);
            pst.setString(2, receiver);
            pst.setString(3, content);
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                privateMessageId = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privateMessageId;
    }


    /**
     * 查询聊天记录
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @param offset  记录偏移量
     * @return 聊天记录数据集合
     */
    @Override
    public List<PrivateMessage> query(String one, String another, int offset) {
        String sql = "SELECT private_message.id AS id," +
                "private_message.sender,sen.nickname AS sender_nickname,sen.avatar AS sender_avatar,sen.gender AS sender_gender," +
                "private_message.receiver,rec.nickname AS receiver_nickname,rec.avatar AS receiver_avatar,rec.gender AS receiver_gender," +
                "content,private_message.create_time " +
                "FROM private_message " +
                "LEFT JOIN `user` AS sen on private_message.sender = sen.id " +
                "LEFT JOIN `user` AS rec on private_message.receiver = rec.id " +
                "WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?) " +
                "ORDER BY private_message.create_time DESC LIMIT ?,?";
        List<PrivateMessage> temp = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
            pst.setString(5, one);
            pst.setInt(6, offset);
            pst.setInt(7, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            PrivateMessage pm;
            byte content[];
            while (rs.next()) {
                pm = new PrivateMessage();
                pm.id = rs.getString(PrivateMessage.ID);
                pm.sender = rs.getString(PrivateMessage.SENDER);
                pm.senderNickname = rs.getString(PrivateMessage.SENDER_NICKNAME);
                pm.senderAvatar = rs.getString(PrivateMessage.SENDER_AVATAR);
                pm.senderGender = rs.getInt(PrivateMessage.SENDER_GENDER);
                pm.receiver = rs.getString(PrivateMessage.RECEIVER);
                pm.receiverNickname = rs.getString(PrivateMessage.RECEIVER_NICKNAME);
                pm.receiverAvatar = rs.getString(PrivateMessage.RECEIVER_AVATAR);
                pm.receiverGender = rs.getInt(PrivateMessage.RECEIVER_GENDER);
                try {
                    content = rs.getBytes(PrivateMessage.CONTENT);
                    pm.content = new String(content, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pm.createTime = CommonUtil.getTimestamp(rs.getString(PrivateMessage.CREATE_TIME));
                temp.add(pm);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            temp = null;
        }
        if (temp == null || temp.isEmpty()) return temp;
        List<PrivateMessage> list = new ArrayList<>();
        for (int i = temp.size() - 1; i >= 0; i--) list.add(temp.get(i));
        return list;
    }


    /**
     * 根据私信记录id查询
     *
     * @param privateMessageId 私信记录id
     * @return 私信记录id对应的实体
     */
    @Override
    public PrivateMessage query(String privateMessageId) {
        String sql = "SELECT private_message.id AS id," +
                "private_message.sender,sen.nickname AS sender_nickname,sen.avatar AS sender_avatar,sen.gender AS sender_gender," +
                "private_message.receiver,rec.nickname AS receiver_nickname,rec.avatar AS receiver_avatar,rec.gender AS receiver_gender," +
                "content,private_message.create_time " +
                "FROM private_message " +
                "LEFT JOIN `user` AS sen on private_message.sender = sen.id " +
                "LEFT JOIN `user` AS rec on private_message.receiver = rec.id " +
                "WHERE private_message.id = ?";
        PrivateMessage pm = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, privateMessageId);
            rs = pst.executeQuery();
            while (rs.next()) {
                pm = new PrivateMessage();
                pm.id = rs.getString(PrivateMessage.ID);
                pm.sender = rs.getString(PrivateMessage.SENDER);
                pm.senderNickname = rs.getString(PrivateMessage.SENDER_NICKNAME);
                pm.senderAvatar = rs.getString(PrivateMessage.SENDER_AVATAR);
                pm.senderGender = rs.getInt(PrivateMessage.SENDER_GENDER);
                pm.receiver = rs.getString(PrivateMessage.RECEIVER);
                pm.receiverNickname = rs.getString(PrivateMessage.RECEIVER_NICKNAME);
                pm.receiverAvatar = rs.getString(PrivateMessage.RECEIVER_AVATAR);
                pm.receiverGender = rs.getInt(PrivateMessage.RECEIVER_GENDER);
                try {
                    byte content[] = rs.getBytes(PrivateMessage.CONTENT);
                    pm.content = new String(content, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pm.createTime = CommonUtil.getTimestamp(rs.getString(PrivateMessage.CREATE_TIME));

            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            pm = null;
        }
        return pm;
    }

    /**
     * 查询私信总记录数
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 私信总记录数
     */
    @Override
    public int countOfPrivateMessage(String one, String another) {
        String sql = "SELECT COUNT(*) AS count FROM private_message WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?)";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
            pst.setString(5, one);
            rs = pst.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 查询指定私信记录id之前的私信总记录数
     *
     * @param one              聊天中的一方
     * @param another          聊天中的另一方
     * @param privateMessageId 指定的私信记录id
     * @return 指定私信记录id之前的私信总记录数
     */
    @Override
    public int countOfBefore(String one, String another, String privateMessageId) {
        String sql = "SELECT COUNT(*) AS count FROM private_message " +
                "WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?) " +
                "ANd private_message.id >= ?";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
            pst.setString(5, one);
            pst.setString(6, privateMessageId);
            rs = pst.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 查询第一条私信记录的id
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 第一条私信记录的id
     */
    @Override
    public String queryFirstPrivateMessageId(String one, String another) {
        String sql = "SELECT id FROM private_message WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?) ORDER BY private_message.create_time ASC LIMIT 0,1";
        String firstPrivateMessageId = "";
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
            pst.setString(5, one);
            rs = pst.executeQuery();
            if (rs.next()) {
                firstPrivateMessageId = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstPrivateMessageId;
    }

    /**
     * 查询最后一条私信记录的id
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 最后一条私信记录的id
     */
    @Override
    public String queryLastPrivateMessageId(String one, String another) {
        String sql = "SELECT id FROM private_message WHERE (sender = ? AND receiver = ? || sender = ? AND receiver = ?) AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = ?) ORDER BY private_message.create_time DESC LIMIT 0,1";
        String lastPrivateMessageId = "";
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
            pst.setString(5, one);
            rs = pst.executeQuery();
            if (rs.next()) {
                lastPrivateMessageId = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastPrivateMessageId;
    }
}
