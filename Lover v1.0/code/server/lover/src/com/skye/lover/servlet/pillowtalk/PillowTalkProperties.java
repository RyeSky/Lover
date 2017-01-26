package com.skye.lover.servlet.pillowtalk;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.PillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>悄悄话部分属性</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：
 * http://localhost:8080/Lover/servlet/pillowtalk/PillowTalkProperties?parameter={"pillowTalkId":"10"}
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
 * <td>page</td>
 * <td>请求页数</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>成功时：{"code":0,"result":{"praiseCount":0,"commentCount":11,"publisherOpen":1,"anotherOpen":0},"message":"请求成功"}
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
 * <td>publisherOpen</td>
 * <td>悄悄话发布者是否愿意公开</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>anotherOpen</td>
 * <td>另一方是否愿意公开</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>praiseCount</td>
 * <td>悄悄话收到的赞数量</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>commentCount</td>
 * <td>悄悄话收到的评论数量</td>
 * <td>int</td>
 * </tr>
 * </table>
 */
public class PillowTalkProperties extends HttpServlet {

    private class Parameter {
        @Expose
        String pillowTalkId;// 悄悄话id

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(pillowTalkId);
        }

        @Override
        public String toString() {
            return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + '}';
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
                PillowTalkService pts = new PillowTalkService();
                pts.properties(br, param.pillowTalkId);
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

