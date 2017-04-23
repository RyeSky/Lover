package com.skye.lover.pillowtalk.report.ui;

import com.skye.lover.pillowtalk.report.model.req.ReportPillowTalkRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.report.service.ReportPillowTalkService;
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
 * 举报悄悄话控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/")
public class ReportPillowTalkController {
    private static final Logger log = LoggerFactory.getLogger(ReportPillowTalkController.class);

    /**
     * 举报悄悄话业务层
     */
    private ReportPillowTalkService rpts;

    @Autowired
    public ReportPillowTalkController(ReportPillowTalkService rpts) {
        this.rpts = rpts;
    }

    /**
     * <h2 style='{text-align:center;}'>举报悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/ReportPillowTalk?parameter={"pillowTalkId":"3","reporter":"1","content":"涉及非法"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "ReportPillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object reportPillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            ReportPillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, ReportPillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数都不为空
                rpts.insert(br, param.pillowTalkId, param.reporter, param.content);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("ReportPillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

}
