package com.skye.lover.dao.interf;

import com.skye.lover.model.Comment;
import com.skye.lover.model.FollowPillowTalk;

import java.util.List;

/**
 * 评论DAO
 */
public interface CommentDao {
    /**
     * 发表评论
     *
     * @param pillowTalkId 悄悄话id
     * @param commenter    评论发布者
     * @param content      评论内容
     * @return 数据是否插入成功
     */
    public boolean insert(String pillowTalkId, String commenter, String content);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @param commenter 评论发布者
     * @return 数据是否删除成功
     */
    public boolean delete(String commentId, String commenter);

    /**
     * 评论列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     * @return 评论列表
     */
    public List<Comment> comments(String pillowTalkId, int page);

    /**
     * 评论列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 评论列表记录总数
     */
    public int countOfComments(String pillowTalkId);
}
