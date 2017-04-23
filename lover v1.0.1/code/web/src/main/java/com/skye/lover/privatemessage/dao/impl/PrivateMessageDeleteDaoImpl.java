package com.skye.lover.privatemessage.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.privatemessage.dao.PrivateMessageDeleteDao;
import com.skye.lover.privatemessage.model.resp.PrivateMessage;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除私信DAO实现
 */
@Repository
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
        String sql = "INSERT INTO private_message_delete (private_message_id,chater) VALUES (:private_message_id,:chater)";
        Map<String, Object> params = new HashMap<>();
        params.put("private_message_id", privateMessageId);
        params.put("chater", chater);
        return jdbcTemplate.update(sql, params) > 0;
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
        String sql = "INSERT INTO private_message_delete (chater,private_message_id) " +
                "SELECT :user_id,id FROM private_message WHERE (sender = :one AND receiver = :another || sender = :another AND receiver = :one) " +
                "AND id NOT IN (SELECT private_message_id FROM private_message_delete WHERE chater = :user_id) ORDER BY private_message.create_time DESC";
        Map<String, Object> params = new HashMap<>();
        params.put(PrivateMessage.USER_ID, one);
        params.put(PrivateMessage.ONE, one);
        params.put(PrivateMessage.ANOTHER, another);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
