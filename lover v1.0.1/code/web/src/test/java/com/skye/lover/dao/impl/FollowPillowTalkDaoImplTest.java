package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.followpillowtalk.dao.impl.FollowPillowTalkDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 跟随悄悄话DAO实现单元测试
 */
public class FollowPillowTalkDaoImplTest {
    private FollowPillowTalkDaoImpl fptdi;

    @Before
    public void setUp() throws Exception {
        fptdi = CommonUtil.getContext().getBean(FollowPillowTalkDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(fptdi.insert("21", "6", "111") + "");
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(fptdi.delete("39", "6") + "");
    }

    @Test
    public void follows() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(fptdi.follows("21", 1)));
    }

    @Test
    public void countOfFollows() throws Exception {
        CommonUtil.log(fptdi.countOfFollows("21") + "");
    }
}