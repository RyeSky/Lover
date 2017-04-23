package com.skye.lover.pillowtalk.followpillowtalk.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 跟随悄悄话业务层业务层
 */
public interface FollowPillowTalkService {
    /**
     * 插入跟随悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param publisher    跟随悄悄话发布者
     * @param content      跟随悄悄话内容
     */
    void insert(BaseResponse br, String pillowTalkId, String publisher, String content);

    /**
     * 删除跟随悄悄话
     *
     * @param br                 返回数据基类
     * @param follwoPillowTalkId 跟随悄悄话id
     * @param publisher          跟随悄悄话发布者
     */
    void delete(BaseResponse br, String follwoPillowTalkId, String publisher);

    /**
     * 跟随悄悄话列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    void follows(BaseResponse br, String pillowTalkId, int page);
}
