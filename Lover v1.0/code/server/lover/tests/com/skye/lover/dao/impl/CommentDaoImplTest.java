package com.skye.lover.dao.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *评论DAO实现测单元测测试
 */
public class CommentDaoImplTest {
    private CommentDaoImpl cdi;

    @Before
    public void setUp() throws Exception {
        cdi = new CommentDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        cdi.insert("1", "2", "情情话123好甜蜜");
    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void comments() throws Exception {

    }

    @Test
    public void countOfComments() throws Exception {

    }

}