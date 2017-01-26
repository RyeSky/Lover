package com.skye.lover.service;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 消息业务层单元测试
 */
public class MessageServiceTest {
    private MessageService ms;

    @Before
    public void setUp() throws Exception {
        ms = new MessageService();
    }

    @Test
    public void messages() throws Exception {
        BaseResponse br = new BaseResponse();
        ms.messages(br, "1", 1);
        CommonUtil.log(br.toString());
    }

    @Test
    public void insertSystemMessage(){
        ms.insertSystemMessage("1","222222");
    }
}