package com.skye.lover.logic.chatinput;

/**
 * 发送事件监听
 */
public interface OnSendListener {
    /**
     * 发送时调用
     * @param content 发送内容
     * */
    public void onSend(SendingContent content);
}
