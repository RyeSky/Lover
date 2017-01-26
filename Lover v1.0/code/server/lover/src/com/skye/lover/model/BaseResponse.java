package com.skye.lover.model;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 返回数据基类
 */
public class BaseResponse {
    /**
     * 请求成功时的code值
     */
    public static int CODE_SUCCESS = 0;
    /**
     * 请求失败时的code值
     */
    public static int CODE_FAIL = 1;

    /**
     * 请求成功时的message值
     */
    public static String MESSAGE_SUCCESS = "请求成功";
    /**
     * 请求失败时的message值
     */
    public static String MESSAGE_FAIL = "请求失败，请稍后重试";

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
