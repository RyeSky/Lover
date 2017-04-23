package com.skye.lover.logic.chatinput;

/**
 * 私信发送的内容
 */
public class SendingContent {
    private String another, content;// 聊天中的另一方，聊天内容

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SendingContent{" +
                "another='" + another + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
