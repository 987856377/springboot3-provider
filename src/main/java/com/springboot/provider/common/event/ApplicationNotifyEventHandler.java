package com.springboot.provider.common.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationNotifyEventHandler implements ApplicationListener<ApplicationNotifyEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationNotifyEvent event) {
        System.out.println("[ApplicationNotifyEventHandler] onApplicationEvent 已处理事件: " + event.toString());
    }
}
