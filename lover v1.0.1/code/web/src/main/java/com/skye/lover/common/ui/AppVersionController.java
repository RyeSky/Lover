package com.skye.lover.common.ui;

import com.skye.lover.common.model.req.AppVersionRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.service.AppVersionService;
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
 * app版本控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/common/")
public class AppVersionController {
    private static final Logger log = LoggerFactory.getLogger(AppVersionController.class);

    /**
     * app版本业务层
     */
    private AppVersionService avs;

    @Autowired
    public AppVersionController(AppVersionService avs) {
        this.avs = avs;
    }

    /**
     * <h2 style='{text-align:center;}'>根据手机平台查询最新版本</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/common/LastAppVersion?parameter={"platform":"0"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>platform</td>
     * <td>手机平台【0：android;1：ios】</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"id":"2","appVersion":"1.0","platform":0,"forceUpdate":0,"downloadUrl":"http://www.baidu.com","title":"有新版本可更新","content":"修复了一些bug","createTime":"1491459330000"},"message":"请求成功"}
     * 或者{"code":0,"result":{"appVersion":"1.0","platform":1,"forceUpdate":0},"message":"请求成功"}
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
     * <td>id</td>
     * <td>app版本记录ID</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>appVersion</td>
     * <td>app版本号</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>platform</td>
     * <td>手机平台【0：android;1：ios】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>forceUpdate</td>
     * <td>是否强制更新【0：不强制;1：强制】</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>downloadUrl</td>
     * <td>app下载地址</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>title</td>
     * <td>更新标题</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>content</td>
     * <td>更新内容</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>createTime</td>
     * <td>app版本创建时间</td>
     * <td>String</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "LastAppVersion", method = {RequestMethod.POST, RequestMethod.GET})
    public Object lastAppVersion(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            AppVersionRequest param = CommonUtil.parseJsonToObject(parameter, AppVersionRequest.class);
            if (param != null && param.check()) {// 请求参数都不为空
                avs.query(br, param.platform);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("LastAppVersion\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
