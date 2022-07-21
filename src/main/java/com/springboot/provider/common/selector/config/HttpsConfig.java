package com.springboot.provider.common.selector.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @Description Https 配置
 * @Project springboot-provider
 * @Package com.springboot.provider.config
 * @Author xuzhenkui
 * @Date 2021-05-27 16:31
 */
public class HttpsConfig {

    @Value("${http.port}")
    private String httpPort;

    @Value("${server.port}")
    private String serverPort;

    /**
     * @Description: 配置 https 加密访问
     * @Throws:
     * @Author: xuzhenkui
     * @Date: 2021-05-27 16:17
     */
    @Bean
    public TomcatServletWebServerFactory servletContainer(@Qualifier("httpConnector") Connector connector) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    /**
     * @Description: 配置 http 跳转向 https
     * @Throws:
     * @Author: xuzhenkui
     * @Date: 2021-05-27 16:18
     */
    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(Integer.parseInt(httpPort));
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(Integer.parseInt(serverPort));
        return connector;
    }

}
