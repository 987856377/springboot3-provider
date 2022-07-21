package com.springboot.provider.common.event;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.event
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-02 09:18
 **/
public class ApplicationMessageEvent {
    private String message;

    public ApplicationMessageEvent() {
    }

    public ApplicationMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApplicationReplyEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
