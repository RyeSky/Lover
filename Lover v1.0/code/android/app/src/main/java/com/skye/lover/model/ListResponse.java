package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 列表数据返回
 */
public class ListResponse {
    @Expose
    public int page, pageCount;//页数、总页数

    @Override
    public String toString() {
        return "ListResponse{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                '}';
    }
}
