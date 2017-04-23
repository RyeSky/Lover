package com.skye.lover.pillowtalk.collect.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 收藏业务层
 */
public interface CollectService {
    /**
     * 收藏悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     */
    void collect(BaseResponse br, String pillowTalkId, String collecter);

    /**
     * 取消收藏
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     */
    void cancelCollect(BaseResponse br, String pillowTalkId, String collecter);
}
