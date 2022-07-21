package com.springboot.provider.common.lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021-05-27 09:41
 */
public class EnvironmentPostProcessorHandler implements EnvironmentPostProcessor {
    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("[EnvironmentPostProcessor] postProcessEnvironment");
    }
}
