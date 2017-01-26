package com.skye.lover.service;

import com.skye.lover.dao.impl.CollectDaoImpl;
import com.skye.lover.dao.interf.CollectDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;

/**
 * 收藏业务层
 */
public class CollectService {
    private CollectDao cd = new CollectDaoImpl();

    /**
     * 收藏悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     */
    public void collect(BaseResponse br, String pillowTalkId, String collecter) {
        String collectId = cd.insert(pillowTalkId, collecter);
        if (!CommonUtil.isBlank(collectId)) br.result = collectId;
        else {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 取消收藏
     *
     * @param br        返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter 收藏者
     */
    public void cancelCollect(BaseResponse br, String pillowTalkId, String collecter) {
        if (!cd.delete(pillowTalkId, collecter)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
