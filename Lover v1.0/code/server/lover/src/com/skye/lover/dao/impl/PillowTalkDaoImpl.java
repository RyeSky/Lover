package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.PillowTalkDao;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PillowTalkProperties;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 悄悄话DAO实现
 */
public class PillowTalkDaoImpl extends BaseDao implements PillowTalkDao {

    /**
     * 填充悄悄实体
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
        try {
            byte content[] = rs.getBytes(PillowTalk.CONTENT);
            pt.content = new String(content, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pt.imgs = rs.getString(PillowTalk.IMGS);
        pt.type = rs.getInt(PillowTalk.TYPE);
        pt.publisherOpen = rs.getInt(PillowTalk.PUBLISHER_OPEN);
        pt.anotherOpen = rs.getInt(PillowTalk.ANOTHER_OPEN);
        pt.praiseId = rs.getString(PillowTalk.PRAISE_ID);
        pt.collectId = rs.getString(PillowTalk.COLLECT_ID);
        pt.createTime = CommonUtil
                .getTimestamp(rs.getString(PillowTalk.CREATE_TIME));
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
    public boolean insert(String publisher, int type, String another, String content,
                          String imgs) {
        String sql = "insert into " + PillowTalk.TABLE_NAME + " ("
                + PillowTalk.PUBLISHER + "," + PillowTalk.TYPE + "," + PillowTalk.ANOTHER + ","
                + PillowTalk.CONTENT + "," + PillowTalk.IMGS
                + ") values (?,?,?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, publisher);
            pst.setInt(2, type);
            pst.setString(3, another);
            pst.setString(4, content);
            pst.setString(5, imgs);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
        String sql = "update " + PillowTalk.TABLE_NAME + " set "
                + PillowTalk.DELETED + " = 1 where " + PillowTalk.ID
                + " = ? and " + PillowTalk.PUBLISHER + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, publisher);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
        String sql = "select " + PillowTalk.PUBLISHER + " , "
                + PillowTalk.ANOTHER + " from " + PillowTalk.TABLE_NAME
                + " where " + PillowTalk.ID + " = ?";
        int identity = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (userId.equals(rs.getString(PillowTalk.PUBLISHER)))
                    identity = 1;
                else if (userId.equals(rs.getString(PillowTalk.ANOTHER)))
                    identity = 2;
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            sql = "update " + PillowTalk.TABLE_NAME + " set "
                    + PillowTalk.PUBLISHER_OPEN + " = 1 where " + PillowTalk.ID
                    + " = ?";
        else if (identity == 2)// 另一方
            sql = "update " + PillowTalk.TABLE_NAME + " set "
                    + PillowTalk.ANOTHER_OPEN + " = 1 where " + PillowTalk.ID
                    + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
                "LEFT JOIN `user` AS pub on pub.id = pillow_talk.publisher " +
                "LEFT JOIN `user` AS ano on ano.id = pillow_talk.another " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ?" +
                "WHERE pillow_talk.deleted = 0 AND (type = 1 OR " +
                "(type = 0 AND publisher_open = 1 AND another_open = 1 AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.publisher AND lover_relationship.another = pillow_talk.another AND deleted = 0)) " +
                "AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.another AND lover_relationship.another = pillow_talk.publisher AND deleted = 0))) " +
                ")GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setInt(3, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(4, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 发现记录总数量
     *
     * @return 发现记录总数量
     */
    @Override
    public int countOfFind() {
        String sql = "SELECT COUNT(pillow_talk.id) as 'count' " +
                "FROM pillow_talk " +
                "WHERE deleted = 0 AND (type = 1 OR " +
                "(type = 0 AND publisher_open = 1 AND another_open = 1 AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.publisher AND lover_relationship.another = pillow_talk.another AND deleted = 0)) " +
                "AND (EXISTS (SELECT * FROM lover_relationship WHERE lover_relationship.one = pillow_talk.another AND lover_relationship.another = pillow_talk.publisher AND deleted = 0))))";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
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
     * 查询恋人之间的悄悄话
     *
     * @param one     一方
     * @param another 相爱的另一方
     * @param page    请求第几页数据
     * @return 返回第page页的数据集合
     */
    @Override
    public List<PillowTalk> loversPillowTalk(String one, String another,
                                             int page) {
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ? " +
                "WHERE (pillow_talk.type = 0 AND ((publisher = ? AND another = ?)||(publisher = ? AND another = ?))) " +
                "AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, one);
            pst.setString(3, one);
            pst.setString(4, another);
            pst.setString(5, another);
            pst.setString(6, one);
            pst.setInt(7, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(8, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
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
        String sql = "SELECT COUNT(pillow_talk.id) as 'count' " +
                "FROM pillow_talk " +
                "WHERE (pillow_talk.type = 0 AND ((publisher = ? AND another = ?)||(publisher = ? AND another = ?))) " +
                "AND deleted = 0";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            pst.setString(3, another);
            pst.setString(4, one);
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ?" +
                "WHERE pillow_talk.id = ? AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time";
        PillowTalk pt = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setString(3, pillowTalkId);
            rs = pst.executeQuery();
            if (rs.next()) {
                pt = fillPillowTalk(rs);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pt;
    }

    /**
     * 根据id查询悄悄话的发表者
     *
     * @param pillowTalkId 悄悄话id
     * @return 悄悄话的发表者
     */
    @Override
    public String queryOwner(String pillowTalkId) {
        String sql = "SELECT publisher FROM pillow_talk WHERE id = ? AND deleted = 0";
        String owner = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            rs = pst.executeQuery();
            if (rs.next()) {
                owner = rs.getString(1);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    /**
     * 根据id查询悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话id
     */
    public PillowTalkProperties properties(String pillowTalkId) {
        String sql = "SELECT pillow_talk.publisher_open,pillow_talk.another_open,(SELECT COUNT(praise_pillow_talk.id) FROM praise_pillow_talk WHERE praise_pillow_talk.pillow_talk_id = pillow_talk.id) AS praise_count,(SELECT COUNT(comment_pillow_talk.id) FROM comment_pillow_talk WHERE comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0) AS comment_count FROM pillow_talk LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id LEFT JOIN comment_pillow_talk ON comment_pillow_talk.pillow_talk_id = pillow_talk.id AND comment_pillow_talk.deleted = 0 WHERE pillow_talk.id = ? AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time";
        PillowTalkProperties properties = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            rs = pst.executeQuery();
            if (rs.next()) {
                properties = new PillowTalkProperties();
                properties.publisherOpen = rs.getInt(PillowTalk.PUBLISHER_OPEN);
                properties.anotherOpen = rs.getInt(PillowTalk.ANOTHER_OPEN);
                properties.praiseCount = rs.getInt(PillowTalk.PRAISE_COUNT);
                properties.commentCount = rs.getInt(PillowTalk.COMMENT_COUNT);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }


    /**
     * 查询用户收藏的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ? " +
                "WHERE (pillow_talk.id IN (SELECT collect_pillow_talk.pillow_talk_id FROM collect_pillow_talk WHERE collect_pillow_talk.collecter = ?)) " +
                "AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setString(3, userId);
            pst.setInt(4, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(5, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 查询用户收藏的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfCollectedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) as 'count' FROM pillow_talk WHERE (pillow_talk.id IN (SELECT collect_pillow_talk.pillow_talk_id FROM collect_pillow_talk WHERE collect_pillow_talk.collecter = ?)) AND deleted = 0";
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ? " +
                "WHERE (pillow_talk.id IN (SELECT praise_pillow_talk.pillow_talk_id FROM praise_pillow_talk WHERE praiser = ?)) " +
                "AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setString(3, userId);
            pst.setInt(4, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(5, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 查询用户赞过的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfPraisedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) as 'count' FROM pillow_talk WHERE (pillow_talk.id IN (SELECT praise_pillow_talk.pillow_talk_id FROM praise_pillow_talk WHERE praiser = ?)) AND deleted = 0";
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ? " +
                "WHERE (pillow_talk.id IN (SELECT DISTINCT pillow_talk_id FROM comment_pillow_talk WHERE commenter = ? AND deleted = 0)) " +
                "AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setString(3, userId);
            pst.setInt(4, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(5, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 查询用户评论过的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfCommentedPillowTalk(String userId) {
        String sql = "SELECT COUNT(pillow_talk.id) as 'count' FROM pillow_talk WHERE (pillow_talk.id IN (SELECT DISTINCT comment_pillow_talk.pillow_talk_id FROM comment_pillow_talk WHERE commenter = ? AND deleted = 0)) AND deleted = 0";
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
                "LEFT JOIN `user` AS pub on pillow_talk.publisher = pub.id " +
                "LEFT JOIN `user` AS ano on pillow_talk.another = ano.id " +
                "LEFT JOIN praise_pillow_talk ON praise_pillow_talk.pillow_talk_id = pillow_talk.id AND praise_pillow_talk.praiser = ? " +
                "LEFT JOIN collect_pillow_talk ON collect_pillow_talk.pillow_talk_id = pillow_talk.id AND collect_pillow_talk.collecter = ? " +
                "WHERE publisher = ? AND ((pillow_talk.type = 0 AND another IS NOT NULL) || pillow_talk.type = 1) " +
                "AND pillow_talk.deleted = 0 GROUP BY pillow_talk.id ORDER BY pillow_talk.create_time DESC LIMIT ?,?";
        List<PillowTalk> list = new ArrayList<>();
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, userId);
            pst.setString(3, userId);
            pst.setInt(4, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(5, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(fillPillowTalk(rs));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }


    /**
     * 查询指定用户发表的悄悄话记录总数量
     *
     * @param userId 用户id
     */
    @Override
    public int countOfOthersPillowTalk(String userId) {
        String sql = "SELECT COUNT(*) AS 'count' FROM pillow_talk " +
                "WHERE publisher = ? AND ((pillow_talk.type = 0 AND another IS NOT NULL) || pillow_talk.type = 1) " +
                "AND pillow_talk.deleted = 0";
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
