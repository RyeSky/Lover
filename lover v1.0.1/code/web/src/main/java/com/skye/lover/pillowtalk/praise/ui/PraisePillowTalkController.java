package com.skye.lover.pillowtalk.praise.ui;

import com.skye.lover.pillowtalk.praise.model.req.CancelPraiseRequest;
import com.skye.lover.pillowtalk.praise.model.req.PraiseRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.praise.service.PraiseService;
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
 * 赞悄悄话控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/praise/")
public class PraisePillowTalkController {
    private static final Logger log = LoggerFactory.getLogger(PraisePillowTalkController.class);

    /**
     * 赞业务层
     */
    private PraiseService ps;

    @Autowired
    public PraisePillowTalkController(PraiseService ps) {
        this.ps = ps;
    }

    /**
     * <h2 style='{text-align:center;}'>赞悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/praise/Praise?parameter={"userId":"2","pillowTalkId":"10"}
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
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"result":"6","message":"请求成功"}<br>
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
     * <td>result</td>
     * <td>赞记录id</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Praise", method = {RequestMethod.POST, RequestMethod.GET})
    public Object praise(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PraiseRequest param = CommonUtil.parseJsonToObject(parameter, PraiseRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                ps.praise(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("Praise\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>取消赞悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/praise/CancelPraise?parameter={"userId":"2","pillowTalkId":"6"}
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
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"message":"请求成功"} <br>
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "CancelPraise", method = {RequestMethod.POST, RequestMethod.GET})
    public Object cancelPraise(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            CancelPraiseRequest param = CommonUtil.parseJsonToObject(parameter, CancelPraiseRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                ps.cancelPraise(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("CancelPraise\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

}
