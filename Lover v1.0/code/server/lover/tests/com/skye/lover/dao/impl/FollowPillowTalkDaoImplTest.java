package com.skye.lover.dao.impl;

import com.skye.lover.model.FollowPillowTalk;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 跟随悄悄话DAO实现单元测试
 */
public class FollowPillowTalkDaoImplTest {
    private FollowPillowTalkDaoImpl fptdi;

    @Before
    public void setUp() throws Exception {
        fptdi = new FollowPillowTalkDaoImpl();
    }

    @Test
    public void follows() throws Exception {
        List<FollowPillowTalk> list = fptdi.follows("3", 1);
        System.out.println("lover:" + list.toString());
    }

}