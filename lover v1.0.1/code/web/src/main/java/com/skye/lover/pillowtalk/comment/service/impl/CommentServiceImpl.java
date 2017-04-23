package com.skye.lover.pillowtalk.comment.service.impl;

import com.skye.lover.pillowtalk.comment.dao.CommentDao;
import com.skye.lover.pillowtalk.pillowtalk.dao.PillowTalkDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.model.resp.ListResponse;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.pillowtalk.comment.service.CommentService;
import com.skye.lover.message.service.MessageService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import com.skye.lover.util.JPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论业务层实现
 */
@Service
public class CommentServiceImpl implements CommentService {
    private CommentDao cd;
    private PillowTalkDao ptd;
    private MessageService ms;

    @Autowired
    public CommentServiceImpl(CommentDao cd, PillowTalkDao ptd, MessageService ms) {
        this.cd = cd;
        this.ptd = ptd;
        this.ms = ms;
    }

    /**
     * 评论
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param commenter    评论发布者
     * @param content      评论内容
     */
    @Override
    public void insert(BaseResponse br, String pillowTalkId, String commenter, String content) {
        if (!cd.insert(pillowTalkId, commenter, content)) {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_COMMENT_FAIL;
        } else {//评论成功时，被评论的用户会收到一条通知消息
            String owner = ptd.queryOwner(pillowTalkId);
            if (!CommonUtil.isBlank(owner) && !owner.equals(commenter)) {
                Message message = ms.insertCommentMessage(owner, commenter, pillowTalkId);
                if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
            }
        }
    }

    /**
     * 删除评论
     *
     * @param br        返回数据基类
     * @param commentId 评论id
     * @param commenter 评论发布者
     */
    @Override
    public void delete(BaseResponse br, String commentId, String commenter) {
        if (!cd.delete(commentId, commenter)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_DELETE_FAIL;
        }
    }


    /**
     * 评论列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    @Override
    public void comments(BaseResponse br, String pillowTalkId, int page) {
        if (page <= 0)
            page = 1;
        Object list = cd.comments(pillowTalkId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = cd.countOfComments(pillowTalkId);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
