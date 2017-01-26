package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 删除私信DAO实现单元测试
 */
public class PrivateMessageDeleteDaoImplTest {
    private PrivateMessageDeleteDaoImpl pmddi;

    @Before
    public void setUp() throws Exception {
        pmddi = new PrivateMessageDeleteDaoImpl();
    }

    @Test
    public void delete() throws Exception {
        pmddi.delete("1", "1");
    }

    @Test
    public void deleteByChater() {
        pmddi.deleteByChater("1", "2");
    }

}