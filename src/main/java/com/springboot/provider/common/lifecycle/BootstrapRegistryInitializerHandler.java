package com.springboot.provider.common.lifecycle;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021/6/2 16:27
 */
public class BootstrapRegistryInitializerHandler implements BootstrapRegistryInitializer {
    /**
     * Initialize the given {@link BootstrapRegistry} with any required registrations.
     *
     * @param registry the registry to initialize
     */
    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("[BootstrapRegistryInitializer] initialize");
    }
}
