package com.springboot.provider.common.lifecycle;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021-05-2010:33
 */
@Component
public class ServletContextListenerHandler implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        System.out.println("[ServletContextListener] contextInitialized servlet path: " + servletContext.getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        System.out.println("[ServletContextListener] contextDestroyed servlet path: " + servletContext.getContextPath());
    }
}
