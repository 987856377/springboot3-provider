package com.springboot.provider.common.jackson.sensitive1;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {
    String maskChar() default "*";

    SensitiveMode maskFunc() default SensitiveMode.NONE;
}
