package com.skye.lover.privatemessage.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 私信业务层
 */
public interface PrivateMessageService {

    /**
     * 插入私信
     *
     * @param br       返回数据基类
     * @param sender   私信发送者id
     * @param receiver 私信接受者id
     * @param content  私信内容
     */
    void insert(BaseResponse br, String sender, String receiver, String content);

    /**
     * 会话列表
     *
     * @param br     返回数据基类
     * @param userId 用户id
     */
    void queryPrivateMessageSessions(BaseResponse br, String userId);

    /**
     * 查询聊天记录
     *
     * @param br               返回数据基类
     * @param one              聊天中的一方
     * @param another          聊天中的另一方
     * @param privateMessageId 指定的私信记录id
     */
    void queryPrivateMessages(BaseResponse br, String one, String another, String privateMessageId);

    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param br               返回数据基类
     * @param privateMessageId 私信记录id
     * @param chater           聊天者id
     */
    void deleteByPrivateMessageId(BaseResponse br, String privateMessageId, String chater);

    /**
     * 根据聊天的两个用户删除整个会话记录
     *
     * @param br      返回数据基类
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     */
    void deleteByPrivateMessageSession(BaseResponse br, String one, String another);
}
