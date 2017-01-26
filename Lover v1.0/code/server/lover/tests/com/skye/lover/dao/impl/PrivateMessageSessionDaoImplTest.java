package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 私信会话DAO实现单元测试
 */
public class PrivateMessageSessionDaoImplTest {

    private PrivateMessageSessionDaoImpl pmsdi;

    @Before
    public void setUp() throws Exception {
        pmsdi = new PrivateMessageSessionDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        pmsdi.insert("1", "2", "5");
        pmsdi.insert("2", "1", "5");
    }

    @Test
    public void query() throws Exception {
        System.out.println(pmsdi.query("2"));
    }
}