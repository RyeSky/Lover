package com.skye.lover.pillowtalk.pillowtalk.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 悄悄话业务层
 */
public interface PillowTalkService {
    /**
     * 插入悄悄话
     *
     * @param br        返回数据基类
     * @param publisher 悄悄话发表者
     * @param type      类型【0:悄悄话；1:广播】
     * @param content   悄悄内容
     * @param imgs      多张图片路径，用英文逗号分隔
     */
    void insert(BaseResponse br, String publisher, int type, String content, String imgs);

    /**
     * 删除悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param publisher    悄悄话发表者
     */
    void delete(BaseResponse br, String pillowTalkId, String publisher);

    /**
     * 公开悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     */
    void open(BaseResponse br, String pillowTalkId, String userId);

    /**
     * 发现
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void find(BaseResponse br, String userId, int page);

    /**
     * 蜜语
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     * @param page    请求第几页数据
     */
    void honeyWord(BaseResponse br, String one, String another, int page);

    /**
     * 根据id查询悄悄话详情
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     */
    void query(BaseResponse br, String pillowTalkId, String userId);

    /**
     * 根据id查询悄悄话部分属性
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     */
    void properties(BaseResponse br, String pillowTalkId);

    /**
     * 收藏的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void collected(BaseResponse br, String userId, int page);

    /**
     * 赞过的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void praised(BaseResponse br, String userId, int page);

    /**
     * 评论过的的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void commented(BaseResponse br, String userId, int page);

    /**
     * 发表过的的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    void others(BaseResponse br, String userId, int page);
}
