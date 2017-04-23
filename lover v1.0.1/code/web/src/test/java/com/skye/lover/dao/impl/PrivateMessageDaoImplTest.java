package com.skye.lover.dao.impl;

import com.skye.lover.privatemessage.dao.impl.PrivateMessageDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 私信Dao实现单元测试
 */
public class PrivateMessageDaoImplTest {
    private PrivateMessageDaoImpl pmdi;

    @Before
    public void setUp() throws Exception {
        pmdi = CommonUtil.getContext().getBean(PrivateMessageDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(pmdi.insert("5", "7", "555"));
    }

    @Test
    public void query() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(pmdi.query("5", "7", 0)));
    }

    @Test
    public void queryById() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(pmdi.query("111")));
    }

    @Test
    public void countOfPrivateMessage() throws Exception {
        CommonUtil.log(pmdi.countOfPrivateMessage("7", "5") + "");
    }

    @Test
    public void countOfBefore() throws Exception {
        CommonUtil.log(pmdi.countOfBefore("7", "5", "109") + "");
    }

    @Test
    public void queryFirstPrivateMessageId() throws Exception {
        CommonUtil.log(pmdi.queryFirstPrivateMessageId("7", "5"));
    }

    @Test
    public void queryLastPrivateMessageId() throws Exception {
        CommonUtil.log(pmdi.queryLastPrivateMessageId("7", "5"));
    }
}