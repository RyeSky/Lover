package com.skye.lover.servlet.user;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.UserService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>用户登录</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/user/Login?parameter={"name":"rsmissyou","password":"123456"}
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
 */
public class Login extends HttpServlet {

    private class Parameter {
        @Expose
        String name, password;// 用户账号、密码

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(name) && !CommonUtil.isBlank(password);
        }

        @Override
        public String toString() {
            return "Param [name=" + name + ", password=" + password + "]";
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Parameter param = CommonUtil.parseJsonToObject(parameter, Parameter.class);
            if (param != null && param.check()) {// 账号和密码都不为空
                UserService us = new UserService();
                us.login(br, param.name, param.password);
            } else {// 账号或密码为空
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
