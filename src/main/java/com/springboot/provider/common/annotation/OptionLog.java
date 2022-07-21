package com.springboot.provider.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.annotation
 * @Author xuzhenkui
 * @Date 2021/9/6 14:17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionLog {
    /**
     * 操作类型
     */
    int mode();

    /**
     * 操作来源(页面+操作)
     */
    String source();
}
