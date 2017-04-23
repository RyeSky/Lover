package com.skye.lover.message.ui;

import com.skye.lover.message.model.req.DeleteMessageRequest;
import com.skye.lover.message.model.req.MessagesRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.message.service.MessageService;
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
 * 消息控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/message/")
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    /**
     * 消息业务层
     */
    private MessageService ms;

    @Autowired
    public MessageController(MessageService ms) {
        this.ms = ms;
    }

    /**
     * <h2 style='{text-align:center;}'>消息列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/message/Messages?parameter={"userId":"1","page":"1"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
     * rules="all" summary='请求参数'>
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
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"3","title":"评论","content":"有人评论了你发表的悄悄话","pillowTalkId":"3","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","createTime":"1484895930000","type":1,"subType":2,"anotherGender":0,"pillowTalkType":0},{"id":"2","title":"回复","content":"对方回复了你","pillowTalkId":"3","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","createTime":"1484895814000","type":1,"subType":1,"anotherGender":0,"pillowTalkType":0},{"id":"1","title":"示爱","content":"有人向你示爱，赶快去看看是不是TA","anotherId":"1","anotherNickname":"花开一半","anotherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","createTime":"1484893565000","type":1,"subType":0,"anotherGender":2,"pillowTalkType":0}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     * <td>消息记录id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>type</td>
     * <td>消息类型【0：系统消息；1：个人消息】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>subType</td>
     * <td>消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>title</td>
     * <td>消息标题</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>content</td>
     * <td>消息内容。如果是私信自定义消息，则为聊天内容</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherId</td>
     * <td>当type=1,subType=0,1,2时，为操作主动方的id。例如有人向我示爱，anotherId就是那个人的id，anotherNickname、anotherAvatar、anotherGender类似</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherNickname</td>
     * <td>昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherAvatar</td>
     * <td>头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherGender</td>
     * <td>性别【0：保密；1：男；2：女】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>pillowTalkId</td>
     * <td>当type=1,subType=0,1,2时，pillowTalkId被回复或评论的悄悄话id，pillowTalkType、imgs类似</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>pillowTalkType</td>
     * <td>悄悄话的类型</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>imgs</td>
     * <td>悄悄话里的图片url</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>clickAble</td>
     * <td>消息是否可以点击【0：不可点击;1可点击】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>createTime</td>
     * <td>消息创建时间时间截</td>
     * <td>String</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Messages", method = {RequestMethod.POST, RequestMethod.GET})
    public Object messages(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            MessagesRequest param = CommonUtil.parseJsonToObject(parameter, MessagesRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                ms.messages(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("messages\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>刪除消息</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/message/DeleteMessage?parameter={"userId":"4","messageId":"1"}
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
     * <td>消息所属用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>messageId</td>
     * <td>消息记录id</td>
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
    @RequestMapping(value = "DeleteMessage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deleteMessage(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeleteMessageRequest param = CommonUtil.parseJsonToObject(parameter, DeleteMessageRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                ms.delete(br, param.messageId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("deleteMessage\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
