package com.springboot.provider.common.registrar.handler;

import com.springboot.provider.common.registrar.BasicComponentHandler;
import com.springboot.provider.common.registrar.annotation.BasicFilter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.registrar.handler
 * @Author xuzhenkui
 * @Date 2021/10/18 13:26
 */
public class BasicFilterHandler extends BasicComponentHandler {

    public BasicFilterHandler() {
        super(BasicFilter.class);
    }

    @Override
    protected void doHandle(Map<String, Object> attributes, AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(FilterRegistrationBean.class);
        builder.addPropertyValue("asyncSupported", attributes.get("asyncSupported"));
        builder.addPropertyValue("filter", beanDefinition);
        String name = determineName(attributes, beanDefinition);
        builder.addPropertyValue("name", name);
        builder.addPropertyValue("servletNames", attributes.get("servletNames"));
        registry.registerBeanDefinition(name, builder.getBeanDefinition());
    }

    private String determineName(Map<String, Object> attributes, BeanDefinition beanDefinition) {
        return (String) (StringUtils.hasText((String) attributes.get("filterName")) ? attributes.get("filterName")
                : beanDefinition.getBeanClassName());
    }
}
