package com.springboot.provider.common.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Project development
 * @Package com.spring.development.global
 * @Author xuzhenkui
 * @Date 2020/3/7 14:45
 */

/*
* ApplicationContextAware：
* 用来获取ApplicationContext的一个扩展类，ApplicationContext应该是很多人非常熟悉的一个类了，就是spring上下文管理器，
* 可以手动的获取任何在spring上下文注册的bean，我们经常扩展这个接口来缓存spring上下文，包装成静态方法。
* 同时ApplicationContext也实现了BeanFactory，MessageSource，ApplicationEventPublisher等接口，也可以用来做相关接口的事情。
* */
@Component
public class ApplicationContextAwareHandler implements ApplicationContextAware {

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("[ApplicationContextAware] setApplicationContext: " + applicationContext.getApplicationName());
    }
}
