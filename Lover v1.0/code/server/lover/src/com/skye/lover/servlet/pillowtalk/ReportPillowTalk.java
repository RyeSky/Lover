package com.skye.lover.servlet.pillowtalk;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.ReportPillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>举报悄悄话</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover//servlet/pillowtalk/ReportPillowTalk?parameter={"pillowTalkId":"3","reporter":"1","content":"涉及非法"}
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
 * <td>悄悄话ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>reporter</td>
 * <td>举报人id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>举报理由</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>成功时：{"code":0,"message":"请求成功"} <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
 * </p>
 */
public class ReportPillowTalk extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            com.skye.lover.model.ReportPillowTalk param = CommonUtil.parseJsonToObject(parameter, com.skye.lover.model.ReportPillowTalk.class);
            if (param != null && param.check()) {// 请求参数都不为空
                ReportPillowTalkService rpts = new ReportPillowTalkService();
                rpts.insert(br, param.pillowTalkId, param.reporter, param.content);
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
