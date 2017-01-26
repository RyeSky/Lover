package com.skye.lover.dao.impl;

import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 私信DAO实现单元测试
 */
public class PrivateMessageDaoImplTest {
    private PrivateMessageDaoImpl pmdi;

    @Before
    public void setUp() throws Exception {
        pmdi = new PrivateMessageDaoImpl();
    }

    @Test
    public void insert() throws Exception {
        System.out.println(pmdi.insert("1", "2", "您好"));
    }

    @Test
    public void query() {
        System.out.println(pmdi.query("2", "1", 1));
    }

    @Test
    public void queryLastPrivateMessageId() {
        System.out.println(pmdi.queryLastPrivateMessageId("2", "1"));
    }

    @Test
    public void countOfPrivateMessage() {
        System.out.println(pmdi.countOfPrivateMessage("2", "1"));
    }

    @Test
    public void countOfBefore() {
        long temp = System.currentTimeMillis();
        CommonUtil.log(pmdi.countOfBefore("1", "2", "44") + "");
        CommonUtil.log(System.currentTimeMillis() - temp + "");
    }
}