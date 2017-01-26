package com.skye.lover.dao.interf;

/**
 * 赞DAO
 */
public interface PraiseDao {
    /**
     * 赞悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param praiser    赞者
     * @return 赞记录id
     */
    public String insert(String pillowTalkId, String praiser);

    /**
     * 取消收藏
     *
     * @param pillowTalkId 悄悄话id
     * @param praiser    赞者
     * @return 数据是否删除成功
     */
    public boolean delete(String pillowTalkId, String praiser);
}
