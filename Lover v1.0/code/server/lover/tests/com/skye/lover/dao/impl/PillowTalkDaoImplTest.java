package com.skye.lover.dao.impl;

import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 悄悄话Dao实现类单元测试
 */
public class PillowTalkDaoImplTest {
    private PillowTalkDaoImpl ptdi;

    @Before
    public void setUp() throws Exception {
        ptdi = new PillowTalkDaoImpl();
    }

    @Test
    public void query() throws Exception {
        System.out.println(ptdi.query("10", "2").toString());
//        long time = System.currentTimeMillis();
//        System.out.println(pti.query("2").toString());
//        System.out.println("time:" + (System.currentTimeMillis() - time));
//        System.out.println(pti.query("3").toString());
//        System.out.println(pti.query("4").toString());
//        System.out.println(pti.query("5").toString());
//        System.out.println(pti.query("6").toString());
    }

    @Test
    public void find() throws Exception {
        System.out.println(ptdi.find("1", 1).toString());
    }

    @Test
    public void loversPillowTalk() {
        CommonUtil.log(ptdi.loversPillowTalk("1", "2", 1).toString());
    }

    @Test
    public void properties() {
        System.out.println(ptdi.properties("10"));
    }

    @Test
    public void commentedPillowTalk() {
        System.out.println(ptdi.commentedPillowTalk("2", 1));
    }

    @Test
    public void countOfCommentedPillowTalk() {
        System.out.println(ptdi.countOfCommentedPillowTalk("1"));
    }

    @Test
    public void praisedPillowTalk() {
        CommonUtil.log(ptdi.praisedPillowTalk("1", 1).toString());
    }

    @Test
    public void queryOwner() {
        CommonUtil.log(ptdi.queryOwner("11"));
    }
}