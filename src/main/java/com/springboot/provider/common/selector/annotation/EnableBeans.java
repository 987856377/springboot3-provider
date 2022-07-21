package com.springboot.provider.common.selector.annotation;

import com.springboot.provider.common.selector.EnableBeansAutoSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: springboot-dev
 * @package com.spring.development.common.annotation
 * @description
 * @author: XuZhenkui
 * @create: 2021-02-11 11:45
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableBeansAutoSelector.class)
public @interface EnableBeans {
    //传入包名
    String[] packages() default {};

    boolean isRecursion() default false;
}
