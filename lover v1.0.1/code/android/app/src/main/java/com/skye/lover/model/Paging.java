package com.skye.lover.model;

/**
 * 翻页
 */
public class Paging {
    /**
     * 请求页数
     */
    public int page;
    /**
     * 请求回数据后的滚动位置
     */
    public ScrollPosition position;

    public Paging(int page, ScrollPosition position) {
        this.page = page;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "page=" + page +
                ", position=" + position +
                '}';
    }
}
