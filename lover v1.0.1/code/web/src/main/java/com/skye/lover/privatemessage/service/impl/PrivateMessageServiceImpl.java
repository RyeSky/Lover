package com.skye.lover.privatemessage.service.impl;

import com.skye.lover.privatemessage.dao.PrivateMessageDao;
import com.skye.lover.privatemessage.dao.PrivateMessageDeleteDao;
import com.skye.lover.privatemessage.dao.PrivateMessageSessionDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.privatemessage.model.resp.PrivateMessage;
import com.skye.lover.privatemessage.model.resp.PrivateMessagesWrapper;
import com.skye.lover.privatemessage.service.PrivateMessageService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.JPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 私信业务层
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {
    private PrivateMessageSessionDao pmsd;
    private PrivateMessageDao pmd;
    private PrivateMessageDeleteDao pmdd;

    @Autowired
    public PrivateMessageServiceImpl(PrivateMessageSessionDao pmsd, PrivateMessageDao pmd, PrivateMessageDeleteDao pmdd) {
        this.pmsd = pmsd;
        this.pmd = pmd;
        this.pmdd = pmdd;
    }

    /**
     * 插入私信
     *
     * @param br       返回数据基类
     * @param sender   私信发送者id
     * @param receiver 私信接受者id
     * @param content  私信内容
     */
    @Override
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
                    br.message = BaseResponse.MESSAGE_SEND_FAIL;
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
                    message.clickAble = 1;
                    message.createTime = pm.createTime;
                    JPushUtil.sendCustomMessage(message);
                }
            } else {//插入私信记录失败
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_SEND_FAIL;
            }
        } else {//插入私信记录失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_SEND_FAIL;
        }
    }

    /**
     * 会话列表
     *
     * @param br     返回数据基类
     * @param userId 用户id
     */
    @Override
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
    @Override
    public void queryPrivateMessages(BaseResponse br, String one, String another, String privateMessageId) {
        Object list = pmd.query(one, another, CommonUtil.isBlank(privateMessageId) ? 0 : pmd.countOfBefore(one, another, privateMessageId));
        if (list != null) {
            br.result = new PrivateMessagesWrapper(list, pmd.queryFirstPrivateMessageId(one, another));
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
            br.message = BaseResponse.MESSAGE_DELETE_FAIL;
        }
    }


    /**
     * 根据聊天的两个用户删除整个会话记录
     *
     * @param br      返回数据基类
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     */
    @Override
    public void deleteByPrivateMessageSession(BaseResponse br, String one, String another) {
        //先删除私信记录
        if (pmdd.deleteByChater(one, another)) {
            //再删除私信会话记录
            if (!pmsd.delete(one, another)) {//删除数据时出错
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_DELETE_FAIL;
            }
        } else {//删除数据时出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_DELETE_FAIL;
        }
    }
}
