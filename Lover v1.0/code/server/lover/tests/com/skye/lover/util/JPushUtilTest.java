package com.skye.lover.util;

import com.skye.lover.dao.impl.MessageDaoImpl;
import com.skye.lover.dao.interf.MessageDao;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * 极光推送工具类单元测试
 */
public class JPushUtilTest {
    private MessageDao md;


    @Before
    public void setUp() throws Exception {
        md = new MessageDaoImpl();
    }


    @Test
    public void sendPushMessage() throws Exception {
        JPushUtil.sendPushMessage(md.query("5"));
    }
}