package com.skye.lover.model;

import android.content.Intent;

/**
 * 网络请求或页面跳转
 */
public class Request {
    /**
     * 接口地址
     */
    public String action;
    /**
     * 页面跳转
     */
    public Intent intent;

    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", intent=" + intent +
                '}';
    }
}
