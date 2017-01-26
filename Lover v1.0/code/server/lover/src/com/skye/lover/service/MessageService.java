package com.skye.lover.service;

import com.skye.lover.dao.impl.LoverRelationshipDaoImpl;
import com.skye.lover.dao.impl.MessageDaoImpl;
import com.skye.lover.dao.impl.UserDaoImpl;
import com.skye.lover.dao.interf.LoverRelationshipDao;
import com.skye.lover.dao.interf.MessageDao;
import com.skye.lover.dao.interf.UserDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

/**
 * 消息业务层
 */
public class MessageService {
    private MessageDao md = new MessageDaoImpl();
    private UserDao ud = new UserDaoImpl();
    private LoverRelationshipDao lrd = new LoverRelationshipDaoImpl();

    /**
     * 插入系统消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 数据是否插入成功
     */
    public boolean insertSystemMessage(String title, String content) {
        return md.insertSystemMessage(title, content);
    }

    /**
     * 插入示爱消息
     *
     * @param userId  被示爱的用户id
     * @param another 主动示爱的用户id
     * @return 消息实体
     */
    public Message insertCourtshipdisplayMessage(String userId, String another) {
        String title = "示爱";
        User user = ud.query(another);
        String content = (user == null ? "有人" : user.nickname) + "向你表达爱意，快去看看TA";
        String messageId = md.insert(userId, Message.TYPE_PERSONAL, Message.COURTSHIPDISPLAY, title, content, another, "0");
        if (CommonUtil.isBlank(messageId)) return null;
        return md.query(messageId);
    }

    /**
     * 插入示爱被同意消息
     *
     * @param userId  主动示爱的用户id
     * @param another 同意示爱的用户id
     * @return 消息实体
     */
    public Message insertCourtshipdisplayBeAgreedMessage(String userId, String another) {
        String title = "示爱被接受";
        User user = ud.query(another);
        String content = (user == null ? "对方" : user.nickname) + "接受了你的爱意，恭喜！";
        String messageId = md.insert(userId, Message.TYPE_PERSONAL, Message.COURTSHIPDISPLAY_BE_AGREED, title, content, another, "0");
        if (CommonUtil.isBlank(messageId)) return null;
        return md.query(messageId);
    }

    /**
     * 插入被分手消息
     *
     * @param userId  被分手的用户id
     * @param another 主动分手的用户id
     * @return 消息实体
     */
    public Message insertBrokedUpMessage(String userId, String another) {
        String title = "分手";
        User user = ud.query(another);
        String content = (user == null ? "对方" : user.nickname) + "解除了恋爱关系，如果你还爱TA，快去追回TA";
        String messageId = md.insert(userId, Message.TYPE_PERSONAL, Message.BE_BROKE_UP, title, content, another, "0");
        if (CommonUtil.isBlank(messageId)) return null;
        return md.query(messageId);
    }

    /**
     * 插入悄悄话被回复消息
     *
     * @param userId       被回复的用户id
     * @param another      主动回复的用户id
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息实体
     */
    public Message insertReplyMessage(String userId, String another, String pillowTalkId) {
        String title = "回复";
        String ano = lrd.queryAnother(userId);
        User anou = CommonUtil.isBlank(ano) ? null : ud.query(ano);
        String content = (anou == null ? "对方" : anou.nickname) + "回复了你的悄悄话";
        String messageId = md.insert(userId, Message.TYPE_PERSONAL, Message.REPLY, title, content, "0", pillowTalkId);
        if (CommonUtil.isBlank(messageId)) return null;
        return md.query(messageId);
    }

    /**
     * 插入悄悄话被评论消息
     *
     * @param userId       被评论的用户id
     * @param another      主动评论的用户id
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息实体
     */
    public Message insertCommentMessage(String userId, String another, String pillowTalkId) {
        String title = "评论";
        String ano = lrd.queryAnother(userId);
        User anou = CommonUtil.isBlank(ano) ? null : ud.query(ano);
        String content = (anou == null ? "对方" : anou.nickname) + "评论了你的悄悄话";
        String messageId = md.insert(userId, Message.TYPE_PERSONAL, Message.COMMENT, title, content, "0", pillowTalkId);
        if (CommonUtil.isBlank(messageId)) return null;
        return md.query(messageId);
    }

    /**
     * 删除指定消息
     *
     * @param br        返回数据基类
     * @param messageId 消息记录id
     * @param userId    消息所属用户
     */
    public void delete(BaseResponse br, String messageId, String userId) {
        if (!md.delete(messageId, userId)) {//删除数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 消息列表
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    public void messages(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = md.messages(userId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = md.countOfMessages(userId);
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
