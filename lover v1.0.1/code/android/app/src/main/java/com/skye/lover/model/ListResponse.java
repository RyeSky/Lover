package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 列表数据返回
 */
@Keep
public class ListResponse<T> {
    /**
     * 当前请求的下一页
     */
    @Expose
    public int page;
    /**
     * 总页数
     */
    @Expose
    public int pageCount;
    /**
     * 数据集合
     */
    @Expose
    public List<T> list;

    @Override
    public String toString() {
        return "ListResponse{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", list=" + list +
                '}';
    }
}
