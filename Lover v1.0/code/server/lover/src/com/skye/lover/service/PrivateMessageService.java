package com.skye.lover.service;

import com.skye.lover.dao.impl.PrivateMessageDaoImpl;
import com.skye.lover.dao.impl.PrivateMessageDeleteDaoImpl;
import com.skye.lover.dao.impl.PrivateMessageSessionDaoImpl;
import com.skye.lover.dao.interf.PrivateMessageDao;
import com.skye.lover.dao.interf.PrivateMessageDeleteDao;
import com.skye.lover.dao.interf.PrivateMessageSessionDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.model.PrivateMessagesResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.JPushUtil;

/**
 * 私信业务层
 */
public class PrivateMessageService {
    private PrivateMessageSessionDao pmsd = new PrivateMessageSessionDaoImpl();
    private PrivateMessageDao pmd = new PrivateMessageDaoImpl();
    private PrivateMessageDeleteDao pmdd = new PrivateMessageDeleteDaoImpl();

    /**
     * 插入私信
     *
     * @param br       返回数据基类
     * @param sender   私信发送者id
     * @param receiver 私信接受者id
     * @param content  私信内容
     */
    public void insert(BaseResponse br, String sender, String receiver, String content) {
        //先插入私信记录
        String privateMessageId = pmd.insert(sender, receiver, content);
        if (!CommonUtil.isBlank(privateMessageId)) {
            //然后得到新插入的数据实体
            PrivateMessage pm = pmd.query(privateMessageId);
            if (pm != null) {
                br.result = pm;
                //再插入双方各自的私信会话记录
                String sessionId1 = pmsd.insert(sender, receiver, privateMessageId);
                String sessionId2 = pmsd.insert(receiver, sender, privateMessageId);
                if (CommonUtil.isBlank(sessionId1) || CommonUtil.isBlank(sessionId2)) {//插入私信会话失败
                    br.code = BaseResponse.CODE_FAIL;
                    br.message = BaseResponse.MESSAGE_FAIL;
                } else {
                    Message message = new Message();
                    message.id = pm.id;
                    message.type = Message.TYPE_PERSONAL;
                    message.subType = Message.PRIVATE_MESSAGE;
                    message.userId = receiver;
                    message.anotherId = pm.sender;
                    message.anotherNickname = pm.senderNickname;
                    message.anotherAvatar = pm.senderAvatar;
                    message.anotherGender = pm.senderGender;
                    message.title = "收到一条" + pm.senderNickname + "发来的私信";
                    message.content = pm.content;
                    message.createTime = pm.createTime;
                    JPushUtil.sendCustomMessage(message);
                }
            } else {//插入私信记录失败
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {//插入私信记录失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 会话列表
     *
     * @param br     返回数据基类
     * @param userId 用户id
     */
    public void queryPrivateMessageSessions(BaseResponse br, String userId) {
        Object list = pmsd.query(userId);
        if (list != null) {
            br.result = list;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 查询聊天记录
     *
     * @param br               返回数据基类
     * @param one              聊天中的一方
     * @param another          聊天中的另一方
     * @param privateMessageId 指定的私信记录id
     */
    public void queryPrivateMessages(BaseResponse br, String one, String another, String privateMessageId) {
        Object list = pmd.query(one, another, CommonUtil.isBlank(privateMessageId) ? 0 : pmd.countOfBefore(one, another, privateMessageId));
        if (list != null) {
            br.result = new PrivateMessagesResponse(list, pmd.queryFirstPrivateMessageId(one, another));
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param br               返回数据基类
     * @param privateMessageId 私信记录id
     * @param chater           聊天者id
     */
    public void deleteByPrivateMessageId(BaseResponse br, String privateMessageId, String chater) {
        if (!pmdd.delete(privateMessageId, chater)) {//删除数据时出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }


    /**
     * 根据聊天的两个用户删除整个会话记录
     *
     * @param br      返回数据基类
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     */
    public void deleteByPrivateMessageSession(BaseResponse br, String one, String another) {
        //先删除私信记录
        if (pmdd.deleteByChater(one, another)) {
            //再删除私信会话记录
            if (pmsd.delete(one, another)) {//删除数据时出错
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {//删除数据时出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
