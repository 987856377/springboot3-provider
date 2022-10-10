package com.springboot.provider.common.lifecycle;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/*
 * EnvironmentAware：
 * 用于获取EnviromentAware的一个扩展类，这个变量非常有用， 可以获得系统内的所有参数。
 * 当然个人认为这个Aware没必要去扩展，因为spring内部都可以通过注入的方式来直接获得。
 * */
@Component
public class EnvironmentAwareHandler implements EnvironmentAware {

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("[EnvironmentAware] setEnvironment");
    }
}
