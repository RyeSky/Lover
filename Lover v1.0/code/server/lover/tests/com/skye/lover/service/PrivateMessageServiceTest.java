package com.skye.lover.service;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 私信业务层单元测试
 */
public class PrivateMessageServiceTest {
    private PrivateMessageService pms;

    @Before
    public void setUp() throws Exception {
        pms = new PrivateMessageService();
    }

    @Test
    public void insert() throws Exception {
        BaseResponse br = new BaseResponse();
        pms.insert(br, "1", "2", "谢谢你，陪我一路走过");
        CommonUtil.log(br.toString());
    }

    @Test
    public void deleteByPrivateMessageSession() throws Exception {
        BaseResponse br = new BaseResponse();
        pms.deleteByPrivateMessageSession(br, "1", "2");
        CommonUtil.log(br.toString());
    }

    @Test
    public void queryPrivateMessages() {
        BaseResponse br = new BaseResponse();
        pms.queryPrivateMessages(br, "1", "2", "");
        CommonUtil.log(br.toString());
    }
}