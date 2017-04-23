package com.skye.lover.pillowtalk.collect.service.impl;

import com.skye.lover.pillowtalk.collect.dao.CollectDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.collect.service.CollectService;
import com.skye.lover.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收藏业务层实现
 */
@Service
public class CollectServiceImpl implements CollectService {
    private CollectDao cd;

    @Autowired
    public CollectServiceImpl(CollectDao cd) {
        this.cd = cd;
    }

    /**
     * 收藏悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     */
    @Override
    public void collect(BaseResponse br, String pillowTalkId, String collecter) {
        String collectId = cd.insert(pillowTalkId, collecter);
        if (!CommonUtil.isBlank(collectId)) br.result = collectId;
        else {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_COLLECT_FAIL;
        }
    }

    /**
     * 取消收藏
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     */
    @Override
    public void cancelCollect(BaseResponse br, String pillowTalkId, String collecter) {
        if (!cd.delete(pillowTalkId, collecter)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_CANCEL_FAIL;
        }
    }
}
