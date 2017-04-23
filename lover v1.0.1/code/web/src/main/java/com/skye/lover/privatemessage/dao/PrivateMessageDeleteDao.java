package com.skye.lover.privatemessage.dao;

/**
 * 删除私信DAO
 */
public interface PrivateMessageDeleteDao {
    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param privateMessageId 私信记录id
     * @param chater           聊天者id
     * @return 私信记录id
     */
    boolean delete(String privateMessageId, String chater);

    /**
     * 根据聊天的两个用户删除整个会话记录
     *
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     * @return 是否删除成功
     */
    boolean deleteByChater(String one, String another);
}
