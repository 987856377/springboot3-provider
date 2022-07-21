package com.springboot.provider.common.registrar.handler;

import com.springboot.provider.common.registrar.BasicComponentHandler;
import com.springboot.provider.common.registrar.annotation.BasicListener;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.WebListenerRegistrar;
import org.springframework.boot.web.servlet.WebListenerRegistry;

import java.util.Map;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.registrar.handler
 * @Author xuzhenkui
 * @Date 2021/10/18 13:27
 */
public class BasicListenerHandler extends BasicComponentHandler {

    public BasicListenerHandler() {
        super(BasicListener.class);
    }

    @Override
    protected void doHandle(Map<String, Object> attributes, AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(BasicListenerHandler.ServletComponentWebListenerRegistrar.class);
        builder.addConstructorArgValue(beanDefinition.getBeanClassName());
        registry.registerBeanDefinition(beanDefinition.getBeanClassName() + "Registrar", builder.getBeanDefinition());
    }

    static class ServletComponentWebListenerRegistrar implements WebListenerRegistrar {

        private final String listenerClassName;

        ServletComponentWebListenerRegistrar(String listenerClassName) {
            this.listenerClassName = listenerClassName;
        }

        @Override
        public void register(WebListenerRegistry registry) {
            registry.addWebListeners(this.listenerClassName);
        }

    }
}
