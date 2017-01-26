package com.skye.lover.servlet.pillowtalk;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.PillowTalkService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>悄悄话详情</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：
 * http://localhost:8080/Lover/servlet/pillowtalk/PillowTalkDetail?parameter={"pillowTalkId":"10","userId":"1"}
 * </p>
 * 请求参数
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
 * rules="all" summary='请求参数'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>pillowTalkId</td>
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>userId</td>
 * <td>用户id</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>成功时：{"code":0,"result":{"id":"10","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.191.1:8080/Lover/upload/ee94e754-efe9-4b6b-9694-cc5bf066f15f.jpg,http://192.168.191.1:8080/Lover/upload/b11c72e1-3e12-4338-b4c6-8e1e8b934cf9.jpg,http://192.168.191.1:8080/Lover/upload/0551686a-d30b-4d98-9cc6-9f7f17638f12.jpg","praiseId":"3","createTime":"1483099998000","publisherGender":2,"anotherGender":0,"praiseCount":2,"commentCount":12,"publisherOpen":1,"anotherOpen":1},"message":"请求成功"}
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
 * <td>悄悄话ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>publisherId</td>
 * <td>悄悄话发布者ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>publisherNickname</td>
 * <td>悄悄话发布者昵称</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>publisherAvatar</td>
 * <td>悄悄话发布者头像</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>publisherGender</td>
 * <td>悄悄话发布者性别【0：保密；1：男；2：女】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>publisherOpen</td>
 * <td>悄悄话发布者是否愿意公开</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>anotherId</td>
 * <td>相恋关系中的另一方ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherNickname</td>
 * <td>相恋关系中的另一方昵称</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherAvatar</td>
 * <td>相恋关系中的另一方头像</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherGender</td>
 * <td>相恋关系中的另一方性别【0：保密；1：男；2：女】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>anotherOpen</td>
 * <td>另一方是否愿意公开</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>悄悄话内容</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>createTime</td>
 * <td>悄悄话发布时间时间截</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>praiseCount</td>
 * <td>悄悄话收到的赞数量</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>commentCount</td>
 * <td>悄悄话收到的评论数量</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>praiseId</td>
 * <td>赞记录id，没有赞时为空</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>collectId</td>
 * <td>收藏记录id，没有收藏时为空</td>
 * <td>int</td>
 * </tr>
 * </table>
 */
public class PillowTalkDetail extends HttpServlet {

    private class Parameter {
        @Expose
        String pillowTalkId, userId;// 悄悄话id，用户id

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(pillowTalkId) && !CommonUtil.isBlank(userId);
        }

        @Override
        public String toString() {
            return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + ", userId='" + userId + '\'' + '}';
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
            if (param != null && param.check()) {// 请求参数不为空
                PillowTalkService pts = new PillowTalkService();
                pts.query(br, param.pillowTalkId, param.userId);
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
