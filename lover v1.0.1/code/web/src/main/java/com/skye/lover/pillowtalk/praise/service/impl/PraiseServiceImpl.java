package com.skye.lover.pillowtalk.praise.service.impl;

import com.skye.lover.pillowtalk.praise.dao.PraiseDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.praise.service.PraiseService;
import com.skye.lover.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 赞业务层实现
 */
@Service
public class PraiseServiceImpl implements PraiseService {
    private PraiseDao pd;

    @Autowired
    public PraiseServiceImpl(PraiseDao pd) {
        this.pd = pd;
    }

    /**
     * 赞悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    @Override
    public void praise(BaseResponse br, String pillowTalkId, String praiser) {
        String praiseId = pd.insert(pillowTalkId, praiser);
        if (!CommonUtil.isBlank(praiseId)) br.result = praiseId;
        else {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_PRAISE_FAIL;
        }
    }

    /**
     * 取消赞
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    @Override
    public void cancelPraise(BaseResponse br, String pillowTalkId, String praiser) {
        if (!pd.delete(pillowTalkId, praiser)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_CANCEL_FAIL;
        }
    }
}
