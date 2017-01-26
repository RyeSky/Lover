package com.skye.lover.service;

import com.skye.lover.dao.impl.LoverRelationshipDaoImpl;
import com.skye.lover.dao.impl.UserDaoImpl;
import com.skye.lover.dao.interf.LoverRelationshipDao;
import com.skye.lover.dao.interf.UserDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.User;
import com.skye.lover.util.JPushUtil;

/**
 * 相恋关系业务层
 */
public class LoverRelationshipService {
    private LoverRelationshipDao lrd = new LoverRelationshipDaoImpl();
    private UserDao ud = new UserDaoImpl();
    private MessageService ms = new MessageService();

    /**
     * 坠入爱河
     *
     * @param br      返回数据基类
     * @param one     一方
     * @param another 相爱的另一方
     */
    public void fallInLove(BaseResponse br, String one, String another) {
        if (lrd.insert(one, another)) {//建立单方面关系成功
            String anotherOfAnother = lrd.queryAnother(another);
            if (one.equals(anotherOfAnother)) {//对方同意建立
                User ano = ud.query(another);
                br.result = ano;
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
    public void breakUp(BaseResponse br, String one, String another) {
        if (!lrd.delete(one, another)) {// 删除数据出错
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        } else {//成功解除恋爱关系，被分手的一方会收到一条通知消息
            Message message = ms.insertBrokedUpMessage(another, one);
            if (message != null) JPushUtil.sendPushMessageByAliasByAlias(message);
        }
    }
}
