package com.skye.lover.model;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 穿越
 */
public class Cross {
    /**
     * 请求动作地址
     */
    public final String action;
    /**
     * 请求的参数
     */
    public final Map<String, String> params;
    /**
     * 是否需要显示错误信息
     */
    public final boolean isNeedShowErrorMessage;
    /**
     * 是否需要缓存返回数据
     */
    public final boolean isNeedCacheResponseData;
    /**
     * 请求
     */
    public final Call call;
    /**
     * 网络请求数据响应
     */
    public String body;
    /**
     * 附带信息
     */
    public Object extra;
    /**
     * 网络是否良好
     */
    public boolean isNetworkConnected;

    public Cross(String action, Map<String, String> params, boolean isNeedCacheResponseData, boolean isNeedShowErrorMessage, Call call) {
        this.action = action;
        this.params = params;
        this.isNeedCacheResponseData = isNeedCacheResponseData;
        this.isNeedShowErrorMessage = isNeedShowErrorMessage;
        this.call = call;
    }

    public Cross(String action, Call call) {
        this.action = action;
        this.params = null;
        this.isNeedCacheResponseData = false;
        this.isNeedShowErrorMessage = false;
        this.call = call;
    }

    public Cross(String action, Map<String, String> params, boolean isNeedShowErrorMessage, String body) {
        this.action = action;
        this.params = params;
        this.isNeedCacheResponseData = false;
        this.isNeedShowErrorMessage = isNeedShowErrorMessage;
        this.call = null;
        this.body = body;
    }

    public Cross setNetworkConnected(boolean networkConnected) {
        isNetworkConnected = networkConnected;
        return this;
    }

    public Cross setExtra(Object extra) {
        this.extra = extra;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cross cross = (Cross) o;
        if (!action.equals(cross.action)) return false;
        return params != null ? params.equals(cross.params) : cross.params == null;
    }

    @Override
    public int hashCode() {
        int result = action.hashCode();
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cross{" +
                "action='" + action + '\'' +
                ", params=" + params +
                ", isNeedShowErrorMessage=" + isNeedShowErrorMessage +
                ", isNeedCacheResponseData=" + isNeedCacheResponseData +
                ", call=" + call +
                ", body='" + body + '\'' +
                '}';
    }
}
