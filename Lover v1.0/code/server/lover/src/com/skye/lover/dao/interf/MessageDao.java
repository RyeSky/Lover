package com.skye.lover.dao.interf;

import com.skye.lover.model.Message;

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
    public String insert(String userId, int type, int subType, String title, String content, String another, String pillowTalkId);


    /**
     * 插入系统消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 数据是否插入成功
     */
    public boolean insertSystemMessage(String title, String content);

    /**
     * 删除指定消息
     *
     * @param messageId 消息记录id
     * @param userId    消息所属用户
     * @return 数据是否删除成功
     */
    public boolean delete(String messageId, String userId);

    /**
     * 查询指定消息
     *
     * @param messageId 消息记录id
     * @return 消息实体
     */
    public Message query(String messageId);

    /**
     * 消息列表
     *
     * @param userId 用户id
     * @param page   请求数据的页数
     * @return 消息数据集合
     */
    public List<Message> messages(String userId, int page);

    /**
     * 消息列表总记录数
     *
     * @param userId 用户id
     * @return 消息列表总记录数
     */
    public int countOfMessages(String userId);
}
