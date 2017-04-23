package com.skye.lover.pillowtalk.report.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 举报悄悄话业务层
 */
public interface ReportPillowTalkService {
    /**
     * 插入举报数据
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     */
    void insert(BaseResponse br, String pillowTalkId, String reporter, String content);
}
