package com.skye.lover.pillowtalk.report.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.util.CommonUtil;

/**
 * 举报悄悄话请求实体类
 */
public class ReportPillowTalkRequest implements RequestParameterCheck {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "report_pillow_talk";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 被举报的悄悄话或世界广播的id
     */
    public static final String PILLOW_TALK_ID = "pillow_talk_id";
    /**
     * 举报人id
     */
    public static final String REPORTER = "reporter";
    /**
     * 举报内容
     */
    public static final String CONTENT = "content";
    /**
     * 举报时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 被举报的悄悄话或世界广播的id
     */
    @Expose
    public String pillowTalkId;
    /**
     * 举报人id
     */
    @Expose
    public String reporter;
    /**
     * 举报内容
     */
    @Expose
    public String content;
    /**
     * 举报时间
     */
    @Expose
    public String createTime;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(reporter)
                && !CommonUtil.isBlank(content);
    }

    @Override
    public String toString() {
        return "ReportPillowTalk{" +
                "id='" + id + '\'' +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", reporter='" + reporter + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
