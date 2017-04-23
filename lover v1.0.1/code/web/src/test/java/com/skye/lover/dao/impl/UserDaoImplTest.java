package com.skye.lover.dao.impl;

import com.skye.lover.user.dao.impl.UserDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 用户Dao实现单元测试
 */
@Service
public class UserDaoImplTest {
    private UserDaoImpl udi;

    @Before
    public void setUp() throws Exception {
//        udi = new UserDaoImpl();
//        ApplicationContext context = new ClassPathXmlApplicationContext("application_context.xml");
//        udi.setJdbcTemplate((NamedParameterJdbcTemplate) context.getBean("jdbcTemplate"));

        ApplicationContext context = new ClassPathXmlApplicationContext("application_context.xml");
        udi = context.getBean(UserDaoImpl.class);
    }

    @Test
    public void login() {
        CommonUtil.log(udi.login("rsmiss","e10adc3949ba59abbe56e057f20f883e").toString());
    }

    @Test
    public void isExist() throws Exception {
        CommonUtil.log(udi.isExist("rsmiss") + "");
    }
}