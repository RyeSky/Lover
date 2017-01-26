package com.skye.lover.servlet.pillowtalk.followpillowtalk;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.FollowPillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

/**
 * <h2 style='{text-align:center;}'>发表跟随悄悄话</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/pillowtalk/followpillowtalk/PublishFollowPillowTalk?parameter={"userId":"4","pillowTalkId":"10","content":"昭华易谢君难见"}
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
 * <td>pillowTalkId</td>
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>跟随悄悄话内容</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>
 * 成功时：{"code":0,"message":"请求成功"} <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
 * </p>
 */
public class PublishFollowPillowTalk extends HttpServlet {

    private class Parameter {
        @Expose
        String pillowTalkId, userId, content;// 悄悄话id，发表者，跟随悄悄话内容

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(pillowTalkId)
                    && !CommonUtil.isBlank(userId)
                    && !CommonUtil.isBlank(content);
        }

        @Override
        public String toString() {
            return "Parameter [pillowTalkId=" + pillowTalkId + ", userId="
                    + userId + ", content=" + content + "]";
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
                FollowPillowTalkService fpts = new FollowPillowTalkService();
                fpts.insert(br, param.pillowTalkId, param.userId, param.content);
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
