package com.skye.lover.logic.chatinput;

import com.skye.lover.model.PrivateMessage;

/**
 * 私信发送成功事件监听
 */
public interface OnSendSuccessListener {
    /**
     * 私信发送成功调用
     *
     * @param pm 发送成功产生的私信记录实体
     */
    public void onSendSuccess(PrivateMessage pm);
}
