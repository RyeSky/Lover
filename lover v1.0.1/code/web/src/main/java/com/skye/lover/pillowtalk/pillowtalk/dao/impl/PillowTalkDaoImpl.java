package com.skye.lover.pillowtalk.pillowtalk.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.pillowtalk.dao.PillowTalkDao;
import com.skye.lover.loverrelatiionship.model.LoverRelationship;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalkProperties;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 悄悄话DAO实现
 */
@Repository
public class PillowTalkDaoImpl extends BaseDao implements PillowTalkDao {

    /**
     * 填充悄悄话实体
     *
     * @param rs 数据库查询结果集
     * @return 悄悄话实体
     */
    private PillowTalk fillPillowTalk(ResultSet rs) throws SQLException {
        PillowTalk pt = new PillowTalk();
        pt.id = rs.getString(PillowTalk.ID);
        pt.publisherId = rs.getString(PillowTalk.PUBLISHER);
        pt.publisherNickname = rs
                .getString(PillowTalk.PUBLISHER_NICKNAME);
        pt.publisherAvatar = rs.getString(PillowTalk.PUBLISHER_AVATAR);
        pt.publisherGender = rs.getInt(PillowTalk.PUBLISHER_GENDER);
        pt.anotherId = rs.getString(PillowTalk.ANOTHER);
        pt.anotherNickname = rs.getString(PillowTalk.ANOTHER_NICKNAME);
        pt.anotherAvatar = rs.getString(PillowTalk.ANOTHER_AVATAR);
        pt.anotherGender = rs.getInt(PillowTalk.ANOTHER_GENDER);
        pt.content = CommonUtil.getStringFromByteArray(rs.getBytes(PillowTalk.CONTENT));
        pt.imgs = rs.getString(PillowTalk.IMGS);
        pt.type = rs.getInt(PillowTalk.TYPE);
        pt.publisherOpen = rs.getInt(PillowTalk.PUBLISHER_OPEN);
        pt.anotherOpen = rs.getInt(PillowTalk.ANOTHER_OPEN);
        pt.praiseId = rs.getString(PillowTalk.PRAISE_ID);
        pt.collectId = rs.getString(PillowTalk.COLLECT_ID);
        pt.createTime = CommonUtil.getTimestamp(rs.getString(PillowTalk.CREATE_TIME));
        pt.praiseCount = rs.getInt(PillowTalk.PRAISE_COUNT);
        pt.commentCount = rs.getInt(PillowTalk.COMMENT_COUNT);
        return pt;
    }

