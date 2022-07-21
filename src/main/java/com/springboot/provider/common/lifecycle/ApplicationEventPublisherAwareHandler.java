package com.springboot.provider.common.lifecycle;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


/*
* ApplicationEventPublisherAware：
* 用于获取ApplicationEventPublisher的一个扩展类，ApplicationEventPublisher可以用来发布事件，结合ApplicationListener来共同使用。
* 这个对象也可以通过spring注入的方式来获得。
* */
@Component
public class ApplicationEventPublisherAwareHandler implements ApplicationEventPublisherAware {

    /**
     * Set the ApplicationEventPublisher that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param applicationEventPublisher event publisher to be used by this object
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        System.out.println("[ApplicationEventPublisherAware] setApplicationEventPublisher");
    }

}
