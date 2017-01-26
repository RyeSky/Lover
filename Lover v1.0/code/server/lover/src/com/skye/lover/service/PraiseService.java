package com.skye.lover.service;

import com.skye.lover.dao.impl.PraiseDaoImpl;
import com.skye.lover.dao.interf.PraiseDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;

/**
 * 赞业务层
 */
public class PraiseService {
    private PraiseDao pd = new PraiseDaoImpl();

    /**
     * 赞悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    public void praise(BaseResponse br, String pillowTalkId, String praiser) {
        String praiseId = pd.insert(pillowTalkId, praiser);
        if (!CommonUtil.isBlank(praiseId)) br.result = praiseId;
        else {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 取消赞
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     */
    public void cancelPraise(BaseResponse br, String pillowTalkId, String praiser) {
        if (!pd.delete(pillowTalkId, praiser)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
