package com.skye.lover.dao.impl;

import com.skye.lover.privatemessage.dao.impl.PrivateMessageDeleteDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 删除私信DAO实现单元测试
 */
public class PrivateMessageDeleteDaoImplTest {
    private PrivateMessageDeleteDaoImpl pmddi;

    @Before
    public void setUp() throws Exception {
        pmddi = CommonUtil.getContext().getBean(PrivateMessageDeleteDaoImpl.class);
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(pmddi.delete("1", "5") + "");
    }

    @Test
    public void deleteByChater() throws Exception {
        CommonUtil.log(pmddi.deleteByChater("5", "7") + "");
    }
}