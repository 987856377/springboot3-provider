package com.springboot.provider.common.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/*
* org.springframework.beans.factory.config.BeanFactoryPostProcessor
* 这个接口是beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
*
* 在这个时机，用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息。
* */
@Component
public class BeanFactoryPostProcessorHandler implements BeanFactoryPostProcessor {
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
        System.out.println("[BeanFactoryPostProcessor] postProcessBeanFactory");
    }
}
