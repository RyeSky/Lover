package com.skye.lover.dao.interf;

import com.skye.lover.model.PrivateMessageSession;

import java.util.List;

/**
 * 私信会话DAO
 */
public interface PrivateMessageSessionDao {
    /**
     * 插入私信会话
     *
     * @param one              聊天中的一方id
     * @param another          聊天中的另一方id
     * @param privateMessageId 私信记录id
     * @return 私信会话记录id
     */
    public String insert(String one, String another, String privateMessageId);

    /**
     * 会话列表
     *
     * @param userId 用户id
     * @return 会话列表数据集合
     */
    public List<PrivateMessageSession> query(String userId);

    /**
     * 删除私信会话
     *
     * @param one     聊天中的一方id
     * @param another 聊天中的另一方id
     * @return 是否删除成功
     */
    public boolean delete(String one, String another);
}
