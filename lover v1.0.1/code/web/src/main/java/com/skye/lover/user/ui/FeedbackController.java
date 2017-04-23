package com.skye.lover.user.ui;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.user.model.req.FeedbackRequest;
import com.skye.lover.user.service.FeedbackService;
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
 * 意见反馈控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/user/")
public class FeedbackController {
    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);

    /**
     * 意见反馈业务层
     */
    private FeedbackService fs;

    @Autowired
    public FeedbackController(FeedbackService fs) {
        this.fs = fs;
    }

    /**
     * <h2 style='{text-align:center;}'>提交意见反馈</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/SubmitFeedback?parameter={"publisher":"4","content":"you dai gai jin","platform":"0","platformVersion":"21","appVersion":"1.0"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "SubmitFeedback", method = {RequestMethod.POST, RequestMethod.GET})
    public Object submitFeedback(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            FeedbackRequest param = CommonUtil.parseJsonToObject(parameter, FeedbackRequest.class);
            if (param != null && param.check()) {// 请求参数都不为空
                fs.submitFeedback(br, param.publisher, param.content, param.platform, param.platformVersion, param.appVersion);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("submitFeedback\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
