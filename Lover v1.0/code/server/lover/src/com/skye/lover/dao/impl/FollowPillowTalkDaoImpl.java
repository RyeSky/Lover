package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.FollowPillowTalkDao;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 跟随悄悄话DAO实现
 */
public class FollowPillowTalkDaoImpl extends BaseDao implements
        FollowPillowTalkDao {
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
        String sql = "insert into " + FollowPillowTalk.TABLE_NAME + " (" + FollowPillowTalk.PILLOW_TALK_ID + ","
                + FollowPillowTalk.PUBLISHER + "," + FollowPillowTalk.CONTENT + ") values (?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, publisher);
            pst.setString(3, content);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
        String sql = "update " + FollowPillowTalk.TABLE_NAME + " set " + FollowPillowTalk.DELETED + " = 1 where "
                + FollowPillowTalk.ID + " = ? and " + FollowPillowTalk.PUBLISHER + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, follwoPillowTalkId);
            pst.setString(2, publisher);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }


    /**
     * 跟随悄悄话列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     * @return 跟随悄悄话列表
     */
    @Override
    public List<FollowPillowTalk> follows(String pillowTalkId, int page) {
        String sql = "SELECT follow_pillow_talk.id,publisher,pub.nickname,pub.avatar,pub.gender,content,follow_pillow_talk.create_time FROM follow_pillow_talk LEFT JOIN `user` AS pub on follow_pillow_talk.publisher = pub.id WHERE pillow_talk_id = ? AND deleted = 0 ORDER BY create_time DESC LIMIT ?,?";
        List<FollowPillowTalk> list = new ArrayList<>();
        FollowPillowTalk fpt;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setInt(2, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(3, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            byte content[];
            while (rs.next()) {
                fpt = new FollowPillowTalk();
                fpt.id = rs.getString(FollowPillowTalk.ID);
                fpt.pillowTalkId = pillowTalkId;
                fpt.publisher = rs.getString(FollowPillowTalk.PUBLISHER);
                fpt.nickname = rs.getString(User.NICKNAME);
                fpt.avatar = rs.getString(User.AVATAR);
                fpt.gender = rs.getInt(User.GENDER);
                try {
                    content = rs.getBytes(FollowPillowTalk.CONTENT);
                    fpt.content = new String(content, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fpt.createTime = CommonUtil.getTimestamp(rs.getString(FollowPillowTalk.CREATE_TIME));
                list.add(fpt);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }


    /**
     * 跟随悄悄话列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 跟随悄悄话列表记录总数
     */
    @Override
    public int countOfFollows(String pillowTalkId) {
        String sql = "SELECT COUNT(id) AS 'count' FROM follow_pillow_talk WHERE pillow_talk_id = ? AND deleted = 0";
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
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
