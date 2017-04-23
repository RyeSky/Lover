package com.skye.lover.loverrelatiionship.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 相恋关系业务层
 */
public interface LoverRelationshipService {
    /**
     * 坠入爱河
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     */
    void fallInLove(BaseResponse br, String one, String another);

    /**
     * 分手
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     */
    void breakUp(BaseResponse br, String one, String another);
}