    /**
     * 插入悄悄话
     *
     * @param publisher 悄悄话发表者
     * @param type      类型【0:悄悄话；1:广播】
     * @param another   相爱的另一方
     * @param content   悄悄内容
     * @param imgs      多张图片路径，用英文逗号分隔
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String publisher, int type, String another, String content, String imgs) {
        String sql = "INSERT INTO pillow_talk (publisher, type, another, content, imgs) VALUES (:publisher, :type, :another, :content, :imgs)";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.PUBLISHER, publisher);
        params.put(PillowTalk.TYPE, type);
        params.put(PillowTalk.ANOTHER, another);
        params.put(PillowTalk.CONTENT, content);
        params.put(PillowTalk.IMGS, imgs);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 删除悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param publisher    悄悄话发表者
     * @return 数据是否删除成功
     */
    @Override
    public boolean delete(String pillowTalkId, String publisher) {
        String sql = "UPDATE pillow_talk SET deleted = 1 WHERE id = :id AND publisher = :publisher";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        params.put(PillowTalk.PUBLISHER, publisher);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 判断userId在悄悄或中承担的角色
     *
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     * @return 没有查到返回0，发表者1，另一方2
     */
    @Override
    public int judgeIdentity(String pillowTalkId, String userId) {
        String sql = "SELECT publisher, another FROM pillow_talk WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, params);
        int identity = 0;
        if (result != null && !result.isEmpty()) {
            if (userId.equals(result.get(PillowTalk.PUBLISHER).toString())) identity = 1;
            else if (userId.equals(result.get(PillowTalk.ANOTHER).toString())) identity = 2;
        }
        return identity;
    }


    /**
     * 公开悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param identity     用户在悄悄话中 承担的角色
     * @return 是否公开成功
     */
    @Override
    public boolean open(String pillowTalkId, int identity) {
        String sql = "";
        if (identity == 1)// 发表者
            sql = "UPDATE pillow_talk SET publisher_open = 1 WHERE id = :id";
        else if (identity == 2)// 另一方
            sql = "UPDATE pillow_talk SET another_open = 1 WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 发现
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> find(String userId, int page) {
        String sql = "SELECT pillow_talk.id AS id,pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content," +
                "pillow_talk.imgs," +
                "pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pub.id = pillow_talk.publisher " +
                "LEFT JOIN `user` AS ano ON ano.id = pillow_talk.another " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE pillow_talk.deleted = 0 AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND (type = 1 OR " +
                "(type = 0 AND publisher_open = 1 AND another_open = 1 AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.publisher AND lover_relationship.another = pillow_talk.another AND deleted = 0)) " +
                "AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.another AND lover_relationship.another = pillow_talk.publisher AND deleted = 0))) " +
                ")GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    /**
     * 发现记录总数量
     *
     * @return 发现记录总数量
     */
    @Override
    public int countOfFind() {
        String sql = "SELECT COUNT(pillow_talk.id) AS 'count' " +
                "FROM pillow_talk " +
                "WHERE deleted = 0 AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND (type = 1 OR " +
                "(type = 0 AND publisher_open = 1 AND another_open = 1 AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.publisher AND lover_relationship.another = pillow_talk.another AND deleted = 0)) " +
                "AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.another AND lover_relationship.another = pillow_talk.publisher AND deleted = 0))))";
        Map<String, Object> params = new HashMap<>();
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 查询恋人之间的悄悄话
     *
     * @param one     一方
     * @param another 相爱的另一方
     * @param page    请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> loversPillowTalk(String one, String another, int page) {
        String sql = "SELECT pillow_talk.id AS id,pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE (pillow_talk.type = 0 AND ((publisher = :one AND another = :another)||(publisher = :another AND another = :one))) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, one);
        params.put(LoverRelationship.ONE, one);
        params.put(LoverRelationship.ANOTHER, another);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    /**
     * 查询恋人之间的悄悄话记录总数量
     *
     * @param one     一方
     * @param another 相爱的另一方
     * @return 恋人之间的悄悄话记录总数量
     */
    @Override
    public int countOfLoversPillowTalk(String one, String another) {
        String sql = "SELECT COUNT(pillow_talk.id) AS 'count' " +
                "FROM pillow_talk " +
                "WHERE (pillow_talk.type = 0 AND ((publisher = :one AND another = :another)||(publisher = :another AND another = :one))) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(LoverRelationship.ONE, one);
        params.put(LoverRelationship.ANOTHER, another);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 根据id查询悄悄话详情
     *
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     * @return 悄悄话详情
     */
    @Override
    public PillowTalk query(String pillowTalkId, String userId) {
        String sql = "SELECT pillow_talk.id AS id,pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE pillow_talk.id = :id AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        params.put(PillowTalk.USER_ID, userId);
        List<PillowTalk> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 根据id查询悄悄话的发表者
     *
     * @param pillowTalkId 悄悄话id
     * @return 悄悄话的发表者
     */
    @Override
    public String queryOwner(String pillowTalkId) {
        String sql = "SELECT publisher FROM pillow_talk WHERE id = :id AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        return jdbcTemplate.queryForObject(sql, params, String.class);
    }

    /**
     * 根据id查询悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话id
     */
    @Override
    public PillowTalkProperties properties(String pillowTalkId) {
        String sql = "SELECT pillow_talk.publisher_open,pillow_talk.another_open," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count " +
                "FROM pillow_talk " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id " +
                "LEFT JOIN comment_pillow_talk ON comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0 " +
                "WHERE pillow_talk.id = :id AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.ID, pillowTalkId);
        List<PillowTalkProperties> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> {
            PillowTalkProperties properties = new PillowTalkProperties();
            properties.publisherOpen = rs.getInt(PillowTalk.PUBLISHER_OPEN);
            properties.anotherOpen = rs.getInt(PillowTalk.ANOTHER_OPEN);
            properties.praiseCount = rs.getInt(PillowTalk.PRAISE_COUNT);
            properties.commentCount = rs.getInt(PillowTalk.COMMENT_COUNT);
            return properties;
        });
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查询用户收藏的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> collectedPillowTalk(String userId, int page) {
        String sql = "SELECT pillow_talk.id AS id,pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE (pillow_talk.id IN (SELECT collect_pillow_talk.pillow_talk_id FROM collect_pillow_talk WHERE collect_pillow_talk.collecter = :user_id)) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    /**
     * 查询用户收藏的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfCollectedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) AS 'count' " +
                "FROM pillow_talk " +
                "WHERE (pillow_talk.id IN (SELECT collect_pillow_talk.pillow_talk_id FROM collect_pillow_talk WHERE collect_pillow_talk.collecter = :user_id)) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 查询用户赞过的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> praisedPillowTalk(String userId, int page) {
        String sql = "SELECT pillow_talk.id AS id," +
                "pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE (pillow_talk.id IN (SELECT praise_pillow_talk.pillow_talk_id FROM praise_pillow_talk WHERE praiser = :user_id)) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    /**
     * 查询用户赞过的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfPraisedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) AS 'count' " +
                "FROM pillow_talk " +
                "WHERE (pillow_talk.id IN (SELECT praise_pillow_talk.pillow_talk_id FROM praise_pillow_talk WHERE praiser = :user_id)) AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 查询用户评论过的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> commentedPillowTalk(String userId, int page) {
        String sql = "SELECT pillow_talk.id AS id," +
                "pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE (pillow_talk.id IN (SELECT DISTINCT pillow_talk_id FROM comment_pillow_talk WHERE commenter = :user_id AND deleted = 0)) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    @Override
    public int countOfCommentedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) AS 'count' " +
                "FROM pillow_talk " +
                "WHERE (pillow_talk.id IN (SELECT DISTINCT comment_pillow_talk.pillow_talk_id FROM comment_pillow_talk WHERE commenter = :user_id AND deleted = 0)) AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }

    /**
     * 查询指定用户发表的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> othersPillowTalk(String userId, int page) {
        String sql = "SELECT pillow_talk.id AS id,pillow_talk.publisher,pub.nickname AS publisher_nickname,pub.avatar AS publisher_avatar,pub.gender AS publisher_gender," +
                "pillow_talk.another,ano.nickname AS another_nickname,ano.avatar AS another_avatar,ano.gender AS another_gender," +
                "pillow_talk.content,pillow_talk.imgs,pillow_talk.type," +
                "pillow_talk.publisher_open,pillow_talk.another_open," +
                "pillow_talk.create_time," +
                "(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count," +
                "(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count," +
                "praise_pillow_talk.id AS praise_id," +
                "collect_pillow_talk.id AS collect_id " +
                "FROM pillow_talk " +
                "LEFT JOIN `user` AS pub ON pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano ON pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = :user_id " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = :user_id " +
                "WHERE publisher = :user_id AND ((pillow_talk.type = 0 AND another IS NOT NULL) || pillow_talk.type = 1) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillPillowTalk(rs));
    }

    /**
     * 查询指定用户发表的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfOthersPillowTalk(String userId) {
        String sql = "SELECT COUNT(*) AS 'count' FROM pillow_talk " +
                "WHERE publisher = :user_id AND ((pillow_talk.type = 0 AND another IS NOT NULL) || pillow_talk.type = 1) " +
                "AND (SELECT report_pillow_talk.pillow_talk_id FROM report_pillow_talk WHERE report_pillow_talk.pillow_talk_id = pillow_talk.id AND report_pillow_talk.checked = 1) IS NULL AND pillow_talk.deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(PillowTalk.USER_ID, userId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }
}
