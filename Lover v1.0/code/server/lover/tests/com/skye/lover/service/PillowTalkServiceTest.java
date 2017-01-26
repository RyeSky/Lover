package com.skye.lover.service;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 悄悄话业务层单元测试
 */
public class PillowTalkServiceTest {
    private PillowTalkService ps;

    @Before
    public void setUp() throws Exception {
        ps = new PillowTalkService();
    }

    @Test
    public void others() throws Exception {
        BaseResponse br = new BaseResponse();
        ps.others(br, "3", 1);
        CommonUtil.log(br.toString());
    }
}