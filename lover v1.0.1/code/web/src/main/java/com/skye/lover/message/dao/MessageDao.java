package com.skye.lover.message.dao;

import com.skye.lover.message.model.resp.Message;

import java.util.List;

/**
 * 消息Dao
 */
public interface MessageDao {
    /**
     * 插入消息
     *
     * @param userId       用户id
     * @param type         消息类型
     * @param subType      消息子类型
     * @param title        消息标题
     * @param content      消息内容
     * @param another      示爱的来源
     * @param pillowTalkId 被回复或评论的悄悄话id
     * @return 消息记录id
     */
    String insert(String userId, int type, int subType, String title, String content, String another, String pillowTalkId);


    /**
     * 插入系统消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 数据是否插入成功
     */
    boolean insertSystemMessage(String title, String content);

    /**
     * 删除指定消息
     *
     * @param messageId 消息记录id
     * @param userId    消息所属用户
     * @return 数据是否删除成功
     */
    boolean delete(String messageId, String userId);

    /**
     * 查询指定消息
     *
     * @param messageId 消息记录id
     * @return 消息实体
     */
    Message query(String messageId);

    /**
     * 消息列表
     *
     * @param userId 用户id
     * @param page   请求数据的页数
     * @return 消息数据集合
     */
    List<Message> messages(String userId, int page);

    /**
     * 消息列表总记录数
     *
     * @param userId 用户id
     * @return 消息列表总记录数
     */
    int countOfMessages(String userId);

    /**
     * 使type指定类型的消息不可点击
     *
     * @param userId  用户id
     * @param subType 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】
     * @return 数据是否更新成功
     */
    boolean disableClick(String userId, int subType);
}
