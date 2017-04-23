package com.skye.lover.loverrelatiionship.ui;

import com.skye.lover.loverrelatiionship.model.req.LoverRelatiionshipRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.loverrelatiionship.service.LoverRelationshipService;
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
 * 相恋关系控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/loverrelatiionship/")
public class LoverRelatiionshipController {
    private static final Logger log = LoggerFactory.getLogger(LoverRelatiionshipController.class);
    /**
     * 相恋关系业务层
     */
    private LoverRelationshipService lrs;

    @Autowired
    public LoverRelatiionshipController(LoverRelationshipService lrs) {
        this.lrs = lrs;
    }

    /**
     * <h2 style='{text-align:center;}'>分手</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/loverrelatiionship/BreakUp?parameter={"userId":"2","another":"1"}
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
     * <td>another</td>
     * <td>另一方用户id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"message":"请求成功"}
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "BreakUp", method = {RequestMethod.POST, RequestMethod.GET})
    public Object breakUp(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            LoverRelatiionshipRequest param = CommonUtil.parseJsonToObject(parameter, LoverRelatiionshipRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                lrs.breakUp(br, param.userId, param.another);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("breakUp\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>坠入爱河</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/loverrelatiionship/FallInLove?parameter={"userId":"2","another":"1"}
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
     * <td>another</td>
     * <td>另一方用户id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"result":{"id":"2","name":"xiangnian","password":"e10adc3949ba59abbe56e057f20f883e","nickname":"想念","avatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","birthday":"1991-03-22","loginTime":"1484612290000","createTime":"1479458995000","gender":0,"anotherGender":0},"message":"请求成功"}
     * <br>或者：{"code":0,"message":"请求成功"}
     * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
     * </p>
     * 返回参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='返回参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
     * </tr>
     * <tr>
     * <td>id</td>
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>name</td>
     * <td>用户账号</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>password</td>
     * <td>32位md5加密后的密码</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>nickname</td>
     * <td>昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>avatar</td>
     * <td>头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>birthday</td>
     * <td>生日</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>loginTime</td>
     * <td>当次登录时间时间截</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>createTime</td>
     * <td>用户注册时间时间截</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>gender</td>
     * <td>性别【0：保密；1：男；2：女】</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "FallInLove", method = {RequestMethod.POST, RequestMethod.GET})
    public Object fallInLove(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            LoverRelatiionshipRequest param = CommonUtil.parseJsonToObject(parameter, LoverRelatiionshipRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                lrs.fallInLove(br, param.userId, param.another);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("fallInLove\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
