package com.skye.lover.pillowtalk.pillowtalk.model.req;

import com.google.gson.annotations.Expose;
import com.skye.lover.common.model.RequestParameterCheck;
import com.skye.lover.pillowtalk.pillowtalk.model.resp.PillowTalk;
import com.skye.lover.util.CommonUtil;

/**
 * 发表悄悄话请求实体类
 */
public class PublishPillowTalkRequest implements RequestParameterCheck {
    /**
     * 用户id
     */
    @Expose
    public String userId;
    /**
     * 悄悄话内容
     */
    @Expose
    public String content;
    /**
     * 图片url
     */
    @Expose
    public String imgs;
    /**
     * 类型【0:悄悄话；1:广播】
     */
    @Expose
    public int type;

    @Override
    public boolean check() {
        return !CommonUtil.isBlank(userId) && (type == PillowTalk.TYPE_PILLOW_TALK || type == PillowTalk.TYPE_BROADCAST)
                && (!CommonUtil.isBlank(content) || !CommonUtil.isBlank(imgs));
    }

    @Override
    public String toString() {
        return "Parameter{" + "userId='" + userId + '\'' + ", another='"
                + type + '\'' + ", content='" + content + '\''
                + ", imgs='" + imgs + '\'' + '}';
    }
}
