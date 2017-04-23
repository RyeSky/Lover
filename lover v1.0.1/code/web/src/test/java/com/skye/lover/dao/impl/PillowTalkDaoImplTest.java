package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.pillowtalk.dao.impl.PillowTalkDaoImpl;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 悄悄话DAO实现单元测试
 */
public class PillowTalkDaoImplTest {
    private PillowTalkDaoImpl ptdi;

    @Before
    public void setUp() throws Exception {
        ptdi = CommonUtil.getContext().getBean(PillowTalkDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(ptdi.insert("6", PillowTalk.TYPE_BROADCAST, "0", "111", "http://imgxc1.kwcdn.kuwo.cn/star/koowoLive/0/0/1464059789071_0.jpg") + "");
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(ptdi.delete("23", "6") + "");
    }

    @Test
    public void judgeIdentity() throws Exception {
        CommonUtil.log(ptdi.judgeIdentity("22", "5") + "");
    }

    @Test
    public void open() throws Exception {
        CommonUtil.log(ptdi.open("22", 1) + "");
        CommonUtil.log(ptdi.open("22", 2) + "");
    }

    @Test
    public void find() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.find("6", 1)));
    }

    @Test
    public void countOfFind() throws Exception {
        CommonUtil.log(ptdi.countOfFind() + "");
    }

    @Test
    public void loversPillowTalk() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.loversPillowTalk("5", "6", 1)));
    }

    @Test
    public void countOfLoversPillowTalk() throws Exception {
        CommonUtil.log(ptdi.countOfLoversPillowTalk("5", "6") + "");
    }

    @Test
    public void query() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.query("22", "6")));
    }

    @Test
    public void queryOwner() throws Exception {
        CommonUtil.log(ptdi.queryOwner("22"));
    }

    @Test
    public void properties() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.properties("22")));
    }

    @Test
    public void collectedPillowTalk() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.collectedPillowTalk("2", 1)));
    }

    @Test
    public void countOfCollectedPillowTalk() throws Exception {
        CommonUtil.log(ptdi.countOfCollectedPillowTalk("2") + "");
    }

    @Test
    public void praisedPillowTalk() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.praisedPillowTalk("5", 1)));
    }

    @Test
    public void countOfPraisedPillowTalk() throws Exception {
        CommonUtil.log(ptdi.countOfPraisedPillowTalk("5") + "");
    }

    @Test
    public void commentedPillowTalk() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.commentedPillowTalk("5", 1)));
    }

    @Test
    public void countOfCommentedPillowTalk() throws Exception {
        CommonUtil.log(ptdi.countOfCommentedPillowTalk("5") + "");
    }

    @Test
    public void othersPillowTalk() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(ptdi.othersPillowTalk("5", 1)));
    }

    @Test
    public void countOfOthersPillowTalk() throws Exception {
        CommonUtil.log(ptdi.countOfOthersPillowTalk("5") + "");
    }
}