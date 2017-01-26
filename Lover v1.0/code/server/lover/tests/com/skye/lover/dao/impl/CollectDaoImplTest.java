package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 收藏DAO实现单元测试
 */
public class CollectDaoImplTest {
    private CollectDaoImpl cdi;

    @Before
    public void setUp() throws Exception {
        cdi = new CollectDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        String collectId = cdi.insert("10", "2");
        System.out.println("collectId=" + collectId);
    }

    @Test
    public void delete() throws Exception {
        System.out.println(cdi.delete("5", "2"));
    }

}