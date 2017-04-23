package com.skye.lover.loverrelatiionship.service.impl;

import com.skye.lover.loverrelatiionship.dao.LoverRelationshipDao;
import com.skye.lover.user.dao.UserDao;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.message.model.resp.Message;
import com.skye.lover.loverrelatiionship.service.LoverRelationshipService;
import com.skye.lover.message.service.MessageService;
import com.skye.lover.util.JPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 相恋关系业务层实现
 */
@Service
public class LoverRelationshipServiceImpl implements LoverRelationshipService {
    private LoverRelationshipDao lrd;
    private UserDao ud;
    private MessageService ms;

    @Autowired
    public LoverRelationshipServiceImpl(LoverRelationshipDao lrd, UserDao ud, MessageService ms) {
        this.lrd = lrd;
        this.ud = ud;
        this.ms = ms;
    }

    /**
     * 坠入爱河
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     */
    @Override
    public void fallInLove(BaseResponse br, String one, String another) {
        if (lrd.insert(one, another)) {//建立单方面关系成功
            String anotherOfAnother = lrd.queryAnother(another);
            if (one.equals(anotherOfAnother)) {//对方同意建立
                br.result = ud.query(another);
                ms.disableClick(one, Message.BE_BROKE_UP);
                ms.disableClick(another, Message.BE_BROKE_UP);
                Message message = ms.insertCourtshipdisplayBeAgreedMessage(another, one);
                if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
            } else {//对方还是单身，对方要收到一条通知消息
                Message message = ms.insertCourtshipdisplayMessage(another, one);
                if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
            }
        } else {// 插入数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 分手
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     */
    @Override
    public void breakUp(BaseResponse br, String one, String another) {
        if (!lrd.delete(one, another)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        } else {//成功解除恋爱关系，被分手的一方会收到一条通知消息
            ms.disableClick(one, Message.COURTSHIPDISPLAY_BE_AGREED);
            ms.disableClick(another, Message.COURTSHIPDISPLAY_BE_AGREED);
            Message message = ms.insertBeBrokedUpMessage(another, one);
            if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
        }
    }
}
