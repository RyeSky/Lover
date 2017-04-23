package com.skye.lover.common.model.resp;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 返回数据基类
 */
public class BaseResponse {
    /**
     * 请求成功时的code值
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * 请求失败时的code值
     */
    public static final int CODE_FAIL = 1;

    /**
     * 请求成功时的message值
     */
    public static final String MESSAGE_SUCCESS = "请求成功";
    /**
     * 请求失败时的message值
     */
    public static final String MESSAGE_FAIL = "请求失败，请稍后重试";
    /**
     * 请求参数有误
     */
    public static final String MESSAGE_REQUEST_PARAMETER_INCORRECT = "请求参数有误";
    /**
     * 悄悄话、世界广播、评论等等发表失败
     */
    public static final String MESSAGE_PUBLISH_FAIL = "发表失败";
    /**
     * 修改信息时失败
     */
    public static final String MESSAGE_UPDATE_FAIL = "修改失败";
    /**
     * 删除信息时失败
     */
    public static final String MESSAGE_DELETE_FAIL = "删除失败";
    /**
     * 提交信息失败
     */
    public static final String MESSAGE_SUBMIT_FAIL = "提交失败";
    /**
     * 收藏失败
     */
    public static final String MESSAGE_COLLECT_FAIL = "收藏失败";
    /**
     * 赞失败
     */
    public static final String MESSAGE_PRAISE_FAIL = "赞失败";
    /**
     * 取消操作失败
     */
    public static final String MESSAGE_CANCEL_FAIL = "取消失败";
    /**
     * 评论悄悄话或世界广播时失败
     */
    public static final String MESSAGE_COMMENT_FAIL = "评论失败";
    /**
     * 回复悄悄话时失败
     */
    public static final String MESSAGE_REPLY_FAIL = "回复失败";
    /**
     * 发送失败
     */
    public static final String MESSAGE_SEND_FAIL = "发送失败";
    /**
     * 举报失败
     */
    public static final String MESSAGE_REPORT_FAIL = "举报失败";
    /**
     * 请求状态码【0：成功;1：失败】
     */
    @Expose
    public int code = CODE_SUCCESS;
    /**
     * 返回数据实体
     */
    @Expose
    public Object result;
    /**
     * 请求失败时，错误描述
     */
    @Expose
    public String message = MESSAGE_SUCCESS;

    @Override
    public String toString() {
        return CommonUtil.gson.toJson(this);
    }
}
