package com.springboot.provider.config;

import com.springboot.provider.common.encrypt.EncryptorManager;
import com.springboot.provider.common.interceptor.MybatisDecryptInterceptor;
import com.springboot.provider.common.interceptor.MybatisEncryptInterceptor;
import com.springboot.provider.config.properties.EncryptorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加解密配置
 *
 * @author 老马
 * @version 4.6.0
 */
@Configuration
@ConditionalOnProperty(value = "mybatis-encryptor.enable", havingValue = "true")
public class EncryptorConfig {

    private final EncryptorProperties properties;

    public EncryptorConfig(EncryptorProperties properties) {
        this.properties = properties;
    }

    @Bean
    public EncryptorManager encryptorManager() {
        return new EncryptorManager();
    }

    @Bean
    public MybatisEncryptInterceptor mybatisEncryptInterceptor(EncryptorManager encryptorManager) {
        return new MybatisEncryptInterceptor(encryptorManager, properties);
    }

    @Bean
    public MybatisDecryptInterceptor mybatisDecryptInterceptor(EncryptorManager encryptorManager) {
        return new MybatisDecryptInterceptor(encryptorManager, properties);
    }

}
