package com.skye.lover.user.ui;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.user.service.FeedbackService;
import com.skye.lover.user.model.req.*;
import com.skye.lover.user.service.UserService;
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
 * 用户控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    /**
     * 用户业务层
     */
    private UserService us;

    @Autowired
    public void setUserService(UserService us) {
        this.us = us;
    }

    /**
     * <h2 style='{text-align:center;}'>用户登录</h2> 请求方式：POST/GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/Login?parameter={"name":"rsmiss","password":"e10adc3949ba59abbe56e057f20f883e"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
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
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"result":{"id":"4","name":"rsmissyou","password":
     * "123456","nickname":"花开一半","avatar":"huakaiyiban","birthday":"1990-10-31",
     * "loginTime"
     * :"1468311837539","createTime":"1465440326000","another":"2","gender"
     * :1,"anotherNickname":"想念","anotherGender":2},"message":"请求成功"} <br>
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
     * <tr>
     * <td>another</td>
     * <td>相恋关系的另一方用户ID，为空则说明用户是单身</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherNickname</td>
     * <td>相恋关系的另一方的昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherAvatar</td>
     * <td>相恋关系的另一方头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherGender</td>
     * <td>相恋关系的另一方性别</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Login", method = {RequestMethod.POST, RequestMethod.GET})
    public Object login(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            LoginRequest param = CommonUtil.parseJsonToObject(parameter, LoginRequest.class);
            if (param != null && param.check()) {// 账号和密码都不为空
                us.login(br, param.name, param.password);
            } else {// 账号或密码为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_REQUEST_PARAMETER_INCORRECT;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_REQUEST_PARAMETER_INCORRECT;
        }
        log.info("login\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>用户注册</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/Register?parameter={"name":"rsmissyou","password":"e10adc3949ba59abbe56e057f20f883e","gender":"1"}
     * </p>
     * 请求参数
     * <table border="2" width="100%" style='{text-align:center;}' frame="hsides" rules="all" summary='请求参数'>
     * <tr>
     * <td>参数名</td>
     * <td>描述</td>
     * <td>参数类型</td>
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
     * <td>gender</td>
     * <td>性别</td>
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
    @RequestMapping(value = "Register", method = {RequestMethod.POST, RequestMethod.GET})
    public Object register(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            RegisterRequest param = CommonUtil.parseJsonToObject(parameter, RegisterRequest.class);
            if (param != null && param.check()) {// 账号和密码都不为空
                us.register(br, param.name, param.password, param.gender);
            } else {// 账号或密码为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_REQUEST_PARAMETER_INCORRECT;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_REQUEST_PARAMETER_INCORRECT;
        }
        log.info("register\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>其他用户信息</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/OtherInfo?parameter={"userId":"2"}
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
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"result":{"id":"4","name":"rsmissyou","password":
     * "123456","nickname":"花开一半","avatar":"huakaiyiban","birthday":"1990-10-31",
     * "loginTime"
     * :"1468311837539","createTime":"1465440326000","another":"2","gender"
     * :1,"anotherNickname":"想念","anotherGender":2},"message":"请求成功"} <br>
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
     * <tr>
     * <td>another</td>
     * <td>相恋关系的另一方用户ID，为空则说明用户是单身</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherNickname</td>
     * <td>相恋关系的另一方的昵称</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherAvatar</td>
     * <td>相恋关系的另一方头像</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>anotherGender</td>
     * <td>相恋关系的另一方性别</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "OtherInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object otherinfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            OtherInfoRequest param = CommonUtil.parseJsonToObject(parameter, OtherInfoRequest.class);
            if (param != null && param.check()) {// 账号和密码都不为空
                us.otherInfo(br, param.userId);
            } else {// 用户id
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("otherinfo\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>更新用户头像</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/UpdateAvatar?parameter={"userId": "6","avatar":"123456"}
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
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>avatar</td>
     * <td>头像url</td>
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
    @RequestMapping(value = "UpdateAvatar", method = {RequestMethod.POST, RequestMethod.GET})
    public Object updateAvatar(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            UpdateAvatarRequest param = CommonUtil.parseJsonToObject(parameter, UpdateAvatarRequest.class);
            if (param != null && param.check()) {
                us.updateAvatar(br, param.userId, param.avatar);
            } else {// 用户id或头像url为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("updateAvatar\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>更新用户生日</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/UpdateBirthday?parameter={"userId": "6","birthday":"1990-02-10"}
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
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>birthday</td>
     * <td>生日</td>
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
    @RequestMapping(value = "UpdateBirthday", method = {RequestMethod.POST, RequestMethod.GET})
    public Object updateBirthday(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            UpdateBirthdayRequest param = CommonUtil.parseJsonToObject(parameter, UpdateBirthdayRequest.class);
            if (param != null && param.check()) {
                us.updateBirthday(br, param.userId, param.birthday);
            } else {// 用户id或生日为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("updateBirthday\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>更新用户性别</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/UpdateGender?parameter={"userId": "6","gender":"1"}
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
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>gender</td>
     * <td>性别</td>
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
    @RequestMapping(value = "UpdateGender", method = {RequestMethod.POST, RequestMethod.GET})
    public Object updateGender(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            UpdateGenderRequest param = CommonUtil.parseJsonToObject(parameter, UpdateGenderRequest.class);
            if (param != null && param.check()) {
                us.updateGender(br, param.userId, param.gender);
            } else {// 用户id或性别为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("updateGender\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>更新用户昵称</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/UpdateNickname?parameter={"userId": "1","nickname":"123456"}
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
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>nickname</td>
     * <td>昵称</td>
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
    @RequestMapping(value = "UpdateNickname", method = {RequestMethod.POST, RequestMethod.GET})
    public Object updateNickname(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            UpdateNicknameRequest param = CommonUtil.parseJsonToObject(parameter, UpdateNicknameRequest.class);
            if (param != null && param.check()) {
                us.updateNickname(br, param.userId, param.nickname);
            } else {// 用户id或昵称为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("updateNickname\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>更新用户密码</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/user/UpdatePassword?parameter={"userId": "6","oldPassword":"e10adc3949ba59abbe56e057f20f883e","newPassword":"123456"}
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
     * <td>用户Id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>oldPassword</td>
     * <td>32位md5加密后的旧密码</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>newPassword</td>
     * <td>32位md5加密后的新密码</td>
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
    @RequestMapping(value = "UpdatePassword", method = {RequestMethod.POST, RequestMethod.GET})
    public Object updatePassword(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            UpdatePasswordRequest param = CommonUtil.parseJsonToObject(parameter, UpdatePasswordRequest.class);
            if (param != null && param.check()) {
                us.updatePassword(br, param.userId, param.oldPassword, param.newPassword);
            } else {// 用户id、旧密码或新密码为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("updatePassword\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
