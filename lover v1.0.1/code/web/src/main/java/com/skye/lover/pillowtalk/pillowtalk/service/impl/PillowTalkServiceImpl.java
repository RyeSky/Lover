package com.skye.lover.pillowtalk.pillowtalk.service.impl;

import com.skye.lover.loverrelatiionship.dao.LoverRelationshipDao;
import com.skye.lover.pillowtalk.pillowtalk.dao.PillowTalkDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.model.resp.ListResponse;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalkProperties;
import com.skye.lover.pillowtalk.pillowtalk.service.PillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 悄悄话业务层实现
 */
@Service
public class PillowTalkServiceImpl implements PillowTalkService {
    private PillowTalkDao ptd;
    private LoverRelationshipDao lrd;

    @Autowired
    public PillowTalkServiceImpl(PillowTalkDao ptd, LoverRelationshipDao lrd) {
        this.ptd = ptd;
        this.lrd = lrd;
    }

    /**
     * 插入悄悄话
     *
     * @param br        返回数据基类
     * @param publisher 悄悄话发表者
     * @param type      类型【0:悄悄话；1:广播】
     * @param content   悄悄内容
     * @param imgs      多张图片路径，用英文逗号分隔
     */
    @Override
    public void insert(BaseResponse br, String publisher, int type, String content, String imgs) {
        if (PillowTalk.TYPE_PILLOW_TALK == type) {//悄悄话
            String another = lrd.queryAnother(publisher);
            if (!CommonUtil.isBlank(another)) {
                if (!ptd.insert(publisher, type, another, content, imgs)) {// 插入数据出错
                    br.code = BaseResponse.CODE_FAIL;
                    br.message = BaseResponse.MESSAGE_PUBLISH_FAIL;
                }
            } else {// 单身不能发表悄悄话
                br.code = BaseResponse.CODE_FAIL;
                br.message = "单身贵族不能发表悄悄话";
            }
        } else if (PillowTalk.TYPE_BROADCAST == type) {//广播
            if (!ptd.insert(publisher, type, "0", content, imgs)) {// 插入数据出错
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_PUBLISH_FAIL;
            }
        } else {// 类型错误
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_PUBLISH_FAIL;
        }
    }

    /**
     * 删除悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param publisher    悄悄话发表者
     */
    @Override
    public void delete(BaseResponse br, String pillowTalkId, String publisher) {
        if (!ptd.delete(pillowTalkId, publisher)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_DELETE_FAIL;
        }
    }

    /**
     * 公开悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     */
    @Override
    public void open(BaseResponse br, String pillowTalkId, String userId) {
        int identity = ptd.judgeIdentity(pillowTalkId, userId);
        if (identity != 0) {
            if (!ptd.open(pillowTalkId, identity)) {// 更新数据出错
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 没有找到对应角色
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 发现
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    @Override
    public void find(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.find(userId, page);
        if (list != null) {
            ListResponse lr;
            lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfFind();
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 蜜语
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     * @param page    请求第几页数据
     */
    @Override
    public void honeyWord(BaseResponse br, String one, String another, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.loversPillowTalk(one, another, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfLoversPillowTalk(one, another);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }


    /**
     * 根据id查询悄悄话详情
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param userId       用户id
     */
    @Override
    public void query(BaseResponse br, String pillowTalkId, String userId) {
        PillowTalk pt = ptd.query(pillowTalkId, userId);
        if (pt != null) br.result = pt;
        else {// 查询数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 根据id查询悄悄话部分属性
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     */
    @Override
    public void properties(BaseResponse br, String pillowTalkId) {
        PillowTalkProperties properties = ptd.properties(pillowTalkId);
        if (properties != null) br.result = properties;
        else {// 查询数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 收藏的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    @Override
    public void collected(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.collectedPillowTalk(userId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfCollectedPillowTalk(userId);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 赞过的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    @Override
    public void praised(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.praisedPillowTalk(userId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfPraisedPillowTalk(userId);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 评论过的的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    @Override
    public void commented(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.commentedPillowTalk(userId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfCommentedPillowTalk(userId);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 发表过的的悄悄话
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param page   请求第几页数据
     */
    @Override
    public void others(BaseResponse br, String userId, int page) {
        if (page <= 0)
            page = 1;
        Object list = ptd.othersPillowTalk(userId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = ptd.countOfOthersPillowTalk(userId);
            lr.pageCount = count / ConstantUtil.PAGE_SIZE;
            if (count % ConstantUtil.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
