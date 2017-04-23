package com.skye.lover.pillowtalk.followpillowtalk.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.followpillowtalk.dao.FollowPillowTalkDao;
import com.skye.lover.pillowtalk.followpillowtalk.model.resp.FollowPillowTalk;
import com.skye.lover.user.model.resp.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跟随悄悄话DAO实现
 */
@Repository
public class FollowPillowTalkDaoImpl extends BaseDao implements FollowPillowTalkDao {
    /**
     * 发表跟随悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param publisher    跟随悄悄话发布者
     * @param content      跟随悄悄话内容
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String pillowTalkId, String publisher, String content) {
        String sql = "INSERT INTO follow_pillow_talk (pillow_talk_id, publisher, content) VALUES (:pillow_talk_id, :publisher, :content)";
        Map<String, Object> params = new HashMap<>();
        params.put(FollowPillowTalk.PILLOW_TALK_ID, pillowTalkId);
        params.put(FollowPillowTalk.PUBLISHER, publisher);
        params.put(FollowPillowTalk.CONTENT, content);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 删除跟随悄悄话
     *
     * @param follwoPillowTalkId 跟随悄悄话id
     * @param publisher          跟随悄悄话发布者
     * @return 数据是否删除成功
     */
    @Override
    public boolean delete(String follwoPillowTalkId, String publisher) {
        String sql = "UPDATE follow_pillow_talk SET deleted = 1 WHERE id = :id AND publisher = :publisher";
        Map<String, Object> params = new HashMap<>();
        params.put(FollowPillowTalk.ID, follwoPillowTalkId);
        params.put(FollowPillowTalk.PUBLISHER, publisher);
        return jdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public List<FollowPillowTalk> follows(String pillowTalkId, int page) {
        String sql = "SELECT follow_pillow_talk.id,publisher,pub.nickname,pub.avatar,pub.gender,content,follow_pillow_talk.create_time " +
                "FROM follow_pillow_talk " +
                "LEFT JOIN `user` AS pub ON follow_pillow_talk.publisher = pub.id " +
                "WHERE pillow_talk_id = :pillow_talk_id AND deleted = 0 ORDER BY create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(FollowPillowTalk.PILLOW_TALK_ID, pillowTalkId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> {
            FollowPillowTalk fpt = new FollowPillowTalk();
            fpt.id = rs.getString(FollowPillowTalk.ID);
            fpt.pillowTalkId = pillowTalkId;
            fpt.publisher = rs.getString(FollowPillowTalk.PUBLISHER);
            fpt.nickname = rs.getString(User.NICKNAME);
            fpt.avatar = rs.getString(User.AVATAR);
            fpt.gender = rs.getInt(User.GENDER);
            fpt.content = CommonUtil.getStringFromByteArray(rs.getBytes(FollowPillowTalk.CONTENT));
            fpt.createTime = CommonUtil.getTimestamp(rs.getString(FollowPillowTalk.CREATE_TIME));
            return fpt;
        });
    }

    /**
     * 跟随悄悄话列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 跟随悄悄话列表记录总数
     */
    @Override
    public int countOfFollows(String pillowTalkId) {
        String sql = "SELECT COUNT(id) AS 'count' FROM follow_pillow_talk WHERE pillow_talk_id = :pillow_talk_id AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(FollowPillowTalk.PILLOW_TALK_ID, pillowTalkId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }
}
