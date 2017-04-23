package com.skye.lover.pillowtalk.followpillowtalk.ui;

import com.skye.lover.pillowtalk.followpillowtalk.model.req.DeleteFollowPillowTalkRequest;
import com.skye.lover.pillowtalk.followpillowtalk.model.req.FollowsRequest;
import com.skye.lover.pillowtalk.followpillowtalk.model.req.PublishFollowPillowTalkRequest;
import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.followpillowtalk.service.FollowPillowTalkService;
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
 * 跟随悄悄话控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/followpillowtalk/")
public class FollowPillowTalkController {
    private static final Logger log = LoggerFactory.getLogger(FollowPillowTalkController.class);

    /**
     * 跟随悄悄话业务层业务层
     */
    private FollowPillowTalkService fpts;

    @Autowired
    public FollowPillowTalkController(FollowPillowTalkService fpts) {
        this.fpts = fpts;
    }

    /**
     * <h2 style='{text-align:center;}'>发表跟随悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/followpillowtalk/PublishFollowPillowTalk?parameter={"userId":"4","pillowTalkId":"10","content":"昭华易谢君难见"}
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
     * <td>跟随悄悄话内容</td>
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
    @RequestMapping(value = "PublishFollowPillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object publishFollowPillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PublishFollowPillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, PublishFollowPillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                fpts.insert(br, param.pillowTalkId, param.userId, param.content);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PublishFollowPillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>删除跟随悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/followpillowtalk/DeleteFollowPillowTalk?parameter={"userId":"3","followPillowTalkId":"10"}
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
     * <td>followPillowTalkId</td>
     * <td>跟随悄悄话id</td>
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
    @RequestMapping(value = "DeleteFollowPillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deleteFollowPillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeleteFollowPillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, DeleteFollowPillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                fpts.delete(br, param.followPillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("DeleteFollowPillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>跟随悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/followpillowtalk/Follows?parameter={"pillowTalkId":"3","page":"1"}
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
     * <td>当前请求的下一页，比如请求时page=1，则返回page=2</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>
     * 成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"3","pillowTalkId":"3","publisher":"2",
     * "content":"有啊有啊","createTime":"1481699356000"},{"id":"1","pillowTalkId":"3","publisher":"2","content":"我也喜欢你","createTime":"1481699274000"}]},
     * "message":"请求成功"}  <br>
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
     * <td>跟随悄悄话ID</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>pillowTalkId</td>
     * <td>悄悄话id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>publisher</td>
     * <td>跟随悄悄话发布者ID</td>
     * <td>String</td>
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
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Follows", method = {RequestMethod.POST, RequestMethod.GET})
    public Object follows(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            FollowsRequest param = CommonUtil.parseJsonToObject(parameter, FollowsRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                fpts.follows(br, param.pillowTalkId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("Follows\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
