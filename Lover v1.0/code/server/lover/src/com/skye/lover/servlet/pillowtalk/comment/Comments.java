package com.skye.lover.servlet.pillowtalk.comment;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.service.CommentService;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h2 style='{text-align:center;}'>评论列表</h2> 请求方式：POST，调试时暂时支持GET
 * <p>
 * 请求示例：http://localhost:8080/Lover/servlet/pillowtalk/comment/Comments?parameter={"pillowTalkId":"10","page":"1"}
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
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>page</td>
 * <td>请求页数</td>
 * <td>String</td>
 * </tr>
 * </table>
 * <p>
 * 返回示例<br>
 * 成功时：{"code":0,"result":{"page":2,"pageCount":2,"list":[{"id":"24","pillowTalkId":"10","commenter":"3","nickname":"神秘人物","content":"快乐","createTime":"1484667551000","gender":0},{"id":"21","pillowTalkId":"10","commenter":"2","nickname":"想念","avatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"jjh","createTime":"1483347168000","gender":0},{"id":"12","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"十","createTime":"1483250158000","gender":2},{"id":"11","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"九","createTime":"1483250150000","gender":2},{"id":"10","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"八","createTime":"1483250135000","gender":2},{"id":"9","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"七","createTime":"1483250126000","gender":2},{"id":"8","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"六","createTime":"1483250117000","gender":2},{"id":"7","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"五","createTime":"1483250111000","gender":2},{"id":"6","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"四","createTime":"1483250106000","gender":2},{"id":"5","pillowTalkId":"10","commenter":"1","nickname":"花开一半","avatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","content":"三","createTime":"1483250101000","gender":2}]},"message":"请求成功"}  <br>
 * 失败时：{"code":1,"message":"请求失败，请稍后重试"}
 * </p>
 * 返回参数
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
 * rules="all"  summary='返回参数'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>page</td>
 * <td>当前请求的下一页，比如请求时page=1，则返回page=2</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>pageCount</td>
 * <td>记录总页数</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>list</td>
 * <td>数据集合</td>
 * <td>array</td>
 * </tr>
 * <tr>
 * <td>id</td>
 * <td>评论ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>pillowTalkId</td>
 * <td>悄悄话id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>commenter</td>
 * <td>评论发布者ID</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>评论内容</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>createTime</td>
 * <td>评论发布时间时间截</td>
 * <td>String</td>
 * </tr>
 * </table>
 */
public class Comments extends HttpServlet {

    private class Parameter {
        @Expose
        String pillowTalkId;// 用户id，悄悄话id
        @Expose
        int page;// 请求页数

        /**
         * 检查 参数是否不为空
         */
        public boolean check() {
            return !CommonUtil.isBlank(pillowTalkId) && page > 0;
        }

        @Override
        public String toString() {
            return "Parameter{" + "pillowTalkId='" + pillowTalkId + '\'' + ", page=" + page + '}';
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
                CommentService cs = new CommentService();
                cs.comments(br, param.pillowTalkId, param.page);
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
