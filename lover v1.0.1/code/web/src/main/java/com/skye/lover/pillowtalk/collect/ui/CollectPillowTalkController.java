package com.skye.lover.pillowtalk.collect.ui;

import com.skye.lover.pillowtalk.collect.model.req.CancelCollectRequest;
import com.skye.lover.pillowtalk.collect.model.req.CollectRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.collect.service.CollectService;
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
 * 收藏悄悄话控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/collect/")
public class CollectPillowTalkController {
    private static final Logger log = LoggerFactory.getLogger(CollectPillowTalkController.class);

    /**
     * 收藏业务层
     */
    private CollectService cs;

    @Autowired
    public CollectPillowTalkController(CollectService cs) {
        this.cs = cs;
    }

    /**
     * <h2 style='{text-align:center;}'>收藏悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/collect/Collect?parameter={"userId":"2","pillowTalkId":"10"}
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
     * <td>收藏记录id</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Collect", method = {RequestMethod.POST, RequestMethod.GET})
    public Object collect(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            CollectRequest param = CommonUtil.parseJsonToObject(parameter, CollectRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                cs.collect(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("Collect\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>取消收藏悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/collect/CancelCollect?parameter={"userId":"2","pillowTalkId":"6"}
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
    @RequestMapping(value = "CancelCollect", method = {RequestMethod.POST, RequestMethod.GET})
    public Object cancelCollect(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            CancelCollectRequest param = CommonUtil.parseJsonToObject(parameter, CancelCollectRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                cs.cancelCollect(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("CancelCollect\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

}
