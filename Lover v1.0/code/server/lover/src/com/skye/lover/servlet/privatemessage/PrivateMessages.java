package com.skye.lover.servlet.privatemessage;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.PrivateMessageService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>私信聊天记录</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/privatemessage/PrivateMessages?parameter={"userId":"1","another":"2","privateMessageId":"37"}
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
 */
public class PrivateMessages extends HttpServlet {

    private class Parameter {
        @Expose
        String userId, another, privateMessageId;// 用户id，聊天中的另一个id,指定的私信记录id

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(userId) && !CommonUtil.isBlank(another);
        }

        @Override
        public String toString() {
            return "Parameter{" + "userId='" + userId + '\'' + ", another='" + another + '\'' + ", privateMessageId=" + privateMessageId + '}';
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Parameter param = CommonUtil.parseJsonToObject(parameter, Parameter.class);
            if (param != null && param.check()) {// 请求参数不为空
                PrivateMessageService pms = new PrivateMessageService();
                pms.queryPrivateMessages(br, param.userId, param.another, param.privateMessageId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(br.toString());
        out.flush();
        out.close();
        CommonUtil.log(getClass().getSimpleName() + "\n" + parameter + "\n" + br.toString());
    }
}

