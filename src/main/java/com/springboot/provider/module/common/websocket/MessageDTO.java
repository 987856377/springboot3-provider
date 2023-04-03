package com.springboot.provider.module.common.websocket;

public class MessageDTO {
    private String receiveUserId;
    private String content;

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "receiveUserId='" + receiveUserId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
