package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

/**
 * BugDao单元测试
 */
public class BugDaoImplTest {
    private BugDaoImpl bdi;

    @Before
    public void setUp() throws Exception {
        bdi = new BugDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        bdi.insert("oppo", "R7007", 0, "18", "1.0", "NullPri");
    }

}