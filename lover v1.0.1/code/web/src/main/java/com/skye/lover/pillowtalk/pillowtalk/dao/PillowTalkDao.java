package com.skye.lover.pillowtalk.pillowtalk.dao;

import java.util.List;

import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalkProperties;

/**
 * 悄悄话DAO
 */
public interface PillowTalkDao {

    /**
     * 插入悄悄话
     *
     * @param publisher 悄悄话发表者
     * @param type      类型【0:悄悄话；1:广播】
     * @param another   相爱的另一方
     * @param content   悄悄内容
     * @param imgs      多张图片路径，用英文逗号分隔
     * @return 数据是否插入成功
     */
    boolean insert(String publisher, int type, String another, String content, String imgs);

    /**
     * 删除悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param publisher    悄悄话发表者
     * @return 数据是否删除成功
     */
    boolean delete(String pillowTalkId, String publisher);

    /**
     * 判断userId在悄悄或中承担的角色
     *
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     * @return 没有查到返回0，发表者1，另一方2
     */
    int judgeIdentity(String pillowTalkId, String userId);

    /**
     * 公开悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param identity     用户在悄悄话中 承担的角色
     * @return 是否公开成功
     */
    boolean open(String pillowTalkId, int identity);

    /**
     * 发现
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> find(String userId, int page);

    /**
     * 发现记录总数量
     *
     * @return 发现记录总数量
     */
    int countOfFind();

    /**
     * 查询恋人之间的悄悄话
     *
     * @param one     一方
     * @param another 相爱的另一方
     * @param page    请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> loversPillowTalk(String one, String another,
                                      int page);

    /**
     * 查询恋人之间的悄悄话记录总数量
     *
     * @param one     一方
     * @param another 相爱的另一方
     * @return 恋人之间的悄悄话记录总数量
     */
    int countOfLoversPillowTalk(String one, String another);

    /**
     * 根据id查询悄悄话详情
     *
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     * @return 悄悄话详情
     */
    PillowTalk query(String pillowTalkId, String userId);

    /**
     * 根据id查询悄悄话的发表者
     *
     * @param pillowTalkId 悄悄话id
     * @return 悄悄话的发表者
     */
    String queryOwner(String pillowTalkId);


    /**
     * 根据id查询悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话id
     * @return 悄悄话部分属性实体
     */
    PillowTalkProperties properties(String pillowTalkId);


    /**
     * 查询用户收藏的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> collectedPillowTalk(String userId, int page);

    /**
     * 查询用户收藏的悄悄话记录总数量
     *
     * @param userId 用户id
     * @return 用户收藏的悄悄话记录总数量
     */
    int countOfCollectedPillowTalk(String userId);

    /**
     * 查询用户赞过的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> praisedPillowTalk(String userId, int page);

    /**
     * 查询用户赞过的悄悄话记录总数量
     *
     * @param userId 用户id
     * @return 用户赞过的悄悄话记录总数量
     */
    int countOfPraisedPillowTalk(String userId);

    /**
     * 查询用户评论过的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> commentedPillowTalk(String userId, int page);

    /**
     * 查询用户评论过的悄悄话记录总数量
     *
     * @param userId 用户id
     * @return 用户评论过的悄悄话记录总数量
     */
    int countOfCommentedPillowTalk(String userId);

    /**
     * 查询指定用户发表的悄悄话
     *
     * @param userId 用户id
     * @param page   请求第几页数据
     * @return 返回第page页的数据集合
     */
    List<PillowTalk> othersPillowTalk(String userId, int page);


    /**
     * 查询指定用户发表的悄悄话记录总数量
     *
     * @param userId 用户id
     * @return 指定用户发表的悄悄话记录总数量
     */
    int countOfOthersPillowTalk(String userId);
}
