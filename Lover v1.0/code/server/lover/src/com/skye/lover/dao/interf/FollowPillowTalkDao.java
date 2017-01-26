package com.skye.lover.dao.interf;

import com.skye.lover.model.FollowPillowTalk;

import java.util.List;

/**
 * 跟随悄悄话DAO
 */
public interface FollowPillowTalkDao {
    /**
     * 发表跟随悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param publisher    跟随悄悄话发布者
     * @param content      跟随悄悄话内容
     * @return 数据是否插入成功
     */
    public boolean insert(String pillowTalkId, String publisher, String content);

    /**
     * 删除跟随悄悄话
     *
     * @param follwoPillowTalkId 跟随悄悄话id
     * @param publisher          跟随悄悄话发布者
     * @return 数据是否删除成功
     */
    public boolean delete(String follwoPillowTalkId, String publisher);

    /**
     * 跟随悄悄话列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     * @return 跟随悄悄话列表
     */
    public List<FollowPillowTalk> follows(String pillowTalkId, int page);

    /**
     * 跟随悄悄话列表记录总数
     *
     * @param pillowTalkId 悄悄话id
     * @return 跟随悄悄话列表记录总数
     */
    public int countOfFollows(String pillowTalkId);
}
