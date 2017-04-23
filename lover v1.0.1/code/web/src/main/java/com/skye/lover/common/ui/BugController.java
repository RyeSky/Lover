package com.skye.lover.common.ui;

import com.skye.lover.common.model.req.SubmitBugRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.service.BugService;
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
 * bug控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/common/")
public class BugController {
    private static final Logger log = LoggerFactory.getLogger(BugController.class);
    /**
     * bug业务层
     */
    private BugService bs;

    @Autowired
    public BugController(BugService bs) {
        this.bs = bs;
    }

    /**
     * <h2 style='{text-align:center;}'>提交app bug日志</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/common/SubmitBug?parameter={"mobileBrand":"OPPO","mobileVersion":"R7007","platform":"0","platformVersion":"21","appVersion":"1.0","bugDetails":"NPE"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "SubmitBug", method = {RequestMethod.POST, RequestMethod.GET})
    public Object submitBug(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            SubmitBugRequest param = CommonUtil.parseJsonToObject(parameter, SubmitBugRequest.class);
            if (param != null && param.check()) {// 请求参数都不为空
                bs.submitBug(br, param.mobileBrand, param.mobileVersion, param.platform, param.platformVersion, param.appVersion, param.bugDetails);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("submitBug\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
