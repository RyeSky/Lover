package com.skye.lover.pillowtalk.report.service.impl;

import com.skye.lover.pillowtalk.report.dao.ReportPillowTalkDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.report.service.ReportPillowTalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 举报悄悄话业务层实现
 */
@Service
public class ReportPillowTalkServiceImpl implements ReportPillowTalkService {
    private ReportPillowTalkDao rptd;

    @Autowired
    public ReportPillowTalkServiceImpl(ReportPillowTalkDao rptd) {
        this.rptd = rptd;
    }

    /**
     * 插入举报数据
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     */
    @Override
    public void insert(BaseResponse br, String pillowTalkId, String reporter, String content) {
        if (!rptd.insert(pillowTalkId, reporter, content)) {//插入数据失败
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_REPORT_FAIL;
        }
    }
}
