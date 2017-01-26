package com.skye.lover.service;

import com.skye.lover.dao.impl.ReportPillowTalkDaoImpl;
import com.skye.lover.dao.interf.ReportPillowTalkDao;
import com.skye.lover.model.BaseResponse;

/**
 * 举报悄悄话业务层
 */
public class ReportPillowTalkService {
    private ReportPillowTalkDao rptd = new ReportPillowTalkDaoImpl();


    /**
     * 插入举报数据
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     */
    public void insert(BaseResponse br, String pillowTalkId, String reporter, String content) {
        if (!rptd.insert(pillowTalkId, reporter, content)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
