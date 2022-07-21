package com.springboot.provider.common.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/*
* org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
* 这个接口在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
*
* 使用场景：你可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
* */
@Component
public class BeanDefinitionRegistryPostProcessorHandler implements BeanDefinitionRegistryPostProcessor {
    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     *
     * @param registry the bean definition registry used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanDefinitionRegistry");
    }

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanFactory");
    }
}
