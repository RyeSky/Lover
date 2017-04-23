package com.skye.lover.dao.impl;

import com.skye.lover.pillowtalk.comment.dao.impl.CommentDaoImpl;
import com.skye.lover.util.CommonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 评论Dao实现单元测试
 */
public class CommentDaoImplTest {
    private CommentDaoImpl cdi;

    @Before
    public void setUp() throws Exception {
        cdi = CommonUtil.getContext().getBean(CommentDaoImpl.class);
    }

    @Test
    public void insert() throws Exception {
        CommonUtil.log(cdi.insert("21", "6", "11") + "");
    }

    @Test
    public void delete() throws Exception {
        CommonUtil.log(cdi.delete("46", "6") + "");
    }

    @Test
    public void comments() throws Exception {
        CommonUtil.log(cdi.comments("26", 1).toString());
    }

    @Test
    public void countOfComments() throws Exception {
        CommonUtil.log(cdi.countOfComments("21") + "");
    }
}