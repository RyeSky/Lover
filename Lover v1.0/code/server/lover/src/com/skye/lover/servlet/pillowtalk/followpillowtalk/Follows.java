package com.skye.lover.servlet.pillowtalk.followpillowtalk;


import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.FollowPillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>跟随悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/pillowtalk/followpillowtalk/Follows?parameter={"pillowTalkId":"3","page":"1"}
 * </p>
 * 请求参数
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>pillowTalkId</td>
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>page</td>
 * <td>当前请求的下一页，比如请求时page=1，则返回page=2</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>
 * 成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"3","pillowTalkId":"3","publisher":"2",
 * "content":"有啊有啊","createTime":"1481699356000"},{"id":"1","pillowTalkId":"3","publisher":"2","content":"我也喜欢你","createTime":"1481699274000"}]},
 * "message":"请求成功"}  <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
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
 * <td>跟随悄悄话ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>pillowTalkId</td>
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>publisher</td>
 * <td>跟随悄悄话发布者ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>悄悄话内容</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>createTime</td>
 * <td>悄悄话发布时间时间截</td>
 * <td>String</td>
 * </tr>
 * </table>
 */
public class Follows extends HttpServlet {

    private class Parameter {
        @Expose
        String pillowTalkId;// 用户id，悄悄话id
        @Expose
        int page;// 请求页数

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(pillowTalkId) && page > 0;
        }

        @Override
        public String toString() {
            return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + ", page=" + page + '}';
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
                fpts.follows(br, param.pillowTalkId, param.page);
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
