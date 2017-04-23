package com.skye.lover.pillowtalk.praise.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 赞业务层
 */
public interface PraiseService {
    /**
     * 赞悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    void praise(BaseResponse br, String pillowTalkId, String praiser);

    /**
     * 取消赞
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    void cancelPraise(BaseResponse br, String pillowTalkId, String praiser);
}
