package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

/**
 * 意见反馈Dao实现Test
 */
public class FeedbackDaoImplTest {
    private FeedbackDaoImpl fdi;

    @Before
    public void setUp() throws Exception {
        fdi = new FeedbackDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        fdi.insert("4", "有待改进", 0, "21", "1.0");
    }

}