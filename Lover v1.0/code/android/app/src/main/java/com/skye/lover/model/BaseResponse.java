package com.skye.lover.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 返回数据基类
 */
public class BaseResponse {
    public static int CODE_SUCCESS = 0;// 请求成功
    public static int CODE_FAIL = 1;// 请求失败

    public static String MESSAGE_SUCCESS = "请求成功";
    public static String MESSAGE_FAIL = "请求失败，请稍后重试";

    @Expose
    public int code = CODE_SUCCESS;// 请求是否成功【0：成功;1：失败】
    @Expose
    public JsonElement result;// 返回数据
    @Expose
    public String message = MESSAGE_SUCCESS;// 请求失败时，错误描述

    /**
     * 检查是否请求成功
     */
    public boolean check() {
        return code == CODE_SUCCESS;
    }

    @Override
    public String toString() {
        return CommonUtil.gson.toJson(this);
    }

}

