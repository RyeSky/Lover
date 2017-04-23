package com.skye.lover.pillowtalk.pillowtalk.ui;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.pillowtalk.pillowtalk.model.req.*;
import com.skye.lover.pillowtalk.pillowtalk.service.PillowTalkService;
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
 * 悄悄话控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/pillowtalk/")
public class PillowTalkController {
    private static final Logger log = LoggerFactory.getLogger(PillowTalkController.class);

    /**
     * 悄悄话业务层
     */
    private PillowTalkService pts;

    @Autowired
    public PillowTalkController(PillowTalkService pts) {
        this.pts = pts;
    }

    /**
     * <h2 style='{text-align:center;}'>发现</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/Find?parameter={"page":"1","userId":"5"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","collectId":"10","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":3,"publisherOpen":1,"anotherOpen":1}]},"message":"请求成功"}
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
     * <td>collectId</td>
     * <td>收藏记录id，没有收藏时为空</td>
     * <td>int</td>
     * </tr>
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "Find", method = {RequestMethod.POST, RequestMethod.GET})
    public Object find(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalksRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalksRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.find(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("Find\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>蜜语</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/HoneyWord?parameter={"userId":"5","page":"1","another":"6"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>another</td>
     * <td>相恋关系中的另一方</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"10","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.191.1:8080/Lover/upload/ee94e754-efe9-4b6b-9694-cc5bf066f15f.jpg,http://192.168.191.1:8080/Lover/upload/b11c72e1-3e12-4338-b4c6-8e1e8b934cf9.jpg,http://192.168.191.1:8080/Lover/upload/0551686a-d30b-4d98-9cc6-9f7f17638f12.jpg","praiseId":"3","createTime":"1483099998000","publisherGender":2,"anotherGender":0,"praiseCount":2,"commentCount":12,"publisherOpen":1,"anotherOpen":1},{"id":"9","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"fggh😖😖😍😍😳😰😍😍😳😰😰","imgs":"http://192.168.2.58:8080/Lover/upload/fe4b915a-50fe-4f85-b2f8-6a7f4889b8eb.png","createTime":"1482913783000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":0,"publisherOpen":0,"anotherOpen":1},{"id":"8","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","createTime":"1482913729000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":2,"publisherOpen":1,"anotherOpen":1},{"id":"7","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","createTime":"1482913728000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":1,"publisherOpen":1,"anotherOpen":1},{"id":"6","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"😰😰😰😰😰😰😰😰","imgs":"","createTime":"1482550896000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":0,"publisherOpen":0,"anotherOpen":0},{"id":"5","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/118a5889-4035-44e7-ba28-299d180cf40d.jpeg","createTime":"1480378474000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":0,"publisherOpen":1,"anotherOpen":0},{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":1,"commentCount":3,"publisherOpen":1,"anotherOpen":1},{"id":"1","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"hello world!","imgs":"","createTime":"1480127828000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":0,"publisherOpen":0,"anotherOpen":1}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "HoneyWord", method = {RequestMethod.POST, RequestMethod.GET})
    public Object honeyWord(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            HoneyWordRequest param = CommonUtil.parseJsonToObject(parameter, HoneyWordRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.honeyWord(br, param.userId, param.another, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("HoneyWord\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>发表悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/PublishPillowTalk?parameter={"userId":"4","another":"2","content":"我好想你":""}
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
     * <td>type</td>
     * <td>类型【0:悄悄话；1:广播】</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>content</td>
     * <td>悄悄话内容</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>imgs</td>
     * <td>多张图片路径，用英文逗号分隔</td>
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
    @RequestMapping(value = "PublishPillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object publishPillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PublishPillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, PublishPillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.insert(br, param.userId, param.type, param.content, param.imgs);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PublishPillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>悄悄话详情</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：
     * http://localhost:8080/Lover/mobile/pillowtalk/PillowTalkDetail?parameter={"pillowTalkId":"10","userId":"1"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "PillowTalkDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Object pillowTalkDetail(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalkDetailRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalkDetailRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.query(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PillowTalkDetail\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>悄悄话部分属性</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：
     * http://localhost:8080/Lover/mobile/pillowtalk/PillowTalkProperties?parameter={"pillowTalkId":"10"}
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
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"praiseCount":0,"commentCount":11,"publisherOpen":1,"anotherOpen":0},"message":"请求成功"}
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
     * <td>publisherOpen</td>
     * <td>悄悄话发布者是否愿意公开</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>anotherOpen</td>
     * <td>另一方是否愿意公开</td>
     * <td>int</td>
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
     * </table>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "PillowTalkProperties", method = {RequestMethod.POST, RequestMethod.GET})
    public Object pillowTalkProperties(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalkPropertiesRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalkPropertiesRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.properties(br, param.pillowTalkId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PillowTalkProperties\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>刪除悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/DeletePillowTalk?parameter={"userId":"4","pillowTalkId":"10"}
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
    @RequestMapping(value = "DeletePillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object deletePillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            DeletePillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, DeletePillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.delete(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("DeletePillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }


    /**
     * <h2 style='{text-align:center;}'>公开悄悄话</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/OpenPillowTalk?parameter={"userId":"4","pillowTalkId":"10"}
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
    @RequestMapping(value = "OpenPillowTalk", method = {RequestMethod.POST, RequestMethod.GET})
    public Object openPillowTalk(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            OpenPillowTalkRequest param = CommonUtil.parseJsonToObject(parameter, OpenPillowTalkRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.open(br, param.pillowTalkId, param.userId);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("OpenPillowTalk\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>指定用户发表的悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/OthersPillowTalks?parameter={"userId":"1","page":"1"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"7","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","collectId":"13","createTime":"1482913728000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":1,"publisherOpen":1,"anotherOpen":1},{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","praiseId":"1","collectId":"14","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":1,"commentCount":3,"publisherOpen":1,"anotherOpen":1}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "OthersPillowTalks", method = {RequestMethod.POST, RequestMethod.GET})
    public Object othersPillowTalks(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalksRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalksRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.others(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("OthersPillowTalks\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>用户收藏的悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/collect/CollectedPillowTalks?parameter={"userId":"1","page":"1"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"7","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","collectId":"13","createTime":"1482913728000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":1,"publisherOpen":1,"anotherOpen":1},{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","praiseId":"1","collectId":"14","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":1,"commentCount":3,"publisherOpen":1,"anotherOpen":1}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "collect/CollectedPillowTalks", method = {RequestMethod.POST, RequestMethod.GET})
    public Object collectedPillowTalks(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalksRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalksRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.collected(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("CollectedPillowTalks\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>用户赞过的悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/praise/PraisedPillowTalks?parameter={"userId":"2","page":"1"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"7","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","collectId":"13","createTime":"1482913728000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":1,"publisherOpen":1,"anotherOpen":1},{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","praiseId":"1","collectId":"14","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":1,"commentCount":3,"publisherOpen":1,"anotherOpen":1}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "praise/PraisedPillowTalks", method = {RequestMethod.POST, RequestMethod.GET})
    public Object praisedPillowTalks(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter(ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalksRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalksRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.praised(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("PraisedPillowTalks\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }

    /**
     * <h2 style='{text-align:center;}'>用户评论过的悄悄话列表</h2> 请求方式：POST，调试时暂时支持GET
     * <p>
     * 请求示例：http://localhost:8080/Lover/mobile/pillowtalk/comment/CommentedPillowTalks?parameter={"userId":"2","page":"1"}
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
     * <td>userId</td>
     * <td>用户id</td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td>page</td>
     * <td>请求页数</td>
     * <td>String</td>
     * </tr>
     * </table>
     * <p>
     * 返回示例<br>成功时：{"code":0,"result":{"page":2,"pageCount":1,"list":[{"id":"7","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"dddfp😰😰😰😰😡😡","imgs":"","collectId":"13","createTime":"1482913728000","publisherGender":2,"anotherGender":0,"praiseCount":0,"commentCount":1,"publisherOpen":1,"anotherOpen":1},{"id":"3","publisherId":"1","publisherNickname":"花开一半","publisherAvatar":"http://192.168.2.58:8080/Lover/upload/fb39f409-b679-43b7-b910-2fe058333e7f.jpeg","anotherId":"2","anotherNickname":"想念","anotherAvatar":"http://192.168.191.1:8080/Lover/upload/7f65e5ed-46b2-4db0-8da6-6c2a610b3f30.jpeg","content":"","imgs":"http://192.168.2.58:8080/Lover/upload/34ee1927-2a62-4580-b86d-95087a4797c8.jpg","praiseId":"1","collectId":"14","createTime":"1480378332000","publisherGender":2,"anotherGender":0,"praiseCount":1,"commentCount":3,"publisherOpen":1,"anotherOpen":1}]},"message":"请求成功"}
     * <br>或{"code":0,"result":{"page":2,"pageCount":0,"list":
     * []},"message":"请求成功"}
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
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "comment/CommentedPillowTalks", method = {RequestMethod.POST, RequestMethod.GET})
    public Object commentedPillowTalks(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        String parameter = request.getParameter
                (ConstantUtil.PARAMETER);
        if (!CommonUtil.isBlank(parameter)) {// 请求参数不为空
            PillowTalksRequest param = CommonUtil.parseJsonToObject(parameter, PillowTalksRequest.class);
            if (param != null && param.check()) {// 请求参数不为空
                pts.commented(br, param.userId, param.page);
            } else {// 请求参数为空
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 请求参数为空
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("CommentedPillowTalks\n\t" + parameter + "\n\t" + br.toString());
        return br.toString();
    }
}
