package com.skye.lover.pillowtalk.followpillowtalk.service.impl;

import com.skye.lover.pillowtalk.followpillowtalk.dao.FollowPillowTalkDao;
import com.skye.lover.loverrelatiionship.dao.LoverRelationshipDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.model.resp.ListResponse;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.pillowtalk.followpillowtalk.service.FollowPillowTalkService;
import com.skye.lover.message.service.MessageService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import com.skye.lover.util.JPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 跟随悄悄话业务层业务层实现
 */
@Service
public class FollowPillowTalkServiceImpl implements FollowPillowTalkService {
    private FollowPillowTalkDao fptd;
    private LoverRelationshipDao lrd;
    private MessageService ms;

    @Autowired
    public FollowPillowTalkServiceImpl(FollowPillowTalkDao fptd, LoverRelationshipDao lrd, MessageService ms) {
        this.fptd = fptd;
        this.lrd = lrd;
        this.ms = ms;
    }

    /**
     * 插入跟随悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param publisher    跟随悄悄话发布者
     * @param content      跟随悄悄话内容
     */
    @Override
    public void insert(BaseResponse br, String pillowTalkId, String publisher, String content) {
        if (!fptd.insert(pillowTalkId, publisher, content)) {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_REPLY_FAIL;
        } else {//回复成功，恋爱关系中的对方要收到一条通知消息
            String another = lrd.queryAnother(publisher);
            if (!CommonUtil.isBlank(another)) {
                Message message = ms.insertReplyMessage(another, publisher, pillowTalkId);
                if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
            }
        }
    }

    /**
     * 删除跟随悄悄话
     *
     * @param br                 返回数据基类
     * @param follwoPillowTalkId 跟随悄悄话id
     * @param publisher          跟随悄悄话发布者
     */
    @Override
    public void delete(BaseResponse br, String follwoPillowTalkId, String publisher) {
        if (!fptd.delete(follwoPillowTalkId, publisher)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_DELETE_FAIL;
        }
    }


    /**
     * 跟随悄悄话列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    @Override
    public void follows(BaseResponse br, String pillowTalkId, int page) {
        if (page <= 0)
            page = 1;
        Object list = fptd.follows(pillowTalkId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = fptd.countOfFollows(pillowTalkId);
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
