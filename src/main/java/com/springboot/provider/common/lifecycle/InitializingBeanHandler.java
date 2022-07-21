package com.springboot.provider.common.lifecycle;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/*
* org.springframework.beans.factory.InitializingBean
* 这个类，顾名思义，也是用来初始化bean的。InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候都会执行该方法。
* 这个扩展点的触发时机在postProcessAfterInitialization之前。
*
* 使用场景：用户实现此接口，来进行系统启动的时候一些业务指标的初始化工作。
* */
@Component
public class InitializingBeanHandler implements InitializingBean {
    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[InitializingBean] afterPropertiesSet");
    }
}
