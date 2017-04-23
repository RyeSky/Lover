package com.skye.lover.dao.impl;

import com.skye.lover.user.dao.impl.FeedbackDaoImpl;
import com.skye.lover.user.model.req.FeedbackRequest;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 意见反馈Dao实现单元测试
 */
public class FeedbackDaoImplTest {
    private FeedbackDaoImpl fdi;

    @Before
    public void setUp() throws Exception {
        fdi = CommonUtil.getContext().getBean(FeedbackDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(fdi.insert("6", "没有版本升级功能", FeedbackRequest.PLATFORM_ANDROID, "18", "1.0") + "");
    }
}