package com.skye.lover.servlet.loverrelatiionship;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.LoverRelationshipService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>坠入爱河</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/loverrelatiionship/FallInLove?parameter={"userId":"2","another":"1"}
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
 */
public class FallInLove extends HttpServlet {

    private class Parameter {
        @Expose
        String userId, another;// // 用户id，另一方

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(another) && !CommonUtil.isBlank(userId);
        }

        @Override
        public String toString() {
            return "Parameter{" + "userId='" + userId + '\'' + ", another='" + another + '\'' + '}';
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(Const.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            Parameter param = CommonUtil.parseJsonToObject(parameter, Parameter.class);
            if (param != null && param.check()) {// 请求参数不为空
                LoverRelationshipService lrs = new LoverRelationshipService();
                lrs.fallInLove(br, param.userId, param.another);
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
