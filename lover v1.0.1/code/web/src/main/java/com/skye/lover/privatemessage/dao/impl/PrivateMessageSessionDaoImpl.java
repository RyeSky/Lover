package com.skye.lover.privatemessage.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.privatemessage.dao.PrivateMessageSessionDao;
import com.skye.lover.privatemessage.model.resp.PrivateMessageSession;
import com.skye.lover.util.CommonUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 私信会话DAO实现
 */
@Repository
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
        String privateMessageSessionId;
        String sql = "SELECT id FROM private_message_session WHERE one = :one AND another = :another";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessageSession.ONE, one);
        params.put(PrivateMessageSession.ANOTHER, another);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        privateMessageSessionId = list == null || list.isEmpty() ? null : list.get(0).get(PrivateMessageSession.ID).toString();
        if (CommonUtil.isBlank(privateMessageSessionId)) {//还不存在记录
            sql = "INSERT INTO private_message_session (one,another,last_private_message_id) VALUES (:one,:another,:last_private_message_id)";
            KeyHolder key = new GeneratedKeyHolder();
            SqlParameterSource source = new MapSqlParameterSource().addValue(PrivateMessageSession.ONE, one)
                    .addValue(PrivateMessageSession.ANOTHER, another).addValue(PrivateMessageSession.LAST_PRIVATE_MESSAGE_ID, privateMessageId);
            jdbcTemplate.update(sql, source, key);
            return String.valueOf(key.getKey().longValue());
        } else {//已经存在记录
            sql = "UPDATE private_message_session SET last_private_message_id = :last_private_message_id WHERE one = :one AND another = :another";
            params = new HashMap<>();
            params.put(PrivateMessageSession.LAST_PRIVATE_MESSAGE_ID, privateMessageId);
            params.put(PrivateMessageSession.ONE, one);
            params.put(PrivateMessageSession.ANOTHER, another);
            if (jdbcTemplate.update(sql, params) <= 0) privateMessageSessionId = "";
        }
        return privateMessageSessionId;
    }

    /**
     * 会话列表
     *
     * @param userId 用户id
     * @return 会话列表数据集合
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
                "WHERE one = :user_id AND last_private_message_id IS NOT NULL";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessageSession.USER_ID, userId);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> {
            PrivateMessageSession pms = new PrivateMessageSession();
            pms.id = rs.getString(PrivateMessageSession.ID);
            pms.one = userId;
            pms.another = rs.getString(PrivateMessageSession.ANOTHER);
            pms.anotherNickname = rs.getString(PrivateMessageSession.ANOTHER_NICKNAME);
            pms.anotherAvatar = rs.getString(PrivateMessageSession.ANOTHER_AVATAR);
            pms.anotherGender = rs.getInt(PrivateMessageSession.ANOTHER_GENDER);
            pms.lastPrivateMessageId = rs.getString(PrivateMessageSession.LAST_PRIVATE_MESSAGE_ID);
            pms.lastPrivateMessageContent = CommonUtil.getStringFromByteArray(rs.getBytes(PrivateMessageSession.LAST_PRIVATE_MESSAGE_CONTENT));
            pms.lastPrivateMessageCreateTime = CommonUtil.getTimestamp(rs.getString(PrivateMessageSession.LAST_PRIVATE_MESSAGE_CREATE_TIME));
            return pms;
        });
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
        String sql = "UPDATE private_message_session SET last_private_message_id = NULL WHERE one = :one AND another = :another";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessageSession.ONE, one);
        params.put(PrivateMessageSession.ANOTHER, another);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
