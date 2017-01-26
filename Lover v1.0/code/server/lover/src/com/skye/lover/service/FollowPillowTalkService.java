package com.skye.lover.service;

import com.skye.lover.dao.impl.FollowPillowTalkDaoImpl;
import com.skye.lover.dao.impl.LoverRelationshipDaoImpl;
import com.skye.lover.dao.interf.FollowPillowTalkDao;
import com.skye.lover.dao.interf.LoverRelationshipDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.Message;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;
import com.skye.lover.util.JPushUtil;

import java.util.List;

/**
 * 跟随悄悄话业务层业务层
 */
public class FollowPillowTalkService {
    private FollowPillowTalkDao fptd = new FollowPillowTalkDaoImpl();
    private LoverRelationshipDao lrd = new LoverRelationshipDaoImpl();
    private MessageService ms = new MessageService();

    /**
     * 插入跟随悄悄话
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param publisher    跟随悄悄话发布者
     * @param content      跟随悄悄话内容
     */
    public void insert(BaseResponse br, String pillowTalkId, String publisher, String content) {
        if (!fptd.insert(pillowTalkId, publisher, content)) {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
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
    public void delete(BaseResponse br, String follwoPillowTalkId, String publisher) {
        if (!fptd.delete(follwoPillowTalkId, publisher)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }


    /**
     * 跟随悄悄话列表
     *
     * @param br           返回数据基类
     * @param pillowTalkId 悄悄话id
     * @param page         请求第几页数据
     */
    public void follows(BaseResponse br, String pillowTalkId, int page) {
        if (page <= 0)
            page = 1;
        Object list = fptd.follows(pillowTalkId, page);
        if (list != null) {
            ListResponse lr = new ListResponse();
            lr.page = page + 1;
            // 计算总页数
            int count = fptd.countOfFollows(pillowTalkId);
            lr.pageCount = count / Const.PAGE_SIZE;
            if (count % Const.PAGE_SIZE != 0)
                lr.pageCount++;
            lr.list = list;
            br.result = lr;
        } else {// 查询数据时发生异常
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }
}
