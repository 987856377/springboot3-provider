package com.springboot.provider.common.lifecycle;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

/**
 * @program: springboot-gradle
 * @package com.spring.development.common.handler
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-11 16:34
 **/
@Component
public class ServletContextInitializerHandler implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("[ServletContextInitializerHandler] servlet onStartup getServerInfo: " + servletContext.getServerInfo());
    }
}
