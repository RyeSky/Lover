package com.skye.lover.service.impl;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.user.service.UserService;
import com.skye.lover.user.service.impl.UserServiceImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 用户业务层实现单元测试
 */
public class UserServiceImplTest {
    private UserService us;

    @Before
    public void setUp() throws Exception {
        us = CommonUtil.getContext().getBean(UserServiceImpl.class);
    }

    @Test
    public void login() throws Exception {
        BaseResponse br = new BaseResponse();
        us.login(br, "rsmiss", "e10adc3949ba59abbe56e057f20f883e");
        CommonUtil.log(br.toString());
    }

}