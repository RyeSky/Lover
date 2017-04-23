package com.skye.lover.pillowtalk.report.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.report.dao.ReportPillowTalkDao;
import com.skye.lover.pillowtalk.report.model.req.ReportPillowTalkRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 举报悄悄话Dao实现
 */
@Repository
public class ReportPillowTalkDaoImpl extends BaseDao implements ReportPillowTalkDao {
    /**
     * 插入举报数据
     *
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     * @return 数据是否插入成功
     */
    @Override
    public boolean insert(String pillowTalkId, String reporter, String content) {
        String sql = "INSERT INTO  report_pillow_talk (pillow_talk_id, reporter, content) VALUES (:pillow_talk_id, :reporter, :content)";
        Map<String, Object> params = new HashMap<>();
        params.put(ReportPillowTalkRequest.PILLOW_TALK_ID, pillowTalkId);
        params.put(ReportPillowTalkRequest.REPORTER, reporter);
        params.put(ReportPillowTalkRequest.CONTENT, content);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
