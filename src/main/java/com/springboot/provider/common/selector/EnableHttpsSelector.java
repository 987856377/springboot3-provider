package com.springboot.provider.common.selector;

import com.springboot.provider.common.selector.annotation.EnableHttps;
import com.springboot.provider.common.selector.config.HttpsConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description 使用 @{@link EnableHttps} 注解, 自动启用 @{@link HttpsConfig} 并将其中的配置注册到spring context中
 * @Project springboot-provider
 * @Package com.springboot.provider.common.selector
 * @Author xuzhenkui
 * @Date 2021-05-27 16:31
 */
public class EnableHttpsSelector implements ImportSelector {
    public static final String HTTPS_CONFIG = "com.springboot.provider.common.selector.config.HttpsConfig";

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{HTTPS_CONFIG};
    }
}
