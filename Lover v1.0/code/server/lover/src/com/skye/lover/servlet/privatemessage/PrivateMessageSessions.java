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
 * <h2 style='{text-align:center;}'>私信会话列表</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/privatemessage/PrivateMessageSessions?parameter={"userId":"2"}
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
 */
public class PrivateMessageSessions extends HttpServlet {

    private class Parameter {
        @Expose
        String userId;// 用户id

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(userId);
        }

        @Override
        public String toString() {
            return "Parameter{" + "userId='" + userId + '\'' + '}';
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Parameter param = CommonUtil.parseJsonToObject(parameter, Parameter.class);
            if (param != null && param.check()) {// 请求参数不为空
                PrivateMessageService pms = new PrivateMessageService();
                pms.queryPrivateMessageSessions(br, param.userId);
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
