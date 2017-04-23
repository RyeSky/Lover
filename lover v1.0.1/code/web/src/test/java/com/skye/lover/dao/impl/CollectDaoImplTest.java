package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.collect.dao.impl.CollectDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 收藏悄悄话Dao实现单元测试
 */
public class CollectDaoImplTest {
    private CollectDaoImpl cdi;

    @Before
    public void setUp() throws Exception {
        cdi = CommonUtil.getContext().getBean(CollectDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(cdi.insert("11", "2"));
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(cdi.delete("11", "2") + "");
    }
}