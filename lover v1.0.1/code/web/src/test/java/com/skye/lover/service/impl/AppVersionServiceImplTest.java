package com.skye.lover.service.impl;

import com.skye.lover.common.service.impl.AppVersionServiceImpl;
import com.skye.lover.common.model.resp.AppVersion;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.service.AppVersionService;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * app版本业务层实现单元测试
 */
public class AppVersionServiceImplTest {
    private AppVersionService avs;

    @Before
    public void setUp() throws Exception {
        avs = CommonUtil.getContext().getBean(AppVersionServiceImpl.class);
    }

    @Test
    public void insert() throws Exception {
        BaseResponse br = new BaseResponse();
        avs.insert(br, "1.0", AppVersion.PLATFORM_ANDROID, 0, "http://www.baidu.com", "有新版本可更新", "修复了一些bug");
        CommonUtil.log(br.toString());
    }

    @Test
    public void query() throws Exception {
        BaseResponse br = new BaseResponse();
        avs.query(br, AppVersion.PLATFORM_IOS);
        CommonUtil.log(br.toString());
    }
}