package com.skye.lover.common.model;

/**
 * 请求参数检查
 */
public interface RequestParameterCheck {
    /**
     * 检查请求参数有效性
     *
     * @return 请求参数是否有效
     */
    boolean check();
}
