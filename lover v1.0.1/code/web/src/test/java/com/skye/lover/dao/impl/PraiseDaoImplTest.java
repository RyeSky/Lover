package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.praise.dao.impl.PraiseDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 赞DAO实现单元测试
 */
public class PraiseDaoImplTest {
    private PraiseDaoImpl pdi;

    @Before
    public void setUp() throws Exception {
        pdi = CommonUtil.getContext().getBean(PraiseDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(pdi.insert("21", "5"));
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(pdi.delete("21", "5") + "");
    }
}