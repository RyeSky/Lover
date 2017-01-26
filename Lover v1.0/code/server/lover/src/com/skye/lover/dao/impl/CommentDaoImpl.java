package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.CommentDao;
import com.skye.lover.model.Comment;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论DAO实现
 */
public class CommentDaoImpl extends BaseDao implements CommentDao {
    /**
     * 发表评论
     *
     * @param pillowTalkId 悄悄话id
     * @param commenter    评论发布者
     * @param content      评论内容
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String pillowTalkId, String commenter, String content) {
        String sql = "insert into " + Comment.TABLE_NAME + " (" + Comment.PILLOW_TALK_ID + ","
                + Comment.COMMENTER + "," + Comment.CONTENT + ") values (?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setString(2, commenter);
            pst.setString(3, content);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @param commenter 评论发布者
     * @return 数据是否删除成功
     */
    @Override
    public boolean delete(String commentId, String commenter) {
        String sql = "update " + Comment.TABLE_NAME + " set " + Comment.DELETED + " = 1 where "
                + Comment.ID + " = ? and " + Comment.COMMENTER + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, commentId);
            pst.setString(2, commenter);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 评论列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     * @return 评论列表
     */
    @Override
    public List<Comment> comments(String pillowTalkId, int page) {
        String sql = "SELECT comment_pillow_talk.id,commenter,com.nickname,com.avatar,com.gender,content,comment_pillow_talk.create_time FROM comment_pillow_talk LEFT JOIN `user` AS com on comment_pillow_talk.commenter = com.id WHERE pillow_talk_id = ? AND deleted = 0 ORDER BY create_time DESC LIMIT ?,?";
        List<Comment> list = new ArrayList<>();
        Comment comment;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, pillowTalkId);
            pst.setInt(2, (page - 1) * Const.PAGE_SIZE);
            pst.setInt(3, Const.PAGE_SIZE);
            rs = pst.executeQuery();
            byte content[];
            while (rs.next()) {
                comment = new Comment();
                comment.id = rs.getString(Comment.ID);
                comment.pillowTalkId = pillowTalkId;
                comment.commenter = rs.getString(Comment.COMMENTER);
                comment.nickname = rs.getString(User.NICKNAME);
                comment.avatar = rs.getString(User.AVATAR);
                comment.gender = rs.getInt(User.GENDER);
                try {
                    content = rs.getBytes(Comment.CONTENT);
                    comment.content = new String(content, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                comment.createTime = CommonUtil.getTimestamp(rs.getString(Comment.CREATE_TIME));
                list.add(comment);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /**
     * 评论列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 评论列表记录总数
     */
    @Override
    public int countOfComments(String pillowTalkId) {
        String sql = "SELECT COUNT(id) AS 'count' FROM comment_pillow_talk WHERE pillow_talk_id = ? AND deleted = 0";
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
