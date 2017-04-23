package com.skye.lover.privatemessage.dao;

import com.skye.lover.privatemessage.model.resp.PrivateMessage;

import java.util.List;

/**
 * 私信DAO
 */
public interface PrivateMessageDao {
    /**
     * 插入私信
     *
     * @param sender   私信发送者id
     * @param receiver 私信接受者id
     * @param content  私信内容
     * @return 私信记录id
     */
    String insert(String sender, String receiver, String content);

    /**
     * 查询聊天记录
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @param offset  记录偏移量
     * @return 聊天记录数据集合
     */
    List<PrivateMessage> query(String one, String another, int offset);

    /**
     * 根据私信记录id查询
     *
     * @param privateMessageId 私信记录id
     * @return 私信记录id对应的实体
     */
    PrivateMessage query(String privateMessageId);

    /**
     * 查询私信总记录数
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 私信总记录数
     */
    int countOfPrivateMessage(String one, String another);

    /**
     * 查询指定私信记录id之前的私信总记录数
     *
     * @param one              聊天中的一方
     * @param another          聊天中的另一方
     * @param privateMessageId 指定的私信记录id
     * @return 指定私信记录id之前的私信总记录数
     */
    int countOfBefore(String one, String another, String privateMessageId);


    /**
     * 查询第一条私信记录的id
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 第一条私信记录的id
     */
    String queryFirstPrivateMessageId(String one, String another);

    /**
     * 查询最后一条私信记录的id
     *
     * @param one     聊天中的一方
     * @param another 聊天中的另一方
     * @return 最后一条私信记录的id
     */
    String queryLastPrivateMessageId(String one, String another);
}
