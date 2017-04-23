package com.skye.lover.pillowtalk.comment.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.comment.dao.CommentDao;
import com.skye.lover.pillowtalk.comment.model.resp.Comment;
import com.skye.lover.user.model.resp.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论Dao实现
 */
@Repository
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
        String sql = "INSERT INTO comment_pillow_talk (pillow_talk_id,commenter,content) VALUES (:pillow_talk_id,:commenter,:content)";
        Map<String, Object> params = new HashMap<>();
        params.put(Comment.PILLOW_TALK_ID, pillowTalkId);
        params.put(Comment.COMMENTER, commenter);
        params.put(Comment.CONTENT, content);
        return jdbcTemplate.update(sql, params) > 0;
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
        String sql = "UPDATE comment_pillow_talk SET deleted = 1 WHERE id = :id AND commenter = :commenter";
        Map<String, Object> params = new HashMap<>();
        params.put(Comment.ID, commentId);
        params.put(Comment.COMMENTER, commenter);
        return jdbcTemplate.update(sql, params) > 0;
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
        String sql = "SELECT comment_pillow_talk.id,commenter,com.nickname,com.avatar,com.gender,content,comment_pillow_talk.create_time " +
                "FROM comment_pillow_talk " +
                "LEFT JOIN `user` AS com ON comment_pillow_talk.commenter = com.id " +
                "WHERE pillow_talk_id = :pillow_talk_id AND deleted = 0 ORDER BY create_time DESC LIMIT :os,:lt";
        Map<String, Object> params = new HashMap<>();
        params.put(Comment.PILLOW_TALK_ID, pillowTalkId);
        params.put("os", (page - 1) * ConstantUtil.PAGE_SIZE);
        params.put("lt", ConstantUtil.PAGE_SIZE);
        return jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> {
            Comment comment = new Comment();
            comment.id = rs.getString(Comment.ID);
            comment.pillowTalkId = pillowTalkId;
            comment.commenter = rs.getString(Comment.COMMENTER);
            comment.nickname = rs.getString(User.NICKNAME);
            comment.avatar = rs.getString(User.AVATAR);
            comment.gender = rs.getInt(User.GENDER);
            comment.content = CommonUtil.getStringFromByteArray(rs.getBytes(Comment.CONTENT));
            comment.createTime = CommonUtil.getTimestamp(rs.getString(Comment.CREATE_TIME));
            return comment;
        });
    }

    /**
     * 评论列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 评论列表记录总数
     */
    @Override
    public int countOfComments(String pillowTalkId) {
        String sql = "SELECT COUNT(id) AS 'count' FROM comment_pillow_talk WHERE pillow_talk_id = :pillow_talk_id AND deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put(Comment.PILLOW_TALK_ID, pillowTalkId);
        return jdbcTemplate.queryForObject(sql, params, int.class);
    }
}
