package com.skye.lover.service;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 用户业务层单元测试
 */
public class UserServiceTest {
    private UserService us;

    @Before
    public void setUp() throws Exception {
        us = new UserService();
    }

    @Test
    public void otherInfo() throws Exception {
        BaseResponse br = new BaseResponse();
        us.otherInfo(br, "2");
        CommonUtil.log(br.toString());
    }

}