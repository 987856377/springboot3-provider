package com.springboot.provider.common.selector.annotation;

import com.springboot.provider.common.selector.EnableHttpsSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description 使用此注解，需要在application.properties中添加ssl配置
 *              {@link EnableHttpsSelector#selectImports} 自动将配置文件{@link EnableHttpsSelector#HTTPS_CONFIG}注册到spring context中
 * @Project springboot-provider
 * @Package com.springboot.provider.common.annotation
 * @Author xuzhenkui
 * @Date 2021-05-27 16:30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableHttpsSelector.class)
public @interface EnableHttps {
}
