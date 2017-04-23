package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.report.dao.impl.ReportPillowTalkDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 举报悄悄话Dao实现单元测试
 */
public class ReportPillowTalkDaoImplTest {
    private ReportPillowTalkDaoImpl rptdi;

    @Before
    public void setUp() throws Exception {
        rptdi = CommonUtil.getContext().getBean(ReportPillowTalkDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(rptdi.insert("21", "5", "1111") + "");
    }
}