package com.springboot.provider.module.common.websocket;

public class MessageDTO {
    private String receiveUserId;

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "receiveUserId='" + receiveUserId + '\'' +
                '}';
    }
}
