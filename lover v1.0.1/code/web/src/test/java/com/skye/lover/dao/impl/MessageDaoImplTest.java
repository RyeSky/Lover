package com.skye.lover.dao.impl;

import com.skye.lover.message.dao.impl.MessageDaoImpl;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 消息Dao实现
 */
public class MessageDaoImplTest {
    private MessageDaoImpl mdi;

    @Before
    public void setUp() throws Exception {
        mdi = CommonUtil.getContext().getBean(MessageDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(mdi.insert("6", Message.TYPE_PERSONAL, Message.REPLY, "111", "222", "0", "21"));
    }

    @Test
    public void insertSystemMessage() throws Exception {
        CommonUtil.log(mdi.insertSystemMessage("333", "444") + "");
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(mdi.delete("27", "6") + "");
    }

    @Test
    public void query() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(mdi.query("26")));
    }

    @Test
    public void messages() throws Exception {
        CommonUtil.log(CommonUtil.gson.toJson(mdi.messages("6", 1)));
    }

    @Test
    public void countOfMessages() throws Exception {
        CommonUtil.log(mdi.countOfMessages("6") + "");
    }

    @Test
    public void disableClick() throws Exception {
        CommonUtil.log(mdi.disableClick("4", Message.COURTSHIPDISPLAY_BE_AGREED) + "");
    }
}