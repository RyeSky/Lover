package com.skye.lover.privatemessage.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.privatemessage.dao.PrivateMessageDao;
import com.skye.lover.privatemessage.model.resp.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 私信Dao实现
 */
@Repository
public class PrivateMessageDaoImpl extends BaseDao implements PrivateMessageDao {

    /**
     * 填充私信实体类
     *
     * @param rs 数据库查询结果集
     * @return 私信实体
     */
    private PrivateMessage fillPrivateMessage(ResultSet rs) throws SQLException {
        PrivateMessage pm = new PrivateMessage();
        pm.id = rs.getString(PrivateMessage.ID);
        pm.sender = rs.getString(PrivateMessage.SENDER);
        pm.senderNickname = rs.getString(PrivateMessage.SENDER_NICKNAME);
        pm.senderAvatar = rs.getString(PrivateMessage.SENDER_AVATAR);
        pm.senderGender = rs.getInt(PrivateMessage.SENDER_GENDER);
        pm.receiver = rs.getString(PrivateMessage.RECEIVER);
        pm.receiverNickname = rs.getString(PrivateMessage.RECEIVER_NICKNAME);
        pm.receiverAvatar = rs.getString(PrivateMessage.RECEIVER_AVATAR);
        pm.receiverGender = rs.getInt(PrivateMessage.RECEIVER_GENDER);
        pm.content = CommonUtil.getStringFromByteArray(rs.getBytes(PrivateMessage.CONTENT));
        pm.createTime = CommonUtil.getTimestamp(rs.getString(PrivateMessage.CREATE_TIME));
        return pm;
    }

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
        String sql = "INSERT INTO private_message (sender,receiver,content) VALUES (:sender,:receiver,:content)";
        KeyHolder key = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource().addValue(PrivateMessage.SENDER, sender)
                .addValue(PrivateMessage.RECEIVER, receiver).addValue(PrivateMessage.CONTENT, content);
        jdbcTemplate.update(sql, params, key);
        return String.valueOf(key.getKey().longValue());
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
                "LEFT JOIN `user` AS sen ON private_message.sender = sen.id " +
                "LEFT JOIN `user` AS rec ON private_message.receiver = rec.id " +
                "WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id) " +
                "ORDER BY private_message.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        params.put(PrivateMessage.USER_ID, one);
        params.put("os", offset);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPrivateMessage(rs));
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
                "LEFT JOIN `user` AS sen ON private_message.sender = sen.id " +
                "LEFT JOIN `user` AS rec ON private_message.receiver = rec.id " +
                "WHERE private_message.id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ID, privateMessageId);
        List<PrivateMessage> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPrivateMessage(rs));
        return list == null || list.isEmpty() ? null : list.get(0);
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
        String sql = "SELECT COUNT(*) AS count " +
                "FROM private_message " +
                "WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id)";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        params.put(PrivateMessage.USER_ID, one);
        return jdbcTemplate.queryForObject(sql, params, int.class);
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
                "WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id) " +
                "AND private_message.id >= :id";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        params.put(PrivateMessage.USER_ID, one);
        params.put(PrivateMessage.ID, privateMessageId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
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
        String sql = "SELECT id FROM private_message " +
                "WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id) " +
                "ORDER BY private_message.create_time ASC LIMIT 0,1";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        params.put(PrivateMessage.USER_ID, one);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        return list == null || list.isEmpty() ? null : list.get(0).get(PrivateMessage.ID).toString();
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
        String sql = "SELECT id FROM private_message " +
                "WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND private_message.id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id) " +
                "ORDER BY private_message.create_time DESC LIMIT 0,1";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        params.put(PrivateMessage.USER_ID, one);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        return list == null || list.isEmpty() ? null : list.get(0).get(PrivateMessage.ID).toString();
    }
}
