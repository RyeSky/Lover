package com.skye.lover.pillowtalk.pillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 蜜语请求实体类
 */
public class HoneyWordRequest extends PillowTalksRequest {
    /**
     * 相恋关系中的另一方id
     */
    @Expose
    public String another;

    @Override
    public boolean check() {
        return super.check() && !CommonUtil.isBlank(another);
    }

    @Override
    public String toString() {
        return "HoneyWordRequest{" +
                "another='" + another + '\'' +
                '}' + super.toString();
    }
}
