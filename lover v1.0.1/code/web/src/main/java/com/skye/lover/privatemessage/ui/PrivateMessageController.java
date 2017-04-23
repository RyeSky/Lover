package com.skye.lover.privatemessage.ui;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.privatemessage.model.req.*;
import com.skye.lover.privatemessage.service.PrivateMessageService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 私信控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/privatemessage/")
public class PrivateMessageController {
    private static final Logger log = LoggerFactory.getLogger(PrivateMessageController.class);
    /**
     * 私信业务层
     */
    private PrivateMessageService pms;

    @Autowired
    public PrivateMessageController(PrivateMessageService pms) {
        this.pms = pms;
    }

    /**
     * <h2 style='{text-align:center;}'>发送私信消息</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/privatemessage/PublishPrivateMessage?parameter={"userId":"4","another":"2","content":"我好想你"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>another</td>
     * <td>聊天中的另一方id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>content</td>
     * <td>聊天内容</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"message":"请求成功"} <br>
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "PublishPrivateMessage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object publishPrivateMessage(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PublishPrivateMessageRequest param = CommonUtil.parseJsonToObject(parameter, PublishPrivateMessageRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pms.insert(br, param.userId, param.another, param.content);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("publishPrivateMessage\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>私信会话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/privatemessage/PrivateMessageSessions?parameter={"userId":"2"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":[{"id":"9","one":"2","another":"1","anotherNickname":"花开一半","anotherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","lastPrivateMessageId":"5","lastPrivateMessageContent":"您好","lastPrivateMessageCreateTime":"1483865900000","anotherGender":2}],"message":"请求成功"}
     * 或者{"code":0,"result":[],"message":"请求成功"}
     * <br> 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     * 返回参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
     * rules="all" summary='返回参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>当前请求的下一页，比如请求时page=1，则返回page=2</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>pageCount</td>
     * <td>记录总页数</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>list</td>
     * <td>数据集合</td>
     * <td>array</td>
     * </tr>
     * <tr>
     * <td>id</td>
     * <td>私信会话记录ID</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>one</td>
     * <td>聊天中的一方</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>another</td>
     * <td>私信会话的对方</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherNickname</td>
     * <td>私信会话的对方昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherAvatar</td>
     * <td>私信会话的对方头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherGender</td>
     * <td>私信会话的对方性别</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>lastPrivateMessageId</td>
     * <td>最后一条消息id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>lastPrivateMessageContent</td>
     * <td>最后一条消息内容</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>lastPrivateMessageCreateTime</td>
     * <td>最后一条消息创建时间</td>
     * <td>String</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "PrivateMessageSessions", method = {RequestMethod.POST, RequestMethod.GET})
    public Object privateMessageSessions(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PrivateMessageSessionsRequest param = CommonUtil.parseJsonToObject(parameter, PrivateMessageSessionsRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pms.queryPrivateMessageSessions(br, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("privateMessageSessions\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>私信聊天记录</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/privatemessage/PrivateMessages?parameter={"userId":"1","another":"2","privateMessageId":"37"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>another</td>
     * <td>聊天中的另一个id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>privateMessageId</td>
     * <td>指定的私信记录id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"1","sender":"1","senderNickname":"花开一半",
     * "senderAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","receiver":"2",
     * "receiverNickname":"想念","receiverAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg",
     * "content":"hello","createTime":"1483417125000","senderGender":2,"receiverGender":0},{"id":"2","sender":"1","senderNickname":"花开一半","senderAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","receiver":"2","receiverNickname":"想念","receiverAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"你好","createTime":"1483417151000","senderGender":2,"receiverGender":0},{"id":"3","sender":"2","senderNickname":"想念","senderAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","receiver":"1","receiverNickname":"花开一半","receiverAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"你好，刚刚有事，没看消息","createTime":"1483417185000","senderGender":0,"receiverGender":2},{"id":"5","sender":"1","senderNickname":"花开一半","senderAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","receiver":"2","receiverNickname":"想念","receiverAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"您好","createTime":"1483865900000","senderGender":2,"receiverGender":0}]},"message":"请求成功"}
     * <br> 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     * 返回参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
     * rules="all" summary='返回参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>firstPrivateMessageId</td>
     * <td>聊天记录的第一条记录id，如果手机显示的第一条id和此字段一致，则没有更多记录可以加载了</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>list</td>
     * <td>数据集合</td>
     * <td>array</td>
     * </tr>
     * <tr>
     * <td>id</td>
     * <td>私信记录id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>sender</td>
     * <td>私信发送者id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>senderNickname</td>
     * <td>私信发送者昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>senderAvatar</td>
     * <td>私信发送者头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>senderGender</td>
     * <td>私信发送者性别【0：保密；1：男；2：女】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>receiver</td>
     * <td>私信接收者id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>receiverNickname</td>
     * <td>私信接收者昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>receiverAvatar</td>
     * <td>私信接收者头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>receiverGender</td>
     * <td>私信接收者性别【0：保密；1：男；2：女】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>content</td>
     * <td>私信聊天内容</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>createTime</td>
     * <td>私信发布时间时间截</td>
     * <td>String</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "PrivateMessages", method = {RequestMethod.POST, RequestMethod.GET})
    public Object privateMessages(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PrivateMessagesRequest param = CommonUtil.parseJsonToObject(parameter, PrivateMessagesRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pms.queryPrivateMessages(br, param.userId, param.another, param.privateMessageId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("privateMessages\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>根据聊天的两个用户删除整个会话记录</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/privatemessage/DeleteByPrivateMessageSession?parameter={"userId":"4","another":"2"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>another</td>
     * <td>聊天中的另一个id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"message":"请求成功"} <br>
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "DeleteByPrivateMessageSession", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deleteByPrivateMessageSession(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeleteByPrivateMessageSessionRequest param = CommonUtil.parseJsonToObject(parameter, DeleteByPrivateMessageSessionRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pms.deleteByPrivateMessageSession(br, param.userId, param.another);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("deleteByPrivateMessageSession\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>根据私信记录id和聊天者id删除私信</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/privatemessage/DeleteByPrivateMessageId?parameter={"userId":"4","privateMessageId":"2"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>privateMessageId</td>
     * <td>私信记录id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"message":"请求成功"} <br>
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "DeleteByPrivateMessageId", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deleteByPrivateMessageId(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeleteByPrivateMessageIdRequest param = CommonUtil.parseJsonToObject(parameter, DeleteByPrivateMessageIdRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pms.deleteByPrivateMessageId(br, param.privateMessageId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("deleteByPrivateMessageId\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
