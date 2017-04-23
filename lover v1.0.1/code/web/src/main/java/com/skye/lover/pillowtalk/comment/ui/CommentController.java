package com.skye.lover.pillowtalk.comment.ui;

import com.skye.lover.pillowtalk.comment.model.req.CommentsRequest;
import com.skye.lover.pillowtalk.comment.model.req.DeleteCommentRequest;
import com.skye.lover.pillowtalk.comment.model.req.PublishCommentRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.comment.service.CommentService;
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
 * 悄悄话评论控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/comment/")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    /**
     * 评论业务层
     */
    private CommentService cs;

    @Autowired
    public CommentController(CommentService cs) {
        this.cs = cs;
    }

    /**
     * <h2 style='{text-align:center;}'>发表评论</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/comment/PublishComment?parameter={"userId":"2","pillowTalkId":"10","content":"昭华易谢君难见"}
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
     * <tr>
     * <td>content</td>
     * <td>评论内容</td>
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
    @RequestMapping(value = "PublishComment", method = {RequestMethod.POST, RequestMethod.GET})
    public Object publishComment(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PublishCommentRequest param = CommonUtil.parseJsonToObject(parameter, PublishCommentRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                cs.insert(br, param.pillowTalkId, param.userId, param.content);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PublishComment\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>删除评论</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/comment/DeleteComment?parameter={"userId":"2","commentId":"1"}
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
     * <td>commentId</td>
     * <td>评论id</td>
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
    @RequestMapping(value = "DeleteComment", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deleteComment(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeleteCommentRequest param = CommonUtil.parseJsonToObject(parameter, DeleteCommentRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                cs.delete(br, param.commentId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("DeleteComment\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>评论列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/comment/Comments?parameter={"pillowTalkId":"10","page":"1"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Comments", method = {RequestMethod.POST, RequestMethod.GET})
    public Object comments(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            CommentsRequest param = CommonUtil.parseJsonToObject(parameter, CommentsRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                cs.comments(br, param.pillowTalkId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("Comments\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
