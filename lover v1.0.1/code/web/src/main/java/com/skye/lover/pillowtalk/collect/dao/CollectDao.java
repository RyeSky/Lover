package com.skye.lover.pillowtalk.collect.dao;

/**
 * 收藏悄悄话DAO
 */
public interface CollectDao {
    /**
     * 收藏悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 收藏记录id
     */
    String insert(String pillowTalkId, String collecter);

    /**
     * 取消收藏
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 数据是否插入成功
     */
    boolean delete(String pillowTalkId, String collecter);
}
