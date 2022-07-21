package com.springboot.provider.common.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationNotifyEvent extends ApplicationEvent {

    private Boolean isNotified = false;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ApplicationNotifyEvent(Object source) {
        super(source);
    }

    public ApplicationNotifyEvent(Object source, Boolean isNotified) {
        super(source);
        this.isNotified = isNotified;
    }

    public Boolean getNotified() {
        return isNotified;
    }

    @Override
    public String toString() {
        return "ApplicationNotifyEvent{" +
                "isNotified=" + isNotified +
                ", source=" + source +
                '}';
    }
}
