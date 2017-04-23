package com.skye.lover.common.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 返回列表数据
 */
public class ListResponse {
    /**
     * 当前请求的下一页，例如当前请求第一页，page则为2，客户端判断page大于等于pageCount，成立则为最后一页
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
    public Object list;

    @Override
    public String toString() {
        return "ListResponse [page=" + page + ", pageCount=" + pageCount
                + ", list=" + list + "]";
    }
}
