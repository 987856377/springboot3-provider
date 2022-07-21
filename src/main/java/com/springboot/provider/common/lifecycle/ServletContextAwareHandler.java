package com.springboot.provider.common.lifecycle;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * @program: springboot-gradle
 * @package com.spring.development.common.handler
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-11 16:34
 **/
@Component
public class ServletContextAwareHandler implements ServletContextAware {
    /**
     * Set the {@link ServletContext} that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's {@code afterPropertiesSet} or a
     * custom init-method. Invoked after ApplicationContextAware's
     * {@code setApplicationContext}.
     *
     * @param servletContext the ServletContext object to be used by this object
     * @see InitializingBean#afterPropertiesSet
     * @see ApplicationContextAware#setApplicationContext
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        System.out.println("[ServletContextAware] servlet path: " + servletContext.getContextPath());
    }
}
