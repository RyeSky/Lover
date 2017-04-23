package com.skye.lover.message.service;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.message.model.resp.Message;

/**
 * 消息业务层
 */
public interface MessageService {
    /**
     * 插入系统消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 数据是否插入成功
     */
    boolean insertSystemMessage(String title, String content);

    /**
     * 插入示爱消息
     *
     * @param userId  被示爱的用户id
     * @param another 主动示爱的用户id
     * @return 消息实体
     */
    Message insertCourtshipdisplayMessage(String userId, String another);

    /**
     * 插入示爱被同意消息
     *
     * @param userId  主动示爱的用户id
     * @param another 同意示爱的用户id
     * @return 消息实体
     */
    Message insertCourtshipdisplayBeAgreedMessage(String userId, String another);

    /**
     * 插入被分手消息
     *
     * @param userId  被分手的用户id
     * @param another 主动分手的用户id
     * @return 消息实体
     */
    Message insertBeBrokedUpMessage(String userId, String another);

    /**
     * 插入悄悄话被回复消息
     *
     * @param userId       被回复的用户id
     * @param another      主动回复的用户id
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息实体
     */
    Message insertReplyMessage(String userId, String another, String pillowTalkId);

    /**
     * 插入悄悄话被评论消息
     *
     * @param userId       被评论的用户id
     * @param another      主动评论的用户id
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息实体
     */
    Message insertCommentMessage(String userId, String another, String pillowTalkId);

    /**
     * 使type指定类型的消息不可点击
     *
     * @param userId  用户id
     * @param subType 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】
     */
    void disableClick(String userId, int subType);

    /**
     * 删除指定消息
     *
     * @param br        返回数据基类
     * @param messageId 消息记录id
     * @param userId    消息所属用户
     */
    void delete(BaseResponse br, String messageId, String userId);

    /**
     * 消息列表
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void messages(BaseResponse br, String userId, int page);
}
