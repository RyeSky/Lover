package com.skye.lover.dao.impl;

import com.skye.lover.common.dao.impl.BugDaoImpl;
import com.skye.lover.common.model.req.SubmitBugRequest;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * bugDao实现单元测试
 */
public class BugDaoImplTest {
    private BugDaoImpl bdi;

    @Before
    public void setUp() throws Exception {
        bdi = CommonUtil.getContext().getBean(BugDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(bdi.insert("OPPO", "R7007", SubmitBugRequest.PLATFORM_ANDROID, "18", "1.0.1", "125877") + "");
    }
}