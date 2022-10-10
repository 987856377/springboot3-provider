package com.springboot.provider.common.lifecycle;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/*
 * MessageSourceAware：
 * 用于获取MessageSource的一个扩展类，MessageSource主要用来做国际化。
 * */
@Component
public class MessageSourceAwareHandler implements MessageSourceAware {

    /**
     * Set the MessageSource that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param messageSource message source to be used by this object
     */
    @Override
    public void setMessageSource(MessageSource messageSource) {
        System.out.println("[MessageSourceAware] setMessageSource");
    }
}
