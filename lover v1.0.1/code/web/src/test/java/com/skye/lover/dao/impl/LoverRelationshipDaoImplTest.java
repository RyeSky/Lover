package com.skye.lover.dao.impl;

import com.skye.lover.loverrelatiionship.dao.impl.LoverRelationshipDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 相爱关系Dao实现单元测试
 */
public class LoverRelationshipDaoImplTest {
    private LoverRelationshipDaoImpl lrdi;

    @Before
    public void setUp() throws Exception {
        lrdi = CommonUtil.getContext().getBean(LoverRelationshipDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(lrdi.insert("1", "2") + "");
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(lrdi.delete("6", "5") + "");
    }

    @Test
    public void queryAnother() throws Exception {
        CommonUtil.log(lrdi.queryAnother("5"));
    }
}