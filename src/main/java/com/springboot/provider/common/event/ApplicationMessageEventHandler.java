package com.springboot.provider.common.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.event
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-02 09:19
 **/
@Component
public class ApplicationMessageEventHandler {

    @EventListener
    public void handleMessageEvent(ApplicationMessageEvent event) {
        System.out.println("我监听到了 ApplicationReplyEvent 发布的message为:" + event.getMessage());
    }
}
