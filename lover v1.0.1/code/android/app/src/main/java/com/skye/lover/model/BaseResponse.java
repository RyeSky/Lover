package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 返回数据基类
 */
@Keep
public class BaseResponse<T> {
    private static final int CODE_SUCCESS = 0;

    private static final String MESSAGE_SUCCESS = "请求成功";

    @Expose
    public int code = CODE_SUCCESS;
    @Expose
    public T result;
    @Expose
    public String message = MESSAGE_SUCCESS;

    /**
     * 检查是否请求成功
     */
    boolean check() {
        return code == CODE_SUCCESS;
    }

    @Override
    public String toString() {
        return CommonUtil.gson.toJson(this);
    }
}

