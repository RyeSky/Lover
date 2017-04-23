package com.skye.lover.dao.impl;

import com.skye.lover.privatemessage.dao.impl.PrivateMessageSessionDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 私信会话DAO实现单元测试
 */
public class PrivateMessageSessionDaoImplTest {
    private PrivateMessageSessionDaoImpl pmsdi;

    @Before
    public void setUp() throws Exception {
        pmsdi = CommonUtil.getContext().getBean(PrivateMessageSessionDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(pmsdi.insert("5", "6", "1"));
    }

    @Test
    public void query() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(pmsdi.query("5")));
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(pmsdi.delete("5", "6") + "");
    }
}