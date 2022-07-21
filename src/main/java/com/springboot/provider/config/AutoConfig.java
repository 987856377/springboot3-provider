package com.springboot.provider.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.config
 * @Author xuzhenkui
 * @Date 2022-06-08 17:18
 */
@AutoConfiguration
public class AutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfig.class);

    @Bean
    public CommandLineRunner autoConfigCommandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("AutoConfigCommandLineRunner...");
        };
    }
}
