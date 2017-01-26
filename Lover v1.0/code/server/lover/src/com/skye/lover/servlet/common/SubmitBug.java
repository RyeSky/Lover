package com.skye.lover.servlet.common;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Bug;
import com.skye.lover.service.BugService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>提交app bug日志</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/common/SubmitBug?parameter={"mobileBrand":"OPPO","mobileVersion":"R7007","platform":"0","platformVersion":"21","appVersion":"1.0","bugDetails":"NPE"}
 * </p>
 * 请求参数
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>mobileBrand</td>
 * <td>手机品牌</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>mobileVersion</td>
 * <td>手机型号</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>platform</td>
 * <td>手机平台【0：android;1:ios】</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>platformVersion</td>
 * <td>平台系统版本号</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>appVersion</td>
 * <td>app版本号</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>bugDetails</td>
 * <td>bug日志</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>成功时：{"code":0,"message":"请求成功"} <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
 * </p>
 */
public class SubmitBug extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Bug param = CommonUtil.parseJsonToObject(parameter, Bug.class);
            if (param != null && param.check()) {// 请求参数都不为空
                BugService bs = new BugService();
                bs.submitBug(br, param.mobileBrand, param.mobileVersion, param.platform, param.platformVersion, param.appVersion, param.bugDetails);
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
