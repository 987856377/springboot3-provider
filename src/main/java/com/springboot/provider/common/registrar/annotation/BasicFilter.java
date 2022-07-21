package com.springboot.provider.common.registrar.annotation;

import java.lang.annotation.*;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.registrar.annotation
 * @Author xuzhenkui
 * @Date 2021/10/18 13:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BasicFilter {

    /**
     * @return description of the filter, if present
     */
    String value() default "";
}
