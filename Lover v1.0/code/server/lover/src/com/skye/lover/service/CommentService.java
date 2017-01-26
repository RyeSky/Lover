package com.skye.lover.service;

import com.skye.lover.dao.impl.CommentDaoImpl;
import com.skye.lover.dao.impl.PillowTalkDaoImpl;
import com.skye.lover.dao.interf.CommentDao;
import com.skye.lover.dao.interf.PillowTalkDao;
import com.skye.lover.dao.interf.PrivateMessageDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;
import com.skye.lover.util.JPushUtil;

/**
 * 评论业务层
 */
public class CommentService {
    private CommentDao cd = new CommentDaoImpl();
    private PillowTalkDao ptd = new PillowTalkDaoImpl();
    private MessageService ms = new MessageService();

    /**
     * 评论
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param commenter    评论发布者
     * @param content      评论内容
     */
    public void insert(BaseResponse br, String pillowTalkId, String commenter, String content) {
        if (!cd.insert(pillowTalkId, commenter, content)) {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
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
    public void delete(BaseResponse br, String commentId, String commenter) {
        if (!cd.delete(commentId, commenter)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }


    /**
     * 评论列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    public void comments(BaseResponse br, String pillowTalkId, int page) {
        if (page <= 0)
            page = 1;
        Object list = cd.comments(pillowTalkId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = cd.countOfComments(pillowTalkId);
            lr.pageCount = count / Const.PAGE_SIZE;
            if (count % Const.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
