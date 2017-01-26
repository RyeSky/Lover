package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ReportPillowTalkDao实现单元测试
 */
public class ReportPillowTalkDaoImplTest {
    private ReportPillowTalkDaoImpl rptdi;

    @Before
    public void setUp() throws Exception {
        rptdi = new ReportPillowTalkDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        rptdi.insert("2", "1", "涉及色情");
    }

}