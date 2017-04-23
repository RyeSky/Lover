package com.skye.lover.pillowtalk.report.dao;

/**
 * 举报悄悄话Dao
 */
public interface ReportPillowTalkDao {
    /**
     * 插入举报数据
     *
     * @param pillowTalkId 悄悄话id
     * @param reporter     举报人id
     * @param content      举报理由
     * @return 数据是否插入成功
     */
    boolean insert(String pillowTalkId, String reporter, String content);
}
