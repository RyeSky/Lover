package com.skye.lover.servlet.user;

import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Feedback;
import com.skye.lover.service.FeedbackService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>提交意见反馈</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/user/SubmitFeedback?parameter={"publisher":"4","content":"you dai gai jin","platform":"0","platformVersion":"21","appVersion":"1.0"}
 * </p>
 * 请求参数
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>publisher</td>
 * <td>意见反馈发布者id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>反馈内容</td>
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
 * </table>
 * <p>
 * 返回示例<br>成功时：{"code":0,"message":"请求成功"} <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
 * </p>
 */
public class SubmitFeedback extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Feedback param = CommonUtil.parseJsonToObject(parameter, Feedback.class);
            if (param != null && param.check()) {// 请求参数都不为空
                FeedbackService fs = new FeedbackService();
                fs.submitFeedback(br, param.publisher, param.content, param.platform, param.platformVersion, param.appVersion);
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
