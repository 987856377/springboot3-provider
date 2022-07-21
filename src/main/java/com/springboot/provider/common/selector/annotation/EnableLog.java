package com.springboot.provider.common.selector.annotation;

import com.springboot.provider.common.enums.LogMode;
import com.springboot.provider.common.selector.LogConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.annotation
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-19 09:44
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogConfigurationSelector.class)
public @interface EnableLog {
    LogMode mode() default LogMode.INFO;
}
