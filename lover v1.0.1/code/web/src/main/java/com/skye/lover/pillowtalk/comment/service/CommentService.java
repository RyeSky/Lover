package com.skye.lover.pillowtalk.comment.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 评论业务层
 */
public interface CommentService {

    /**
     * 评论
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param commenter    评论发布者
     * @param content      评论内容
     */
    void insert(BaseResponse br, String pillowTalkId, String commenter, String content);

    /**
     * 删除评论
     *
     * @param br        返回数据基类
     * @param commentId 评论id
     * @param commenter 评论发布者
     */
    void delete(BaseResponse br, String commentId, String commenter);

    /**
     * 评论列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    void comments(BaseResponse br, String pillowTalkId, int page);
}
