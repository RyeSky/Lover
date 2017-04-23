package com.skye.lover.message.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.message.dao.MessageDao;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息Dao实现
 */
@Repository
public class MessageDaoImpl extends BaseDao implements MessageDao {
    /**
     * 填充消息实体
     *
     * @param rs 数据库查询结果集
     * @return 消息实体
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
        message.clickAble = rs.getInt(Message.CLICK_ABLE);
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
        String sql = "INSERT INTO message (user_id,message.type,sub_type,title,content,another,pillow_talk_id) " +
                "VALUES (:user_id,:type,:sub_type,:title,:content,:another,:pillow_talk_id)";
        KeyHolder key = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Message.USER_ID, userId);
        params.addValue(Message.TYPE, type);
        params.addValue(Message.SUB_TYPE, subType);
        params.addValue(Message.TITLE, title);
        params.addValue(Message.CONTENT, content);
        params.addValue(Message.ANOTHER, another);
        params.addValue(Message.PILLOW_TALK_ID, pillowTalkId);
        jdbcTemplate.update(sql, params, key);
        return String.valueOf(key.getKey().longValue());
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
        String sql = "INSERT INTO message (user_id,type,sub_type,title,content,another,pillow_talk_id) " +
                "SELECT id,0,0,:title,:content,0,0 FROM `user`";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.TITLE, title);
        params.put(Message.CONTENT, content);
        return jdbcTemplate.update(sql, params) > 0;
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
        String sql = "UPDATE message SET deleted = 1 WHERE id = :id AND  user_id = :user_id";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.ID, messageId);
        params.put(Message.USER_ID, userId);
        return jdbcTemplate.update(sql, params) > 0;
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
                "pillow_talk_id,pillow_talk.type AS pillow_talk_type,imgs,click_able," +
                "message.create_time " +
                "FROM message " +
                "LEFT JOIN `user` AS ano ON ano.id = message.another " +
                "LEFT JOIN pillow_talk ON pillow_talk.id = message.pillow_talk_id " +
                "WHERE message.id = :id AND message.deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.ID, messageId);
        List<Message> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillMessage(rs));
        return list == null || list.isEmpty() ? null : list.get(0);
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
                "pillow_talk_id,pillow_talk.type AS pillow_talk_type,imgs,click_able," +
                "message.create_time " +
                "FROM message " +
                "LEFT JOIN `user` AS ano ON ano.id = message.another " +
                "LEFT JOIN pillow_talk ON pillow_talk.id = message.pillow_talk_id " +
                "WHERE user_id = :user_id AND message.deleted = 0 GROUP BY message.id ORDER BY message.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        List<Message> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillMessage(rs));
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
        String sql = "SELECT COUNT(*) AS count FROM message WHERE user_id = :user_id AND message.deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.USER_ID, userId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 使type指定类型的消息不可点击
     *
     * @param userId  用户id
     * @param subType 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】
     * @return 数据是否更新成功
     */
    public boolean disableClick(String userId, int subType) {
        String sql = "UPDATE message SET click_able = 0 WHERE type = 1 AND sub_type = :sub_type AND user_id = :user_id";
        Map<String, Object> params = new HashMap<>();
        params.put(Message.USER_ID, userId);
        params.put(Message.SUB_TYPE, subType);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
